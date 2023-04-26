package com.myfirstweb.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myfirstweb.model.AccountDto;

@Controller
@RequestMapping("")
public class HomeController {

	@GetMapping("")
	public String add(Model model) {
		
		return "site/home";
	}
}
