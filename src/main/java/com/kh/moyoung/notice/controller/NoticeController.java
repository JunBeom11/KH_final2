package com.kh.moyoung.notice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.kh.moyoung.common.util.PageInfo;
import com.kh.moyoung.member.model.vo.Member;
import com.kh.moyoung.notice.model.service.NoticeService;
import com.kh.moyoung.notice.model.vo.Notice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	private NoticeService service;
	
	@GetMapping("/list")
	public ModelAndView list(ModelAndView model,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		
		List<Notice> list = null;

		PageInfo pageInfo = new PageInfo(page, 10, service.getNoticeCount(), 10);

		list = service.getNoticeList(pageInfo);

		model.addObject("list", list);
		model.addObject("pageInfo", pageInfo);
		model.setViewName("notice/list");

		return model;
	}
	
	@GetMapping("/view")
	public ModelAndView view(ModelAndView model,
			@RequestParam("no") int noticeNo) {
		
		Notice notice = service.findByNo(noticeNo);
		
		model.addObject("notice",notice);
		model.setViewName("notice/view");
		
		return model;
	}

	@GetMapping("/update")
	public ModelAndView updateView(ModelAndView model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam("no") int noticeNo) {
		
		Notice notice = service.findByNo(noticeNo);
		
		if (loginMember.getU_no() == notice.getWriterNo()) {
			model.addObject("notice", notice);
			model.setViewName("notice/update");
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/notice/list");
			model.setViewName("common/msg");
		}		
		return model;
	}
	
	@PostMapping("/update")
	public ModelAndView update(ModelAndView model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			HttpServletRequest request,
			@ModelAttribute Notice notice) {
		
		int result = 0;
		
		if(loginMember.getU_id().equals(notice.getWriterId())) {		
			
			result = service.save(notice);
			
			if(result > 0) {
				model.addObject("msg", "게시글이 정상적으로 수정되었습니다.");
				model.addObject("location", "/notice/view?no=" + notice.getNo());
			} else {
				model.addObject("msg", "게시글 수정을 실패하였습니다.");
				model.addObject("location", "/notice/update?no=" + notice.getNo());
			}
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/notice/list");
		}
		
		model.setViewName("common/msg");
		
		return model;
	}
	
	@GetMapping("/write")
	public void wirteView() {
		log.info("게시글 작성 페이지 요청");
	}

	@PostMapping("/write")
	public ModelAndView write(ModelAndView model, HttpServletRequest request,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute Notice notice) {
		int result = 0;

		log.info("게시글 작성 요청");

		
		if (loginMember.getU_id().equals(notice.getWriterId())) {
			notice.setWriterNo(loginMember.getU_no());

			result = service.save(notice);

			if (result > 0) {
				model.addObject("msg", "게시글이 정상적으로 등록되었습니다.");
				model.addObject("location", "/");
			} else {
				model.addObject("msg", "게시글 등록을 실패하였습니다.");
				model.addObject("location", "/");
			}
		} else {
			model.addObject("msg", "잘못된 접근입니다");
			model.addObject("location", "/");
		}

		model.setViewName("common/msg");

		return model;
	}

}
