package com.mpcz.deposit_scheme.backend.api.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.ratelimitresilance4j.DynamicRateLimiterService;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

	@Autowired
	private RateLimiterController rateLimiterController;

	@RateLimiter(name = "normalApiLimiter", fallbackMethod = "rateLimitFallback")
	@GetMapping("/hello")
	public String hello() {
		return "Hello API Success";
	}

	@RateLimiter(name = "paymentApiLimiter", fallbackMethod = "paymentFallback")
	@GetMapping("/payment")
	public String payment() {
		return "Payment API Success";
	}

	// ---------- Fallback Methods ----------

	public String rateLimitFallback(RequestNotPermitted ex) {
		return "Too many requests! Please try after some time................";
	}

	public String paymentFallback(RequestNotPermitted ex) {
		return "Payment API rate limit exceeded. Try later................";
	}

	@RateLimiter(name = "normalApiLimiter", fallbackMethod = "rateLimitFallback")
	@GetMapping("/paymentsss")
	public String paymentsss(HttpServletRequest request) throws UnknownHostException {
		ResponseEntity<String> ratelimitResponse = rateLimiterController.getData(request);
		System.err.println("aaaaaaaaaaaaaaaaaaa :" + ratelimitResponse.getBody());
		return "Payment API Successsssssssssssss";
	}

	@Autowired
	private DynamicRateLimiterService rateLimiterService;

	@GetMapping("/data")
	public ResponseEntity<String> getData(HttpServletRequest request) throws UnknownHostException {

		InetAddress localHost = InetAddress.getLocalHost();
		String ipAddress = localHost.getHostAddress();
		System.out.println("Local IP Address: " + ipAddress);
		rateLimiterService.validateRequest(ipAddress);

		return ResponseEntity.ok("API response success : " + ipAddress);
	}

//	public static void main(String[] args) {
//		System.err.println(paymentsss());
//		;
//	}
}
