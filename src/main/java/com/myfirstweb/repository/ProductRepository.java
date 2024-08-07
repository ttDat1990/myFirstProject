package com.myfirstweb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myfirstweb.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByNameContaining(String name);
	Page<Product> findByNameContaining(String name, Pageable pageable);
	@Query("SELECT p FROM Product p Where p.unitPrice BETWEEN ?1 AND ?2")
	List<Product> findByPrice1(Double min, Double max);
	@Query("SELECT p FROM Product p Where p.unitPrice BETWEEN ?1 AND ?2")
	List<Product> findByPrice1(Double min, Double max, Pageable pageable);
}
