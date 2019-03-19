package com.example.demo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginComtroller {

	@GetMapping(value = { "", "/", "index" })
	public String index(HttpServletRequest request) {
		Object username = request.getSession().getAttribute("username");
		// 非空处理
		if (username != null) {
			System.out.println(username.toString() + "用户进入首页");
			return "index";
		}
		return "redirect:login";
	}

	@GetMapping(value = "login")
	public String loginPage(HttpServletRequest request) {
		// 已登录后访问login，则重定向到index
		if (request.getSession().getAttribute("username") != null) {
			return "redirect:index";
		}
		return "login";
	}

	@PostMapping(value = "login")
	public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		int autoLogin = 1;

		if (isVerified(username, password)) {
			request.getSession().setAttribute("username", "admin");
			// 7天内自动登录
			if (autoLogin == 1) {
				Cookie expires = new Cookie("expires", "" + System.currentTimeMillis() + 7 * 24 * 3600 * 1000);
				expires.setMaxAge(7 * 24 * 3600);
				response.addCookie(expires);
			}
			return "redirect:index";
		}
		return "login";
	}

	private boolean isVerified(String username, String password) {
		if ("admin".equals(username) && "admin".equals(password)) {
			return true;
		}
		return false;
	}

}
