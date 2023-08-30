package com.project.fooddeliverysystem.security;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class SecurityService {
	
	public boolean isActionAllowed(String userIdentifier, HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession();
		String sessionUserIdentifier = (String) session.getAttribute("userIdentifier");
		String sessionUserType = (String) session.getAttribute("userType");
		
		if(sessionUserType != null && sessionUserType.equalsIgnoreCase("admin")) {
			return true;
		}
		
		if(sessionUserIdentifier!=null && sessionUserIdentifier.equalsIgnoreCase(userIdentifier)) {
			return true ;
		}
		
		return false;
	}
	
	public boolean isAdmin(HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession();
		//String sessionUserIdentifier = (String) session.getAttribute("userIdentifier");
		String sessionUserType = (String) session.getAttribute("userType");
		
		if(sessionUserType != null && sessionUserType.equalsIgnoreCase("admin")) {
			return true;
		}
		
		return false;
	}
	
	public boolean isloggedInAdmin(String adminId, HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession();
		String sessionUserIdentifier = (String) session.getAttribute("userIdentifier");
		String sessionUserType = (String) session.getAttribute("userType");
		
		if((sessionUserIdentifier!=null && sessionUserIdentifier.equalsIgnoreCase(adminId)) && (sessionUserType != null && sessionUserType.equalsIgnoreCase("admin"))) {
			return true;
		}
		
		return false;
	}
	
	public boolean isUser(String userId, HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession();
		String sessionUserIdentifier = (String) session.getAttribute("userIdentifier");
		String sessionUserType = (String) session.getAttribute("userType");
		
		if(sessionUserIdentifier!=null && sessionUserIdentifier.equalsIgnoreCase(userId) && sessionUserType != null && sessionUserType.equalsIgnoreCase("user")) {
			return true;
		}
		
		return false;
	}

	/*
	 * public Object getUserFromSession(HttpServletRequest httpRequest) {
	 * HttpSession session = httpRequest.getSession(); String sessionUserIdentifier
	 * = (String) session.getAttribute("userIdentifier");
	 * 
	 * if(sessionUserIdentifier==null) { return null ; }
	 * 
	 * // check if this user is present in user table // database all data + User //
	 * set usertype is user
	 * 
	 * // admin // setuesrtye as admin
	 * 
	 * return false; }
	 */

}
