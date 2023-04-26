package com.myfirstweb.controller.admin;


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
@RequestMapping("admin/products")
public class ProductController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	StorageService storageService;
	
	@ModelAttribute("categories")
	public List<CategoryDto> getCategory() {
		return categoryService.findAll().stream().map(item->{
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).toList();
	}
	
	@GetMapping("add")
	public String add(Model model) {
		ProductDto dto = new ProductDto();
		dto.setIsEdit(false);
		model.addAttribute("product", dto);
		return "admin/products/addOrEdit";
	}

	@GetMapping("edit/{productId}")
	public ModelAndView edit(ModelMap model, @PathVariable("productId") Long productId) {

		Optional<Product> opt = productService.findById(productId);
		ProductDto dto = new ProductDto();
		if (opt.isPresent()) {
			Product entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setCategoryId(entity.getCategory().getCategoryId());
			dto.setIsEdit(true);

			model.addAttribute("product", dto);
			return new ModelAndView("admin/products/addOrEdit", model);
		}
		model.addAttribute("message", "Product is not existed");
		return new ModelAndView("forward:/admin/products", model);
	}
	
	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@GetMapping("delete/{productId}")
	public ModelAndView delete(ModelMap model, @PathVariable("productId") Long productId) throws IOException {

		Optional<Product> opt = productService.findById(productId);
		
		if(opt.isPresent()) {
			if(!StringUtils.isEmpty(opt.get().getImage())) {
				storageService.delete(opt.get().getImage());
			}
			productService.delete(opt.get());
			model.addAttribute("message", "Product is Deleted");
		} else {
			model.addAttribute("message", "Product is not found");
		}

		return new ModelAndView("forward:/admin/products", model);
	}
	
	@GetMapping("deleteMany")
	public ModelAndView deleteMany(ModelMap model, @RequestParam("selectMember") List<Long> list){

		if(list != null && !list.isEmpty()) {
			list.stream().forEach(item -> {
				System.out.println("id item: "+item);
				Optional<Product> opt = productService.findById(item);
				
				if(opt.isPresent()) {
					if(!StringUtils.isEmpty(opt.get().getImage())) {
						try {
							storageService.delete(opt.get().getImage());
						} catch (IOException e) {
							model.addAttribute("message1", "Product " + item + " is can not delete");
							e.printStackTrace();
						}
					}
					productService.delete(opt.get());
				} 	
			});
			
			model.addAttribute("message", list.size() + " Products had Deleted");
		} else {
			model.addAttribute("message", "Please Choose Product");
		}

		return new ModelAndView("forward:/admin/products", model);
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("product") ProductDto dto,
			BindingResult result) throws IOException {
		// Nếu quá trình binding gặp lỗi thì trả về lại trang edit
		if (result.hasErrors()) {
			return new ModelAndView("admin/products/addOrEdit");
		}
		
		//Tạo 1 entity mới, sau đó coppy dữ liệu từ dto vào entity
		Product entity = new Product();
		BeanUtils.copyProperties(dto, entity);
		
		// Tạo 1 category mới, set id cho category lấy từ dto (do trong entity không có trường category id mà chỉ có nguyên 1 category)
		Category category = new Category();
		category.setCategoryId(dto.getCategoryId());
		
		// setcategory vừa tạo cho entity (sẽ tự join column categoryid với bảng category)
		entity.setCategory(category);
		
		// Nếu trên dto người dùng có chọn file hình và có thông tin của product id nghĩa là đang edit(do productid tự đông generate được gửi lên từ dtb khi edit, tạo mới sẽ ko có)
		if(!dto.getImageFile().isEmpty() && dto.getProductId() != null) {
			//Trường hợp là edit thì tạo 1 chuỗi random
			UUID uuid = UUID.randomUUID();
			String uuString = uuid.toString();
			// gán chuỗi random đặt cho tên của file lấy từ dto
			entity.setImage(storageService.getStoredFilename(dto.getImageFile(), uuString));
			//lưu hình vào sever và lưu tên hình vào entity
			storageService.store(dto.getImageFile(), entity.getImage());
			// Bắt đầu xóa hình cũ của product:  tạo optional opt để lấy lên product đang edit
			Optional<Product> opt = productService.findById(dto.getProductId());
			//Nếu tìm thấy product có lưu hình thì xóa nó
			if(!StringUtils.isEmpty(opt.get().getImage())) {
				storageService.delete(opt.get().getImage());
			}
			model.addAttribute("message", "Product is updated!");
			
		// else if Trường hợp có chọn file hình nhưng không có thông tin của product id từ dto nghĩa là đang tạo mới sản phẩm
		} else if (!dto.getImageFile().isEmpty() && dto.getProductId() == null) {
			// Lưu hình ảnh vào sever giống ở trên và không cần xóa hình cũ vì đây là tạo mới
			UUID uuid = UUID.randomUUID();
			String uuString = uuid.toString();
			entity.setImage(storageService.getStoredFilename(dto.getImageFile(), uuString));
			storageService.store(dto.getImageFile(), entity.getImage());
			model.addAttribute("message", "Product is created!");
		
		// Trường hợp dto trả về không có hình ảnh, nhưng lại có productid nghĩa là edit nhưng không chọn hình ảnh mới
		} else if (dto.getImageFile().isEmpty() && dto.getProductId() != null) {
			//tạo optional opt để lấy lên product đang edit
			Optional<Product> opt = productService.findById(dto.getProductId());
			//Nếu tìm thấy product có lưu hình thì:
			if(!StringUtils.isEmpty(opt.get().getImage())) {
				// Lấy tên image của nó set cho entity là xong vì chỉ cần tên thì sẽ dẫn tới file hình cũ lưu trên sever
				entity.setImage(opt.get().getImage());
			}
			model.addAttribute("message", "Product is updated!");
		}
		
		//Lưu entity xuống dtb (nếu edit thì sẽ tự ghi đè(update))
		productService.save(entity);

		return new ModelAndView("forward:/admin/products", model);
	}

	@RequestMapping("")
	public String list(ModelMap model, 
			@RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);
		
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
			
			if(totalPages > 5) {
				if(end == totalPages) start = end - 5;
				else if(start == 1) end = start + 5;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
					.boxed()
					.collect(Collectors.toList());
			
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("productPage", resultPage);

		return "admin/products/list";
	}

}
