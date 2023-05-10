package com.myfirstweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myfirstweb.domain.Customer;
import com.myfirstweb.model.CustomerLoginDto;
import com.myfirstweb.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CustomerLoginController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private HttpSession session;
	
	@GetMapping("login")
	public String login(ModelMap model) {
		model.addAttribute("customer", new CustomerLoginDto());
		return "/site/customer/login";
	}
	
	@PostMapping("login")
	public ModelAndView login(ModelMap model, @Valid @ModelAttribute("customer") CustomerLoginDto dto, BindingResult result) {
		if(result.hasErrors()) {
			return new ModelAndView("/site/customer/login", model);
		}
		
		Customer customer = customerService.login(dto.getEmail(), dto.getPassword());
		
		if(customer == null) {
			model.addAttribute("message", "Invalid email or password");
			return new ModelAndView("/site/customer/login", model);
		}
		session.setAttribute("email", customer.getEmail());
		
		
		
		return new ModelAndView("forward:/site/home", model);
	}
	
	@GetMapping("logout")
	public String logout(HttpSession session) {
	
		session.removeAttribute("email");
		return "/site/home";
	}
}
