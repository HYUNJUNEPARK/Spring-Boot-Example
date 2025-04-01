package com.mysite.sbb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
   스프링 시큐리티: 보안 강화, 사용자 인증 및 권한 부여, 외부 공격으로부터 시스템을 보호흐는 역할
 */
@Configuration //스프링의 환경 설정파일
@EnableWebSecurity //모든 요청 URL이 스프링의 시큐리티의 제어를 받도록
public class SecurityConfig {
    @Bean //스프링에 의해 생성 또는 관리되는 객체(e.g., 컨트롤러, 서비스, 리포지터리), 자바 코드 내에서 별도로 빈을 정의하고 등록할 수 있다.
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                /*
                    h2-console/로 시작하는 모든 URL은 CSRF 검증을 하지 않는다는 설정
                 */
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                /*
                    스프링 시큐리티는 'X-Frame-Options:DENY' 를 통해 웹페이지가 다른 프레임에 들어가는 것을 기본적으로 방지(클릭잭킹 공격을 막기 위한 용도)
                    URL 요청 시 X-Frame-Options 헤더를 DENY 대신 SAMEORIGIN 으로 설정
                 */
                .headers((headers) -> headers //
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                /*
                    formLogin: 로그인 설정 담당
                 */
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") //로그인 페이지 URL
                        .defaultSuccessUrl("/")) //성공 시 이동할 페이지
        ;
        return http.build();
    }

    /*
        비크립트는 해시 함수의 하나로 주로 비밀번호롸 같은 보안 정보를 안정하게 저장하고 검증할 때 사용하는 암호화 기술
        new BCryptPasswordEncoder(); 보다는 빈으로 등록해서 사용하는 것이 좋다.
        그 이유는 암호화 방식을 변경하면 BCryptPasswordEncoder 를 사용한 모든 프로그램을 일일이 찾아다니며 수정해야하기 때문
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
