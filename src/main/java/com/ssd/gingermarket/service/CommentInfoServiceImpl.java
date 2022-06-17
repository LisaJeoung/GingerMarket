package com.ssd.gingermarket.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssd.gingermarket.domain.CommentInfo;
import com.ssd.gingermarket.dto.CommentDto;
import com.ssd.gingermarket.dto.CommentDto.Request;
import com.ssd.gingermarket.repository.CommentInfoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentInfoServiceImpl implements CommentInfoService {
	
	private final CommentInfoRepository commentInfoRepository;

	@Override
	@Transactional
	public void addComment(CommentDto.Request request, Long authorIdx, Long groupIdx) {
		// user entity
		// User author = userRepository.findById(authorIdx);
		
		// group entity
		// GroupBuying groupBuying = groupBuyingRepository.findById(groupIdx);
		
		CommentInfo comment = CommentInfo.commentBuilder()
				.content(request.getContent())
				.groupIdx(groupIdx)
				.authorIdx(authorIdx)
				.build();
		
		commentInfoRepository.saveAndFlush(comment);
	}
	
	@Override
	@Transactional
	public void addChildComment(Request request, Long authorIdx, Long groupIdx, Long parentIdx) {
		// TODO Auto-generated method stub
		// post entity
				// GroupBuying groupBuyingPost = groupBuyingRepository.findById(dto.getGroupIdx);
				// 부모 댓글의 entity 
				CommentInfo parentComment = commentInfoRepository.findById(parentIdx).orElseThrow(null);
				
				CommentInfo childComment = CommentInfo.commentBuilder()
						.content(request.getContent())
						.parentIdx(parentComment)
						.groupIdx(groupIdx)
						.authorIdx(authorIdx)
						.build();
				
				parentComment.getChildCommentList().add(childComment);
				
				commentInfoRepository.save(childComment);
				return;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentDto.Info> getCommentList(Long groupIdx) {
		// TODO Auto-generated method stub
		
		// post 정보 가져오기
		// GroupBuyingPost groupPostEntity = GroupBuyingRepository.findById(groupIdx);
		List<CommentInfo> commentInfoList = commentInfoRepository.findByGroupIdx(groupIdx);
		
		// int commentCount = groupPostEntity.getCommentList().size();
		
		List<CommentDto.Info> dto = commentInfoList.stream().map(co -> new CommentDto.Info(co.getId()
				, co.getContent()
				, LocalDateTime.now()
				/*, co.getUpdateDate() == null ? co.getCreateDate() : co.getUpdateDate() */
				, co.getUpdateDate()
				, (co.getIsDeleted().equals("N") ? false : true)
				, co.getAuthorIdx(),"프로필", "닉네임"
				, ofList(co.getChildCommentList())))
				.collect(Collectors.toList());

		return dto;
	}

	// 대댓글 entity -> dto
	public List<CommentDto.ChildInfo> ofList (List<CommentInfo> list) {
		if(list.size() == 0) {
			return new ArrayList<CommentDto.ChildInfo>();
		}
		
		return list.stream().map(ch -> new CommentDto.ChildInfo(
				ch.getId(),
				ch.getParentIdx().getId(),
				ch.getContent(),
				LocalDateTime.now(),
				ch.getUpdateDate(),
				(ch.getIsDeleted().equals("N") ? false : true),
				ch.getAuthorIdx(),
				"대댓글 프로필", "대댓글"))
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public void updateComment(CommentDto.Request request, Long commentIdx) {
		// TODO Auto-generated method stub
		CommentInfo comment = commentInfoRepository.findById(commentIdx).orElseThrow(null);
		comment.updateComment(request.getContent());
	}

	@Override
	@Transactional
	public void removeComment(Long commentIdx) {
		// TODO Auto-generated method stub
		CommentInfo comment = commentInfoRepository.findById(commentIdx).orElseThrow(null); 
		comment.removeComment();
	}

}
