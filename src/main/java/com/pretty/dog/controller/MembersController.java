package com.pretty.dog.controller;

import java.util.HashMap;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.pretty.dog.dto.DogDTO;
import com.pretty.dog.service.MembersService;


@Controller
public class MembersController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired MembersService service;
	@Autowired private JavaMailSender mailSender;
	@Autowired HttpSession session;

	
	
	@RequestMapping(value = "/JoinFormshs", method = RequestMethod.GET)
	public String home(Model model) {
		logger.info("회원가입 페이지");
		
		return "JoinFormshs";
	}
	
	
	
	
	String hashText="";
	@RequestMapping(value = "/joinShs", method = RequestMethod.POST)
	public String joinShs(Model model,@RequestParam String id,@RequestParam String pw,
			@RequestParam String name,@RequestParam String phone,@RequestParam String email) {
		logger.info("일반 회원가입 요청");	
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		hashText = encoder.encode(pw);
		logger.info("암호화값 {}",hashText);
		int row = service.joinShs(id,hashText,name,phone,email);
		
		String success ="redirect:/JoinFormshs";
		String msg = "회원가입에 실패하였습니다.";
		model.addAttribute("msg",msg);
		
		if(row>0) {
			success ="redirect:/";
			msg = "회원가입이 완료 되었습니다.";
			model.addAttribute("msg",msg);
		}
		return success;
	}
	
	@RequestMapping(value = "/ShopJoinFormshs", method = RequestMethod.GET)
	public String ShopjoinShs(Model model) {
		logger.info(" 점주 회원가입 페이지");
		
		return "ShopJoinFormshs";
	}
	
	
	@RequestMapping(value = "/ShopjoinShs", method = RequestMethod.POST)
	public String ShopjoinShs(Model model,@RequestParam String id,@RequestParam String pw,
			@RequestParam String name,@RequestParam String phone,@RequestParam String email) {
		logger.info("점주 회원가입 요청");	
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		hashText = encoder.encode(pw);
		logger.info("암호화값 {}",hashText);
		int row = service.ShopjoinShs(id,hashText,name,phone,email);
		String success ="redirect:/ShopJoinFormshs";
		
		String msg = "회원가입에 실패하였습니다.";
		
		if(row>0) {
			model.addAttribute("id",id);
			msg = "매장 회원가입 완료";
			success ="redirect:/ShopInfoFormshs";
		}
		
		model.addAttribute("msg",msg);
		
		return success;
	}
	
	
	
	@RequestMapping(value = "/ShopInfoFormshs", method = RequestMethod.GET)
	public String ShopInfoFormshs(Model model,HttpServletRequest request) {
		logger.info(" 점주 회원가입 정보 페이지");
		String id =request.getParameter("id");

		model.addAttribute("id",id);
		
		return "ShopInfoFormshs";
	}
	
	@RequestMapping(value = "/ShopInfo", method = RequestMethod.POST)
	public String ShopInfo(Model model,MultipartFile shopPhoto,@RequestParam HashMap<String, String> params, @RequestParam String shopSaup) {
		logger.info("점주 매장정보 요청 컨트롤러");	
		logger.info("params : {}",params);
		logger.info("shopPhoto : {}",shopPhoto);
		
		service.ShopInfo(shopPhoto,params,shopSaup);
		
		return "Main";
	}
	
	
	
	
	
	@GetMapping(value="/mailCheck",consumes = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String mailCheck(String email) throws Exception{
//		logger.info("이메일 데이터 전송확인");
//		logger.info("인증 메일 : "+email);
		
		Random random = new Random();
		int checkNum = random.nextInt(888888)+111111; // 111111 - 999999
//		logger.info("인증번호 : "+checkNum);
		
		//이메일 보내기
		String setFrom = "PrettyDog";
		String toEmail = email;
		String title = "PrettyDog 회원가입 인증 이메일 입니다.";
		String content = "PrettyDog 가입해주셔서 감사합니다."+ "<br/><br/>"+"인증 번호는 "+checkNum+" 입니다.<br/>"+
							"해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        String num = Integer.toString(checkNum);
        return num;
	}
	


		//ajax 통신 - 중복 아이디 확인
		@RequestMapping(value = "/overlayid", method = RequestMethod.GET)
		@ResponseBody
		public HashMap<String, Object> overlayid(@RequestParam String id) {		
			logger.info("중복 아이디 체크 : {}",id);		
			return service.overlayShsid(id);
		}	
		
		
		//ajax 통신 - 중복 사업자 확인
		@RequestMapping(value = "/shopSaupCk", method = RequestMethod.GET)
		@ResponseBody
		public HashMap<String, Object> shopSaupCk(@RequestParam String shopSaup) {		
			logger.info("중복 아이디 체크 : {}",shopSaup);		
			return service.shopSaupCk(shopSaup);
		}	



		
		
// 여기까지가 회원가입 기능 		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

		@RequestMapping(value = "/ShopPriFormshs", method = RequestMethod.GET)
		public String ShopPrifo(Model model) {
			logger.info("요금표 폼");	

			return "ShopPriFormshs";
		}
	
	
		
		
		@RequestMapping(value = "/memberPassCk", method = RequestMethod.GET)
		public String memberPassCk(Model model,HttpSession session) {
//			logger.info("비밀번호체크 페이지 컨트롤러");	
			String id = (String) session.getAttribute("loginId");

			String Page ="redirect:/loginPage";
			if(id != null) {
				String loginId = (String) session.getAttribute("loginId");
				model.addAttribute("loginId", loginId);
				model.addAttribute("memInfo",service.memberPassCk(id)); 
				Page ="MemberCkshs";
			}	

			return Page;
			}
		
		
		@RequestMapping(value = "/PassCk", method = RequestMethod.POST)
		public ModelAndView PassCk(Model model,HttpSession session, @RequestParam String pw,@RequestParam String id) {
			//logger.info("비밀번호체크 컨트롤러{}",pw+id);	
			ModelAndView mav = new ModelAndView();
		
				if (service.PassCk(id,pw)) {
					mav.setViewName("redirect:/MyjungboSujungshs");
				}else {

					mav.setViewName("redirect:/memberPassCk");	
				}
			
			return mav;
		}
		
		
		//개인정보 수정페이지
		@RequestMapping(value = "/MyjungboSujungshs", method = RequestMethod.GET)
			public String memberDe(Model model,HttpSession session) {
			
				String id = (String) session.getAttribute("loginId");
				logger.info("세션아이디 값 : {}",id);
				
				DogDTO dto = service.MyjungboSujungshs(id);
				model.addAttribute("info", dto);
				
			return "MyjungboSujungshs";
		}
		
		
		//개인정보 수정요청
		@RequestMapping(value = "/userUp")
			public String userUp(Model model,HttpSession session,@RequestParam HashMap<String, String> params) {
					
				String id = (String) session.getAttribute("loginId");
				logger.info("세션아이디 값 : {}",id);
				logger.info("정보 값 : {}",params);
						
				service.userUp(params);
						
			return "redirect:/MyjungboSujungshs";
		}
		
		
		
		//매장정보 수정페이지
		@RequestMapping(value = "/MyShopInfoshs", method = RequestMethod.GET)
			public String MyjungboSujungshs(Model model,HttpSession session) {
					
				String id = (String) session.getAttribute("loginId");
				logger.info("세션아이디 값 : {}",id);
						
				DogDTO dto = service.MyShopInfoshs(id);
				model.addAttribute("shopinfo", dto);
						
			return "MyShopInfoshs";
		}
		
		//매장정보 수정
		@RequestMapping(value = "/shopUp", method = RequestMethod.POST)
			public String shopup(Model model,HttpSession session, @RequestParam HashMap<String, String> params) {
							
				String id = (String) session.getAttribute("loginId");
				logger.info("세션아이디 값 : {}",params);
								
				service.shopup(params);
								
			return "redirect:/MyShopInfoshs";
				}
				
		
		
		
	//개인 강아지 등록페이지
		@RequestMapping(value = "/MyDogInfoshs", method = RequestMethod.GET)
		public String MyDogInfoshs(Model model,HttpSession session) {
				
			String id = (String) session.getAttribute("loginId");
			logger.info("세션아이디 값 : {}",id);
			DogDTO memInfo = service.MyDogInfoshs(id);
			model.addAttribute("memInfo", memInfo);
			
		return "MyDogInfoshs";
	}		
		
		//개인 강아지 등록페이지
		@RequestMapping(value = "/DogUp", method = RequestMethod.POST)
		public String DogUp(Model model,HttpSession session,@RequestParam String dogname,@RequestParam String dogage
				,@RequestParam String dogweight,@RequestParam String dogchar) {

			String id = (String) session.getAttribute("loginId");
			
			int row= service.DogUp(id,dogname,dogage,dogweight,dogchar); 
			
		return "redirect:/Mydogshs";
	}	
		
		//개인 애견정보 리스트 페이지
		@RequestMapping(value = "/Mydogshs", method = RequestMethod.GET)
		public ModelAndView DogUp(Model model,HttpSession session) {

			String id = (String) session.getAttribute("loginId");
			logger.info("세션아이디 값 : {}",id);
			
		return service.Mydogshs(id);
	}	
		
		
		
		//개인 애견정보 삭제
		@ResponseBody
		@RequestMapping(value = "/DogDel", method = RequestMethod.GET)
		public HashMap<String, Object> DogDel(Model model,HttpSession session,@RequestParam String dogName) {
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			String id = (String) session.getAttribute("loginId");
			logger.info("세션아이디 값 : {}",id+"/"+dogName);
			int delCnt = service.DogDel(id,dogName);
			
			map.put("msg","삭제에 실패하였습니다.");
			if(delCnt>0) {
				map.put("msg", delCnt+" 마리의 등록 정보를 삭제 하였습니다.");
				
			}
		return map;
	}	
		
		//개인 강아지 수정페이지
		@RequestMapping(value = "/MyDogsujungshs")
		public String MyDogsujungshs(Model model,HttpSession session,HttpServletRequest request) {

			String id = (String) session.getAttribute("loginId");
			String dogName = request.getParameter("dogName");
			logger.info("세션아이디 강아지 값 : {}",id+dogName);
			
			DogDTO dto =  service.MyDogsujungshs(id,dogName);
			model.addAttribute("doginfo",dto);
			
		return "MyDogsujungshs";
	}	

		//개인 강아지 수정페이지
		@RequestMapping(value = "/DogSujung")
		public String DogSujung(Model model,HttpSession session,@RequestParam String dogname,@RequestParam String dogage
				,@RequestParam String dogweight,@RequestParam String dogchar) {
			
			String id = (String) session.getAttribute("loginId");
			logger.info("세션아이디 강아지 값 : {}",dogname+dogage+dogweight+dogchar);
			int row = service.DogSujung(id,dogname,dogage,dogweight,dogchar);
			logger.info("수정 성공 여부 {}",row);
			String msg = "수정에 실패 하였습니다.";
			if(row>0) {
				msg = "수정에 성공 하였습니다.";
			}
			model.addAttribute("msg",msg);
					
		return "redirect:/Mydogshs";
			}	
		
		
		//회원탈퇴
		@RequestMapping(value = "/memberOut")
		public String memberOut(HttpSession session) {
//			logger.info("회원탈퇴 컨트롤러");
			
			String id = (String) session.getAttribute("loginId");
			
			if(id != null) {
				session.getAttribute("loginId");
//				logger.info("회원탈퇴 컨트롤러 : {}",object);
				
				service.memberOut(id);
				session.invalidate();
			}
			
			return "redirect:/";
		}


		
	
}


