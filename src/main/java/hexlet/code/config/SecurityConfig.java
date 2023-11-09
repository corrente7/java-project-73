package hexlet.code.config;

import hexlet.code.component.JwtAuthenticationEntryPoint;
import hexlet.code.component.JwtTokenFilter;
import hexlet.code.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/welcome").permitAll()
                .requestMatchers("/api/login", "login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/statuses/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tasks/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tasks").permitAll()
                .requestMatchers(HttpMethod.GET, "/api-docs").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http,
//    HandlerMappingIntrospector introspector) throws Exception {
//        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
//        return http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers(AntPathRequestMatcher.antMatcher("/api")).permitAll()
////                        .requestMatchers(mvcMatcherBuilder.pattern("/api/login")).permitAll()
////                        .requestMatchers(mvcMatcherBuilder.pattern("/api/users")).permitAll()
////                        .requestMatchers(mvcMatcherBuilder.pattern("/api/welcome")).permitAll()
//                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/login")).permitAll()
//                        .requestMatchers(AntPathRequestMatcher.antMatcher("api/users")).permitAll()
//                         .requestMatchers(AntPathRequestMatcher.antMatcher("api/welcome")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern(GET,"/api/statuses/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern(GET,"/api/tasks/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern(GET,"/api/users/**")).permitAll()
//                                .requestMatchers(mvcMatcherBuilder.pattern(POST,"/api/statuses")).authenticated()
//
//
////
//                        .anyRequest().authenticated())
////
//                .sessionManagement(session -> session.sessionCreationPolicy
//                (SessionCreationPolicy.STATELESS))
//                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint
//                (jwtAuthenticationEntryPoint)
//                )
//                .addFilterBefore(
//                jwtTokenFilter,
//                UsernamePasswordAuthenticationFilter.class)
//                .build();
//         }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
//            throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject
//        (AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        return authenticationManagerBuilder.build();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

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

}




