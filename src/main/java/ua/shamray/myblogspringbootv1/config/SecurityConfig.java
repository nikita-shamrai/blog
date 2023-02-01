package ua.shamray.myblogspringbootv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private static final String[] WHITELIST = {
            "/blog/posts",
            "/blog/register",
            "/blog/login" //УДАЛИТЬ?
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(WHITELIST).permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/blog/posts/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    try {
                        //TODO Разобраться с ЛогинПейдж чтоб была дефолтная
                        login
                                .loginPage("/blog/login")
                                .loginProcessingUrl("/blog/login-process")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/blog/posts", true)
                                .failureUrl("/blog/login?error")
                                .permitAll()
                                .and()
                                .logout()
                                .logoutUrl("/blog/logout")
                                .logoutSuccessUrl("/blog/login?logout")
                                .permitAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                })
                .httpBasic()
                .and()
                .build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
