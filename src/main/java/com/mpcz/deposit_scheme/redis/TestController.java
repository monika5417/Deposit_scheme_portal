package com.mpcz.deposit_scheme.redis;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class TestController {
	
	
	@GetMapping("/getData")
	@ResponseBody
	public String getData() {
		return "Hello World";
	}

}
