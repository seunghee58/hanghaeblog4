package com.sparta.hanghaeblog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    // 회원가입 요청에서 필요한 정보를 담는 DTO
    private String username;
    private String password;
    private boolean admin = false; // 관리자인지 아닌지를 확인
    private String adminToken = "";
}