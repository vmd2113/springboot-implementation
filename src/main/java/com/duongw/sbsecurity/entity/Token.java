package com.duongw.sbsecurity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens") // Tên bảng
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "is_logout")
    private boolean isLogout;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String accessToken, String refreshToken,String resetPasswordToken, boolean isLogout, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.resetPasswordToken = resetPasswordToken;
        this.isLogout = isLogout;
        this.user = user;
    }




}
