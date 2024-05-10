package com.edig.searchengine.service;

import com.edig.searchengine.dto.ProductDTO;
import com.edig.searchengine.entity.Category;

import java.util.List;

/**
 * The interface Product service.
 */
public interface IProductService {

    /**
     * Add product.
     *
     * @param productDTO the product dto
     */
    void addProduct(ProductDTO productDTO);

    /**
     * Gets category by name.
     *
     * @param name the Categories name
     * @return the category by name
     */
    Category getCategoryByName(String name);

    /**
     * List all products list.
     *
     * @return the list
     */
    List<ProductDTO> listAllProducts();

    /**
     * List products by category list.
     *
     * @param categoryName the category name
     * @return the list
     */
    List<ProductDTO> listProductsByCategory(String categoryName);


    /**
     * Delete product boolean.
     *
     * @param productName the product name
     * @return the boolean
     */
    boolean deleteProduct(String productName);


    /**
     * Update product boolean.
     *
     * @param productDTO the product dto
     * @return the boolean
     */
    boolean updateProduct(ProductDTO productDTO);

    /**
     * Gets product by name.
     *
     * @param name the product name
     * @return the product by name
     */
    ProductDTO getProductByName(String name);

    /**
     * Gets all categories.
     *
     * @return the categories
     */
    List<Category> getCategories();


    /**
     * Find products list.
     *
     * @param name     the name
     * @param price    the price
     * @param madeIn   the made in
     * @param category the category
     * @return the list
     */
    List<ProductDTO> findProducts(String name, Float price, String madeIn, String category);

    /**
     * Search products list.
     *
     * @param query the query
     * @return the list
     */
    List<ProductDTO> searchProducts(String query);
}
