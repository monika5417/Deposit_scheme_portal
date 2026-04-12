///**
// * 
// */
//package com.mpcz.deposit_scheme.ssoflow;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author Monika Rajpoot
// * @since 24-Apr-2026
// * @description TestSSO.java class description
// */
//
//@RestController
//@RequestMapping("/api/sso")
//public class TestSSO {
//
//	 @GetMapping("/test")
//	    public String test(HttpServletRequest request) {
//
//	        String authHeader = request.getHeader("Authorization");
//
//	        return "Token: " + authHeader;
//	    }
//}