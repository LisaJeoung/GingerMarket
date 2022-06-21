package com.ssd.gingermarket.controller.SharePost;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ssd.gingermarket.dto.SharePostDto;
import com.ssd.gingermarket.dto.SharePostDto.Request;
import com.ssd.gingermarket.service.ImageService;
import com.ssd.gingermarket.service.SharePostService;

import lombok.RequiredArgsConstructor;

//@Slf4j //로그 
@RestController 
@RequestMapping("/share-posts")
@RequiredArgsConstructor
public class ViewSharePostController {
	private final SharePostService sharePostService;

	@ModelAttribute("categoryList")
	public List<String> categoryList(){
		List<String> category = new ArrayList<>();
		category.add("가전제품");
		category.add("욕실용품");
		category.add("위생용품");
		category.add("주방용품");
		category.add("바디/헤어");
		category.add("청소/세탁용품");
		category.add("문구");
		category.add("생활잡화");

		return category;
	}
	
	/**
     * 게시글 상세 조회
     */
	@GetMapping("/{postIdx}")
	public ModelAndView getPost(@PathVariable Long postIdx){
		Long sessionIdx = (long)2;
		ModelAndView mav = new ModelAndView("content/sharePost/sharePost_view");
		mav.addObject("postInfo", sharePostService.getPost(postIdx));
		mav.addObject("senderIdx", sessionIdx);
		mav.addObject("sessionIdx", sessionIdx);
		
		mav.addObject("userIdx", sessionIdx); //세션 
		return mav;
	}
	
	/**
     * 게시글 리스트 조회
     */
	@GetMapping("")
	public ModelAndView getPostList(@RequestParam(value="page", defaultValue="0") int page) {
		
		Long userIdx = (long) 2;
		
		ModelAndView mav = new ModelAndView("content/sharePost/sharePostList");
		mav.addObject("allPostList", sharePostService.getAllPost(page));
		mav.addObject("favPostList", sharePostService.getFavPost(userIdx));
		
		mav.addObject("userIdx", 2); //user session 구현 후 수정 예정 
		mav.addObject("type", "none");
		
		return mav;
	}
	
	//검색
	@GetMapping(value="/search/{option}")
	public ModelAndView getSearchList(@PathVariable("option") String option,
								@RequestParam String keyword, 
								@RequestParam(value="page", defaultValue="0") int page, 
								@RequestParam(value="type") String type) {
			
		ModelAndView mav = new ModelAndView("content/sharePost/sharePostList");
		
		mav.addObject("allPostList", sharePostService.getAllPostByKeyword(keyword, page, option, type));
		mav.addObject("type", type);
		mav.addObject("keyword", keyword);
		
		mav.addObject("userIdx", 1);
			
		return mav;
	}
	
	
}
