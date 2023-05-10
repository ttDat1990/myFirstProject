package com.myfirstweb.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.myfirstweb.domain.Customer;

public interface CustomerService {

	void deleteAll();

	<S extends Customer> List<S> findAll(Example<S> example, Sort sort);

	<S extends Customer> List<S> findAll(Example<S> example);

	void deleteAll(Iterable<? extends Customer> entities);

	void deleteAllById(Iterable<? extends Integer> ids);

	Customer getReferenceById(Integer id);

	void delete(Customer entity);

	Customer getById(Integer id);

	void deleteById(Integer id);

	long count();

	<S extends Customer, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	Customer getOne(Integer id);

	void deleteAllInBatch();

	<S extends Customer> boolean exists(Example<S> example);

	<S extends Customer> long count(Example<S> example);

	void deleteAllByIdInBatch(Iterable<Integer> ids);

	boolean existsById(Integer id);

	void deleteAllInBatch(Iterable<Customer> entities);

	Optional<Customer> findById(Integer id);

	<S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteInBatch(Iterable<Customer> entities);

	List<Customer> findAllById(Iterable<Integer> ids);

	List<Customer> findAll();

	<S extends Customer> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends Customer> S saveAndFlush(S entity);

	Page<Customer> findAll(Pageable pageable);

	void flush();

	List<Customer> findAll(Sort sort);

	<S extends Customer> Optional<S> findOne(Example<S> example);

	<S extends Customer> List<S> saveAll(Iterable<S> entities);

	<S extends Customer> S save(S entity);

	Optional<Customer> findByEmail(String email);

	Customer login(String email, String password);



	

	

}
