package com.myfirstweb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.myfirstweb.domain.Category;
import com.myfirstweb.domain.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	List<Category> findByNameContaining(String name);
	Page<Category> findByNameContaining(String name, Pageable pageable);
	@Query("SELECT p FROM Product p Where p.category.id=?1")
	List<Product> findByCategoryId(Long categoryId);
	
	
}
