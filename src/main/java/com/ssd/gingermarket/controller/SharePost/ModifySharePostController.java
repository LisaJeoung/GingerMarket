package com.ssd.gingermarket.controller.SharePost;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ssd.gingermarket.dto.SharePostDto;
import com.ssd.gingermarket.dto.SharePostDto.Request;
import com.ssd.gingermarket.service.SharePostService;

import lombok.RequiredArgsConstructor;

//@Slf4j //로그 
@RestController 
@RequestMapping("/share")
@RequiredArgsConstructor
public class ModifySharePostController {
	
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
	
	
	@GetMapping("/updateForm")
	public ModelAndView goUpdateForm(@RequestParam(required=true) Long postIdx) { 
		Long userIdx = (long) 1;//user session으로 추후 수정 
		
		ModelAndView mav = new ModelAndView("content/sharePost/sharePost_update");
		
		SharePostDto.viewResponse viewRes = sharePostService.getPost(postIdx);
		
		SharePostDto.Request req = new SharePostDto.Request();
		req.setTitle(viewRes.getTitle());
		req.setCategory(viewRes.getCategory());
		req.setDescr(viewRes.getDescr());
		req.setImageIdx(viewRes.getImageIdx());
		req.setAuthorIdx(viewRes.getAuthorIdx());
		
		mav.addObject("updateReq", req);
		mav.addObject("postInfo", viewRes);
		
		return mav;
	}
	
	@PutMapping("")
    public RedirectView updatePost(SharePostDto.Request post, 
    								@RequestParam(required=true) Long postIdx,
    								@RequestParam(required=false) String category) 
	{
		Long authorIdx = (long) 1; //session구현 후 변경
		
		post.setCategory(category);
		post.setAuthorIdx(authorIdx);
		sharePostService.modifyPost(postIdx, post);
		
        return new RedirectView("/share/posts");
    }
	
	
}
