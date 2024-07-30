package com.example.somserver.jwt;

import com.example.somserver.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    //AuthenticationManager 생성자 방식으로 주입 받기 //JWTUtil 주입 받기
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 userId, password 추출
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        //스프링 시큐리티에서 userId와 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        //이 AuthenticationManager가 검증을 담당함
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드(여기서 jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //유저 객체를 알아내기 위해
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //userId, role 값 authentication 객체에서 뽑아내기
        String userId = customUserDetails.getUsername(); //이 CustomUserDetails에서 userId을 뽑아냄

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority(); //role 뽑아냄

        //jwt 생성
        String token = jwtUtil.createJwt(userId, role, 60*60*1000L); //60분

        //response 헤더에 JWT 토큰, userId 추가
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("userId", userId);

        //응답 상태 코드를 200 OK로 설정
        response.setStatus(200);

        System.out.println("success login");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);

        System.out.println("fail login");
    }
}
