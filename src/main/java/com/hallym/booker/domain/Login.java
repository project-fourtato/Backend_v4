package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
public class Login {

    @Id
    private String loginUid;
    private String pw;
    private String email;
    @Temporal(TemporalType.DATE)
    private Date birth;

    @OneToOne(mappedBy = "login", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile; //Login이 Profile의 부모이므로, 나중에 회원 삭제 시 login 객체를 삭제하면 된다.

    // 생성자
    protected Login() {}

    private Login(String loginUid, String pw, String email, Date birth) {
        this.loginUid = loginUid;
        this.pw = pw;
        this.email = email;
        this.birth = birth;
    }

    // 수정 메서드
    public Login change(String pw, String email, Date birth) {
        this.pw = pw;
        this.email = email;
        this.birth = birth;

        return this;
    }

    // 생성 메서드
    public static Login create(String loginUid, String pw, String email, Date birth){
        Login login = new Login(loginUid, pw, email, birth);
        return login;
    }

    protected void setProfile(Profile profile) { //1대1의 경우도 한 쪽에서만 연관관계편의메서드를 만들면 됨. 그것을 위한 setter
        this.profile = profile;
    }
}
