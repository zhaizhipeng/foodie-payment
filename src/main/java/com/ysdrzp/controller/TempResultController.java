package com.ysdrzp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TempResultController {

	@GetMapping("/alipayResult")
	public String alipayResult(HttpServletRequest request, HttpServletResponse response) {
		return "alipayResult";
	}
}
