package com.myfirstweb.controller.site;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
import com.myfirstweb.model.CartItemDto;
import com.myfirstweb.model.CategoryDto;
import com.myfirstweb.service.CartService;
import com.myfirstweb.service.CategoryService;
import com.myfirstweb.service.ProductService;
import com.myfirstweb.service.StorageService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("site/cart")
public class CartController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	CartService cartService;

	@GetMapping("add/{productId}")
	public String add(@PathVariable("productId") Long ProductId) {
		Optional<Product> product = productService.findById(ProductId);
		if(product.isPresent()) {
			CartItemDto item = new CartItemDto();
			BeanUtils.copyProperties(product.get(), item);
			item.setQuantity(1);
			cartService.add(item);
			
		}
		return "forward:/site/products";
	}

	@GetMapping("delete/{productId}")
	public String delete(@PathVariable("productId") Long productId) {
		cartService.remove(productId);

		return "redirect:/site/cart";
	}

	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@PostMapping("update")
	public String update(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity) {
		cartService.update(productId, quantity);

		return "redirect:/site/cart";
	}

	@RequestMapping("")
	public String list(ModelMap model) {
		Collection<CartItemDto> cartItems = cartService.getCartItems();
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", cartService.getAmount());
		model.addAttribute("NoOfItems", cartService.getCount());
		
		return "site/cart/list";
	}
}
