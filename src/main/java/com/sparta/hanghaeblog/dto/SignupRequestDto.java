package com.sparta.hanghaeblog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {
    // 회원가입 요청에서 필요한 정보를 담는 DTO

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[a-z0-9]{4,10}$",message = "형식에 맞지 않는 아이디입니다.")
    private String username;
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{8,15}$",message = "형식에 맞지 않는 비밀번호입니다.")
    private String password;
    private boolean admin = false; // 관리자인지 아닌지를 확인
    private String adminToken = "";
}