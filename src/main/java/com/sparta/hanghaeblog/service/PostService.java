package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.ApiResult;
import com.sparta.hanghaeblog.dto.CommentResponseDto;
import com.sparta.hanghaeblog.dto.PostRequestDto;
import com.sparta.hanghaeblog.dto.PostResponseDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.PostRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository; // 데이터베이스와 연결
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // Post 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크 추가
        User user = checkToken(request);

        if (user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Post post = new Post(requestDto, user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    // 전체 Post 조회
    @Transactional
    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDto = new ArrayList<>();

        for (Post post : posts) {
            postResponseDto.add(new PostResponseDto(post));
        }

        return postResponseDto;
    }

    // 선택 Post 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 일치하지 않습니다."));
        return new PostResponseDto(post);
    }

    /// Post 수정
    @Transactional
    public PostResponseDto updatepost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크 추가
        User user = checkToken(request);

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        if (user == null) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        if (post.getUser().equals(user) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            post.update(requestDto);
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        return new PostResponseDto(post);
    }





    // Post 삭제
    @Transactional
    public ApiResult deletePost (Long id, HttpServletRequest request) {

        // 토큰 체크 추가
        User user = checkToken(request);

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        if (user == null) {
            return new ApiResult("작성자만 삭제할 수 있습니다.", 400);
        }

        if (post.getUser().equals(user) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            postRepository.delete(post);
        } else {
            return new ApiResult("작성자만 삭제할 수 있습니다.", 400);
        }

        return new ApiResult("삭제 성공", 200);
    }


    // Token 체크
    public User checkToken(HttpServletRequest request){

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return null;
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }

}