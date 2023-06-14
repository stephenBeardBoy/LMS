package com.spark.lms;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.spark.lms.service.UserService;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession httpSession;
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();
        String displayName = userService.getByUsername( user.getUsername() ).getDisplayName();
        String role = userService.getByUsername( user.getUsername() ).getRole();
        Long user_id = userService.getByUsername( user.getUsername() ).getUser_id();
        httpSession.setAttribute("loggedInUserName", displayName);
        httpSession.setAttribute("loggedInRole", role);
        httpSession.setAttribute("loggedInUserId", user_id);
	}
	
}
