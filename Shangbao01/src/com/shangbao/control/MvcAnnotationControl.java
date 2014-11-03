package com.shangbao.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MvcAnnotationControl {

	@RequestMapping({"/","/hello"})
	public String Hello(String username, Model model){
		model.addAttribute("username", username);
		System.out.println("Hello " + username);
		return "Hello";
	}
}
