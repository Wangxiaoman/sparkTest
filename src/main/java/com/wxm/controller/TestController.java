package com.wxm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/test")
@Api(value = "选品controller")
public class TestController {
	
	@GetMapping("/show")
	@ApiOperation(value = "test show", httpMethod = "GET")
	public String showTest(){
		return "show";
	}
}
