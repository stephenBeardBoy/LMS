package com.spark.lms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Member;
import com.spark.lms.model.User;
import com.spark.lms.service.MemberService;
import com.spark.lms.service.UserService;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
    private UserService userService;
	
	@ModelAttribute(name = "memberTypes")
	public List<String> memberTypes() {
		return Constants.MEMBER_TYPES;
	}
	
	@RequestMapping(value = {"/", "/list"},  method = RequestMethod.GET)
	public String showMembersPage(Model model) {
		model.addAttribute("members", memberService.getAll());
		return "/member/list";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addMemeberPage(Model model) {
		model.addAttribute("member", new Member());
		return "/member/form";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editMemeberPage(@PathVariable(name = "id") Long id, Model model) {
		Member member = memberService.get( id );
		if( member != null ) {
			model.addAttribute("member", member);
			return "/member/form";
		} else {
			return "redirect:/member/add";
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveMember(@Valid Member member, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
		if( bindingResult.hasErrors() ) {
			return "/member/form";
		}
		
		if( member.getId() == null ) {
			
			System.out.println("member null--->"+memberService.getExist(member.getContact()));
			if(memberService.getExist(member.getContact()) != null) {
				System.out.println("member null--->"+member.getId());
				redirectAttributes.addFlashAttribute("errorMsg", "Contact Already exist failed to add user");
				bindingResult.rejectValue("contact", "contact", "contact already exists");
				return "redirect:/member/add";
			}else {
				memberService.addNew(member);
				System.out.println("member id--->"+member.getId());
				userService.addNew(new User(member.getFirstName(), member.getFirstName(), member.getContact(), Constants.ROLE_USER, member.getId()));
				redirectAttributes.addFlashAttribute("successMsg", "'" + member.getFirstName()+" "+member.getMiddleName() + "' is added as a new member.");
				return "redirect:/member/add";
			}
			
			
			
//			if(memberService.getExist(member.getContact()) != "") {
//				memberService.addNew(member);
//				System.out.println("member id--->"+member.getId());
//				userService.addNew(new User(member.getFirstName(), member.getFirstName(), member.getContact(), Constants.ROLE_USER, member.getId()));
//				redirectAttributes.addFlashAttribute("successMsg", "'" + member.getFirstName()+" "+member.getMiddleName() + "' is added as a new member.");
//				return "redirect:/member/add";
//			}else {
//				System.out.println("member contact----------------->" + member.getContact());
//				redirectAttributes.addFlashAttribute("Duplicate Contact Exsist member add operation failed");
//				return "redirect:/member/add";
//			}
			
		} else {
			Member updatedMember = memberService.save( member );
			redirectAttributes.addFlashAttribute("successMsg", "Changes for '" + member.getFirstName()+" "+member.getMiddleName() + "' are saved successfully. ");
			return "redirect:/member/edit/" + updatedMember.getId();
		}
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String removeMember(@PathVariable(name = "id") Long id, Model model) {
		Member member = memberService.get( id );
		if( member != null ) {
			if( memberService.hasUsage(member) ) {
				model.addAttribute("memberInUse", true);
				return showMembersPage(model);
			} else {
				memberService.delete(id);
			}
		}
		return "redirect:/member/list";
	}
	
	
	
	
}
