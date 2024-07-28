package com.example.somserver.jwt;

import com.example.somserver.dto.CustomUserDetails;
import com.example.somserver.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//요청에 대해서 한번만 동작하는 OncePerRequestFilter
public class JWTFilter extends OncePerRequestFilter {

    //JWTUtil 주입 받기
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //토큰을 검증하는 여러 구현

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //***Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");

            //체인방식으로 엮여있는 필터들이 있는데 이 필터를 종료하고 이 필터에서 받은 request, response 를 다음 필터로 넘겨줌
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //***토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");

            //현재 필터를 종료하고, 요청과 응답 객체를 다음 필터로 전달
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //---> 토큰이 있고 아직 소멸시간도 지나지 않았음

        //토큰에서 userId, role 획득
        String userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(userId);
        userDTO.setPassword("temPW");
        userDTO.setNickname("temNickName");
        userDTO.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록 / 유저 세션 생성 -> 특정한 경로에 접근 가능
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //메서드가 종료되었기 때문에 filterChain을 통해 그다음 필터에 request, response 넘겨줌
        filterChain.doFilter(request, response);
    }
}
