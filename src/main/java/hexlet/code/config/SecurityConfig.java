package hexlet.code.config;

import hexlet.code.component.JwtAuthenticationEntryPoint;
import hexlet.code.component.JwtTokenFilter;
import hexlet.code.model.UserRole;
import hexlet.code.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig  {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(mvcMatcherBuilder.pattern("/api")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/users")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/welcome")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern(GET,"/api/statuses/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern(GET,"/api/tasks/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern(GET,"/api/users/**")).permitAll()


//                        .requestMatchers(mvcMatcherBuilder.pattern(POST,"/api/tasks")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern(POST,"/api/statuses")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//
//
                        //.requestMatchers(mvcMatcherBuilder.pattern(PATCH,"/api/users/**")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern(PATCH,"/api/statuses/**")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern(PATCH,"/api/tasks/**")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//
//                        .requestMatchers(mvcMatcherBuilder.pattern(DELETE,"/api/users/**")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern(DELETE,"/api/statuses/**")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
//                        .requestMatchers(mvcMatcherBuilder.pattern(DELETE,"/api/tasks/**")).hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())


                        .anyRequest().authenticated())
//
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
//            throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        return authenticationManagerBuilder.build();
//    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}



