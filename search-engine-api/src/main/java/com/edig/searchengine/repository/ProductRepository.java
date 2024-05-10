package com.edig.searchengine.repository;

import com.edig.searchengine.entity.Category;
import com.edig.searchengine.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@Repository
@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<List<Product>> findByCategory(Category category);
    Optional<Product> findProductByName(String name);
    @Transactional
    @Modifying
    void deleteProductById(Long id);
    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findDistinctBrand();
    @Query("SELECT DISTINCT p.madeIn FROM Product p")
    List<String> findDistinctMadeIn();
    @Query("select distinct c.name from Product p join p.category c")
    List<String> findDistinctCategoryNames();
   @Query("SELECT DISTINCT p.name FROM Product p")
    List<String> findDistinctByName();
}

