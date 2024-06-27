package com.hallym.booker.dto.Login;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginDto {
    private String uid;
    private String pw;
    private String email;
    private Date birth;
}