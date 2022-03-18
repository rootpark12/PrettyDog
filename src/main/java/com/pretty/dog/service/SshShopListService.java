package com.pretty.dog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pretty.dog.dao.SshShopListDAO;

@Service
public class SshShopListService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired SshShopListDAO sshShopListDAO;

	public ArrayList<HashMap<String,Object>> sshShopList() {
		System.out.println("sshShopList 서비스 이동");
		return sshShopListDAO.sshShopList();
	}

	public ArrayList<HashMap<String, Object>> sshShopDetail(String idx) {
		System.out.println("sshShopDetail 서비스 이동");
		System.out.println("매장번호" + idx);
		return sshShopListDAO.sshShopDetail(idx);
	}

	public ArrayList<HashMap<String, Object>> sshShopQnaList(String idx) {
		System.out.println("sshShopQnaList 서비스 이동");
		System.out.println("매장 번호" + idx);
		return sshShopListDAO.sshShopQnaList(idx);
	}

	public ArrayList<HashMap<String, Object>> sshShopQnaIdChk(String memId) {
		System.out.println("sshShopQnaIdChk 서비스 이동");
		System.out.println("로그인 아이디 : " + memId);
		return sshShopListDAO.sshShopQnaIdChk(memId);
	}

	public List<HashMap<String, Object>> qnaComChk(List<String> a) {
		System.out.println("qnaComChk 서비스 이동");
		System.out.println("넘어온 아이디 값들 : " + a);
		return sshShopListDAO.qnaComChk(a);
	}

}
































































































































































