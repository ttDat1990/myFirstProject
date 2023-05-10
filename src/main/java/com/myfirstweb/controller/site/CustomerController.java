package com.myfirstweb.controller.site;


import java.util.Optional;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myfirstweb.domain.Customer;
import com.myfirstweb.model.CustomerChangePassDto;
import com.myfirstweb.model.CustomerDto;
import com.myfirstweb.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("site/customer")
public class CustomerController {
	@Autowired
	CustomerService customerService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("register")
	public String add(Model model) {
		model.addAttribute("customer", new CustomerDto());
		return "site/customer/register";
	}

	@PostMapping("save")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("customer") CustomerDto dto,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("site/customer/register");
		}
		
		Customer entity = new Customer();
		BeanUtils.copyProperties(dto, entity);
		
		Optional<Customer> optExist = customerService.findByEmail(entity.getEmail());
		
		if(optExist.isPresent()) {
			
			model.addAttribute("message", "Email has registered!");
			
			return new ModelAndView("site/customer/register", model);
		}

		customerService.save(entity);
		return new ModelAndView("site/customer/success");
	}
	
	@GetMapping("detail")
	public ModelAndView detail(ModelMap model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Optional<Customer> customer = customerService.findByEmail(email);
		if(customer.isPresent()) {
			model.addAttribute("customer", customer.get());
		}
		return new ModelAndView("site/customer/detail", model);
	}
	
	@GetMapping("changepass")
	public ModelAndView changepass(ModelMap model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Optional<Customer> customer = customerService.findByEmail(email);
		if(customer.isPresent()) {
			model.addAttribute("customer", customer.get());
		}
		model.addAttribute("customerdto", new CustomerChangePassDto());
		return new ModelAndView("site/customer/changePass", model);
	}
	
	@PostMapping("savechangepass")
	public ModelAndView savechangepass(ModelMap model, HttpSession session, @Valid @ModelAttribute("customerdto") CustomerChangePassDto dto,
			BindingResult result) {
		String email = (String) session.getAttribute("email");
		Optional<Customer> customer = customerService.findByEmail(email);
		if (result.hasErrors()) {
			model.addAttribute("customer", customer.get());
			return new ModelAndView("site/customer/changePass", model);
		}
		
		if(!customer.isPresent() || !bCryptPasswordEncoder.matches(dto.getPassword(), customer.get().getPassword())) {
			model.addAttribute("customer", customer.get());
			model.addAttribute("message", "Incorrect password !");
			return new ModelAndView("site/customer/changePass", model);
		}
		
		if(!dto.getNewpassword1().equals(dto.getNewpassword2())) {
			model.addAttribute("customer", customer.get());
			model.addAttribute("message", "Password repeat is not match to new password");
			return new ModelAndView("site/customer/changePass", model);
		}
		
		
		if(customer.isPresent() && bCryptPasswordEncoder.matches(dto.getPassword(), customer.get().getPassword()) && dto.getNewpassword1().equals(dto.getNewpassword2())) {
			model.addAttribute("customer", customer.get());
			
			customer.get().setPassword(dto.getNewpassword1());
			customerService.save(customer.get());
			model.addAttribute("message", "Password was changed !");
		} 
		
		
		
		return new ModelAndView("site/customer/changesuccess", model);
	}
	
	@GetMapping("accorder")
	public ModelAndView accorder(ModelMap model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Optional<Customer> customer = customerService.findByEmail(email);
		if(customer.isPresent()) {
			model.addAttribute("customer", customer.get());
		}
		
		return new ModelAndView("site/customer/accorder", model);
	
	}
	
	@GetMapping("accinfo")
	public ModelAndView accinfo(ModelMap model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Optional<Customer> customer = customerService.findByEmail(email);
		if(customer.isPresent()) {
			model.addAttribute("customer", customer.get());
		}
		return new ModelAndView("site/customer/accinfo", model);
	}
	
//	@GetMapping("edit/{username}")
//	public ModelAndView edit(ModelMap model, @PathVariable("username") String username) {
//
//		Optional<Account> opt = accountService.findById(username);
//		AccountDto dto = new AccountDto();
//		if (opt.isPresent()) {
//			Account entity = opt.get();
//			BeanUtils.copyProperties(entity, dto);
//			dto.setIsEdit(true);
//
//			dto.setPassword("");
//			model.addAttribute("account", dto);
//			return new ModelAndView("admin/accounts/addOrEdit", model);
//		}
//		model.addAttribute("message", "account is not existed");
//		return new ModelAndView("forward:/admin/accounts", model);
//	}

	
}
