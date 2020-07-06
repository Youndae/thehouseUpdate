package com.bit.house.config;

import com.bit.house.security.CustomLoginSuccessHandler;
import com.bit.house.security.CustomOAuth2Provider;
import com.bit.house.security.CustomOAuth2UserService;
import com.bit.house.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService customUserService() {
        return new CustomUserDetailsService();
    }

    // in custom userdetails
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }


    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/resources/**", "/css/**", "/img/**", "/js/**", "/images/**", "/plugins/**");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        httpSecurity
                    .authorizeRequests()
                    .antMatchers("/", "/oauth2/**", "/login/**", "/storeMain", "/resources/**", "/css/**", "/img/**", "/js/**", "/images/**", "/plugins/**")
                    .permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN") // 괄호의 권한을 가진 유저만 접근가능, ROLE_가 붙어서 적용 됨. 즉, 테이블에 ROLE_권한명 으로 저장해야 함.
                    .antMatchers("/member/**").hasAnyRole("MEMBER","USER")
                    .antMatchers("/seller/**").hasAnyRole("SELLER")
                    //.anyRequest().authenticated()
                .and()
                    .oauth2Login()
                    .userInfoEndpoint().userService(new CustomOAuth2UserService()) // 네이버 USER INFO의 응답을 처리하기 위한 설정
                .and()
                    .defaultSuccessUrl("/loginSuccess")
                    .failureUrl("/loginFailure")
                .and()
                    .headers()
                    .frameOptions()
                    .disable()
                .and()
                    .formLogin()
                    .loginPage("/customLogin")
                    .loginProcessingUrl("/login")
                    .successHandler(loginSuccessHandler())
                .and()
                    .logout()
                    .permitAll()
                    .logoutUrl("/customLogout") // 로그아웃 url
                    .invalidateHttpSession(true) // 로그아웃시 세션제거
                    .deleteCookies("JSESSION_ID") // 쿠키제거
                    .clearAuthentication(true) // 권한정보제거

                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/customLogin"))
                .and()
                    .addFilterBefore(filter, CsrfFilter.class)
                    .csrf().disable();

    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties,
            @Value("${custom.oauth2.kakao.client-id}") String kakaoClientId,
            @Value("${custom.oauth2.kakao.client-secret}") String kakaoClientSecret,
            @Value("${custom.oauth2.naver.client-id}") String naverClientId,
            @Value("${custom.oauth2.naver.client-secret}") String naverClientSecret) {
        List<ClientRegistration> registrations = oAuth2ClientProperties
                .getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                    .clientId(kakaoClientId)
                    .clientSecret(kakaoClientSecret)
                    .jwkSetUri("temp")
                    .build());

        registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver")
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .jwkSetUri("temp")
                .build());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }

//        if("facebook".equals(client)) {
//            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
//            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
//                    .clientId(registration.getClientId())
//                    .clientSecret(registration.getClientSecret())
//                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
//                    .scope("email")
//                    .build();
//        }

        return null;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
