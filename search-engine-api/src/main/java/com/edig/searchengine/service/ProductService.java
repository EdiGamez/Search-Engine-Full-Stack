package com.edig.searchengine.service;

import com.edig.searchengine.dto.ProductDTO;
import com.edig.searchengine.entity.Category;
import com.edig.searchengine.entity.Product;
import com.edig.searchengine.exceptions.CategoryNotExistsException;
import com.edig.searchengine.exceptions.ProductAlreadyExistsException;
import com.edig.searchengine.exceptions.ProductNotFoundException;
import com.edig.searchengine.mapper.ProductMapper;
import com.edig.searchengine.repository.CategoryRepository;
import com.edig.searchengine.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.edig.searchengine.constants.ProductConstants.*;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    /**
     * Add product.
     *
     * @param productDTO the product dto
     */
    @Override
    public void addProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toProduct(productDTO);
        Optional<Product> productOptional = productRepository.findProductByName(product.getName());
        if (productOptional.isPresent()) {
            throw new ProductAlreadyExistsException("Product with name " + product.getName() + " already exists");
        } else {
            productRepository.save(product);
        }

    }

    /**
     * Gets category by name.
     *
     * @param name the Categories name
     * @return the category by name
     */
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    /**
     * List all products list.
     *
     * @return the list
     */
    @Override
    public List<ProductDTO> listAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(productMapper.toProductDTO(product));
        }
        return productDTOs;
    }

    /**
     * List products by category list.
     *
     * @param categoryName the category name
     * @return the list
     */
    @Override
    public List<ProductDTO> listProductsByCategory(String categoryName) {
        Category category = categoryRepository.findCategoryByName(categoryName);
        List<Product> products = productRepository.findByCategory(category).orElseThrow(
                () -> new CategoryNotExistsException("Category with name " + categoryName + " not found")
        );
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for category " + categoryName);
        }
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(productMapper.toProductDTO(product));
        }
        return productDTOs;
    }


    /**
     * Delete product boolean.
     *
     * @param productName the product name
     * @return the boolean
     */
    @Override
    public boolean deleteProduct(String productName) {
        Product product = getProduct(productName);
        productRepository.deleteProductById(product.getId());
        return true;
    }

    private Product getProduct(String productName) {
        Product product = productRepository.findProductByName(productName).orElseThrow(
                () -> new ProductNotFoundException("Product with name " + productName + " not found")
        );
        return product;
    }

    /**
     * Update product.
     *
     * @param productDTO the product dto
     */
    @Override
    public boolean updateProduct(ProductDTO productDTO) {
        Product existingProduct = getProduct(productDTO.getName());

        existingProduct.setName(productDTO.getName());
        existingProduct.setBrand(productDTO.getBrand());
        existingProduct.setMadeIn(productDTO.getMadeIn());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCategory(categoryRepository.findCategoryByName(productDTO.getCategoryName()));

        productRepository.save(existingProduct);

        return true;
    }

    /**
     * Gets product by name.
     *
     * @param name the product name
     * @return the product by name
     */
    @Override
    public ProductDTO getProductByName(String name) {
        Product product = getProduct(name);
        return productMapper.toProductDTO(product);
    }

    /**
     * Gets all categories.
     *
     * @return the categories
     */
    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Find products list.
     *
     * @param name     the name
     * @param price    the price
     * @param madeIn   the made in
     * @param category the category
     * @return the list
     */
    @Override
    public List<ProductDTO> findProducts(String name, Float price, String madeIn, String category) {
        return List.of();
    }

    /**
     * Search products list.
     *
     * @param searchQuery the query
     * @return the list
     */
    @Override
    public List<ProductDTO> searchProducts(String searchQuery) {

        String searchQueryLowerCase = searchQuery.toLowerCase();

        List<String> countries = productRepository.findDistinctMadeIn();
        List<String> brands = productRepository.findDistinctBrand();
        List<String> categories = productRepository.findDistinctCategoryNames();
        List<String> names = productRepository.findDistinctByName();


        boolean cheap = false;

        Specification<Product> spec = Specification.where(null);

        for (String country : countries) {
            if (searchQueryLowerCase.contains(country.toLowerCase())) {
                spec = spec.and((root, query, cb) -> cb.like(root.get(MADE_IN), country));
            }
        }
        for (String brand : brands) {
            if (searchQueryLowerCase.contains(brand.toLowerCase())) {
                spec = spec.and((root, query, cb) -> cb.like(root.get(BRAND), brand));
            }
        }

        for (String category : categories) {
            if (searchQueryLowerCase.contains(category.toLowerCase())) {
                spec = spec.and((root, query, cb) -> cb.like(root.get(CATEGORY).get(NAME), category));
            }
        }
        for (String producto : names) {
            if (producto.toLowerCase().contains(searchQueryLowerCase)) {
                spec = spec.and((root, query, cb) -> cb.like(root.get(NAME), "%" + searchQuery + "%"));
            }
        }

        Pattern pattern = Pattern.compile("\\$?\\d+");
        Matcher matcher = pattern.matcher(searchQuery);
        if (matcher.find()) {
            float price = 0;
            try {
                if (matcher.group().startsWith(MONEY_SYMBOL)) {
                    price = Float.parseFloat(matcher.group().substring(1));

                } else {
                    price = Float.parseFloat(matcher.group());
                }
            } catch (NumberFormatException e) {
                //Ignore
            }
            float finalPrice = price;
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get(PRICE), finalPrice));
        }

        if (searchQuery.toLowerCase().contains(CHEAP)) {
            cheap = true;

        }
        List<Product> result;

        if (spec.equals(Specification.where(null))) {
            throw new ProductNotFoundException(STATUS_MESSAGE_NOT_FOUND);
        } else {
            if (cheap) {
                result = productRepository.findAll(spec, Sort.by(Sort.Direction.ASC, PRICE)).stream().limit(10).toList();
            } else {
                result = productRepository.findAll(spec);
            }
            return result.stream().map(productMapper::toProductDTO).collect(Collectors.toList());

        }
    }
}

