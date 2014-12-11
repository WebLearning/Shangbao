package com.shangbao.web.control;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shangbao.model.persistence.Article;

@Controller
@RequestMapping("/picture")
public class PictureController {
	
	@RequestMapping(value="/newPicture", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void addPicture(@RequestBody Article article){
		
	}
	
	
}
