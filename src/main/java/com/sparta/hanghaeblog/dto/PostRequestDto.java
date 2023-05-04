package com.sparta.hanghaeblog.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    // Post Entity를 생성할 때 필요한 정보들을 담음
    private String title;
    private String contents;
}