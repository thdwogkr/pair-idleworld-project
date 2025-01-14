package com.pado.idleworld.security;

import com.pado.idleworld.security.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  //secured 어노테이션 활성화(TestController.info()), preAuthorize, postAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    private final PrincipalOauth2UserService principalOauth2UserService;

    private final AuthenticationFailureHandler authenticationFailureHandler;    //로그인 실패 인터페이스


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.csrf().disable() // todo: disable 하지말고 postman에서 토큰 처리하는 법 확인하기
                .authorizeRequests()
                // 그냥 허용할 응답들
                .mvcMatchers("/**", "/index", "/login", "/sign-up", "/operation", "/operation/history", "/api/sign-up")
                .permitAll();
                //.anyRequest().authenticated(); // 나머지는 로그인을 해야 쓸 수 있다.
*/

        http.csrf().disable()//.authorizeRequests()
                //.antMatchers("/login", "/join", "/v1/sign-up","/change-password","/v1/sendEmail/**","/v1/account/login").permitAll()
                //.antMatchers("/v1/account/{accountEmail}").access("hasRole('ROLE_ADMIN')")
                //.antMatchers("/admin").access("hasRole('ADMIN')")
                //.anyRequest().authenticated()
                //.anyRequest().access("hasRole('ROLE_ADMIN')")
                //.and()
                .formLogin()
                .loginPage("/login")    //접근권한 없으면 해당 url로 가는듯?
                .usernameParameter("email")
                .loginProcessingUrl("/v1/account/login")   //왼쪽 url 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행
                .defaultSuccessUrl("/v1/loginProc",true) //login 성공하면 가는 url (단, 최초에 다른페이지로 접속을 시도했으면 거기로 이동), true 하면 최초 다른 페이지 접속 했어도 여기로 접속
                .permitAll()
                .failureHandler(authenticationFailureHandler)   //로그인 실패시
                .and()
                .logout()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);


    }



    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/h2-console/**");
    }

    //실패처리를 위한 핸들러



}