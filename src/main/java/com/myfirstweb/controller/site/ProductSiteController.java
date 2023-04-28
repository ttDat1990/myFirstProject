package com.myfirstweb.controller.site;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myfirstweb.domain.Category;
import com.myfirstweb.domain.Product;
import com.myfirstweb.model.CategoryDto;
import com.myfirstweb.model.ProductDto;
import com.myfirstweb.service.CategoryService;
import com.myfirstweb.service.ProductService;
import com.myfirstweb.service.StorageService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("site/products")
public class ProductSiteController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	StorageService storageService;
	
	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@RequestMapping("")
	public String list(ModelMap model, 
			@RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(20);
		
		Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("name"));
		Page<Product> resultPage = null;
		
		if (StringUtils.hasText(name)) {
			resultPage = productService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		} else {
			resultPage = productService.findAll(pageable);
		}
		
		int totalPages = resultPage.getTotalPages();
		if(totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			
			if(totalPages > 20) {
				if(end == totalPages) start = end - 20;
				else if(start == 1) end = start + 20;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
					.boxed()
					.collect(Collectors.toList());
			
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("productPage", resultPage);

		return "site/products/list";
	}

}
