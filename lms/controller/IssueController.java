package com.spark.lms.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Category;
import com.spark.lms.service.CategoryService;
import com.spark.lms.service.IssueService;

@Controller
@RequestMapping(value = "/issue")
public class IssueController {

	@Autowired
	private IssueService issueService;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute(name = "memberTypes")
	public List<String> memberTypes() {
		return Constants.MEMBER_TYPES;
	}
	
	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryService.getAllBySort();
	}
	
	@RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
	public String listIssuePage(Model model) {	
		
		String role = (String) httpSession.getAttribute("loggedInRole");
		Long user_id = (Long) httpSession.getAttribute("loggedInUserId");
		
		if(user_id != 0) {
			model.addAttribute("issues", issueService.getAllUnreturnedByID(user_id));
		}else {
			model.addAttribute("issues", issueService.getAllUnreturned());
		}
		
		return "/issue/list";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newIssuePage(Model model) { 
		return "/issue/form";
	}
	
}
