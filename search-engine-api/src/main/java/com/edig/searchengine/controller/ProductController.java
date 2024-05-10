package com.edig.searchengine.controller;

import com.edig.searchengine.constants.ProductConstants;
import com.edig.searchengine.dto.ProductDTO;
import com.edig.searchengine.dto.ResponseDTO;
import com.edig.searchengine.entity.Category;
import com.edig.searchengine.entity.Product;
import com.edig.searchengine.entity.SearchQuery;
import com.edig.searchengine.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path="/products/v1", produces = "application/json")
@AllArgsConstructor
@Validated
@Tag(name = "Product REST API", description = "The Product REST API")
@CrossOrigin("http://localhost:4200")
public class ProductController {
    private ProductService productService;

    @Operation(summary = "Add a new product",
            description = "Add a new product to the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping("/add")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody ProductDTO product) {
        productService.addProduct(product);
        return ResponseEntity.status(201).body(new ResponseDTO(ProductConstants.STATUS_CODE_SUCCESS, ProductConstants.STATUS_MESSAGE_SUCCESS));
    }

    @Operation(summary = "List all products",
            description = "List all products from the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Products found")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/list")
    public ResponseEntity<Object> listAll() {
        List<ProductDTO> products = productService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "List products by category",
            description = "List products by category from the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Products found")
    @ApiResponse(responseCode = "404", description = "Products not found")

    @GetMapping("/list/category")
    public ResponseEntity<Object> listProductsByCategory(@RequestParam String name) {
        List<ProductDTO> products = productService.listProductsByCategory(name);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get a product by his name",
            description = " Get a product by his name from the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/list/{name}")
    public ResponseEntity<Object> listProductByName(@PathVariable String name) {
        ProductDTO product = productService.getProductByName(name);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Update a product",
            description = "Update a product in the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping("/update")
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody ProductDTO product) {
        boolean updated = productService.updateProduct(product);
        if (updated) {
            return ResponseEntity.ok(new ResponseDTO(ProductConstants.STATUS_CODE_SUCCESS, ProductConstants.STATUS_MESSAGE_SUCCESS));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO(ProductConstants.STATUS_500_INTERNAL_SERVER_ERROR, ProductConstants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "Delete a product",
            description = "Delete a product from the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Object> deleteProduct(@PathVariable String name) {
        boolean deleted = productService.deleteProduct(name);
        if (deleted) {
            return ResponseEntity.ok(new ResponseDTO(ProductConstants.STATUS_CODE_SUCCESS, ProductConstants.STATUS_MESSAGE_SUCCESS));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO(ProductConstants.STATUS_500_INTERNAL_SERVER_ERROR, ProductConstants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "List all categories",
            description = "List all categories from the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Categories found")
    @ApiResponse(responseCode = "404", description = "Categories not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/categories")
    public ResponseEntity<Object> listAllVategories() {
        List<Category> categories = productService.getCategories();
        return ResponseEntity.ok(categories);
    }

@Operation(summary = "Search products",
            description = "Search products from the database",
            tags = {"Product REST API"})
    @ApiResponse(responseCode = "200", description = "Products found")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
@CrossOrigin(origins = "http://localhost:4200")
@GetMapping("/search")
    public ResponseEntity<Object> searchProducts(@RequestParam String query){

    List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

}
