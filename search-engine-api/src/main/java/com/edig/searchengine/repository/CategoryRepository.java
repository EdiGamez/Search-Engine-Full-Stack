package com.edig.searchengine.repository;

import com.edig.searchengine.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin("http://localhost:4200")
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find by name category.
     *
     * @param name the name
     * @return the category
     */
    Category findCategoryByName(String name);

    List<Category> findAll();
}
