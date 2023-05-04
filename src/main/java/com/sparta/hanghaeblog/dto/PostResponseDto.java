package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class PostResponseDto { // 게시물 조회 요청에 대한 응답으로 사용되는 DTO
    private long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = post.getComment()
                .stream() // stream은 요소들의 연속적인 흐름을 나타냄
                .map(CommentResponseDto::new) // map() 메서드를 통하여 스트림의 요소들을 CommentResponseDto 객체로 변환, CommentResponseDto::new는 CommentResponseDto 클래스의 생성자를 참조하는 메서드 레퍼런스
                .collect(Collectors.toList()); // Collectors.toList()를 이용하여 스트림의 요소들을 리스트로 수집, 이를 통해 CommentResponseDto 객체로 변환된 댓글들이 리스트로 저장
    }
}