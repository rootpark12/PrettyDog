package com.pretty.dog.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pretty.dog.service.PointManageService;

@Controller
public class PointManagementController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired PointManageService service;
	
	@RequestMapping(value = "/PointInsertPage")
	public String PointInsertPage(Model model,HttpSession session) {
		logger.info("포인트충전페이지");
		
		//String loginId = (String) session.getAttribute("loginId");
		String loginId = "dud";
		model.addAttribute("loginId", loginId);
		return "cywPointInsert";
	}
	
	@RequestMapping(value = "/pointInsert")
	public ModelAndView PointInsert(@RequestParam String point,HttpSession session,RedirectAttributes rArrt) {
		logger.info("포인트충전");
		//String loginId = (String) session.getAttribute("loginId");
		String loginId = "dud";
		return service.pointInsert(point,loginId,rArrt);
	}
	
	
	@RequestMapping(value = "/pointListPage")
	public ModelAndView pointList(Model model,HttpSession session) {
		logger.info("포인트리스트페이지");
		
		//String loginId = (String) session.getAttribute("loginId");
		String loginId = "dud";
		
		return service.pointListPage(loginId);
	}
	
	
	 @RequestMapping(value = "/memPointList", method = RequestMethod.POST)
	 @ResponseBody public HashMap<String, Object> memPointList(Model model,@RequestParam String page,@RequestParam String cnt,HttpSession session){
	 
		// String loginId = (String) session.getAttribute("loginId");
		 String loginId = "dud";
		 
		 int currPage = Integer.parseInt(page); 
		 int pagePerCnt =Integer.parseInt(cnt);
		 
	 return service.memPointList(currPage,pagePerCnt,loginId); 
	 }
	
	
	
	
	
}