package com.example.alumniassocaition1.security;

import com.example.alumniassocaition1.service.UserService;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> upstream/main
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

<<<<<<< HEAD
/**
 * Central Spring Security configuration.
 *
 * <p>Configures stateless JWT authentication, CORS policies, public/protected
 * endpoints, password encoding, and static-resource serving for uploaded files.</p>
 */
=======
>>>>>>> upstream/main
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

<<<<<<< HEAD
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
=======
    @Autowired
    private UserService customUserDetailsService;
>>>>>>> upstream/main

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

<<<<<<< HEAD
    public SecurityConfig(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userService);
=======
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
>>>>>>> upstream/main
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
<<<<<<< HEAD
        configuration.setAllowedOrigins(List.of(
                "https://alumni-association-platform-git-main-sujeet-s-projects-351108b0.vercel.app",
                "http://localhost:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Cache-Control", "Content-Type",
                "X-Requested-With", "Accept", "Origin"
        ));
=======
        // 👇 MODIFY THIS LINE
        configuration.setAllowedOrigins(List.of(
                "https://alumni-association-platform-git-main-sujeet-s-projects-351108b0.vercel.app", // Your Vercel URL
                "http://localhost:5173" // Keep your local frontend URL for development if needed
                // Add other origins like your Vercel production domain if you have one
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "Accept", "Origin"));
>>>>>>> upstream/main
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
<<<<<<< HEAD
        source.registerCorsConfiguration("/**", configuration);
=======
        source.registerCorsConfiguration("/**", configuration); // This applies CORS to all paths
>>>>>>> upstream/main
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/colleges/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/uploads/**", "/api/events/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events", "/api/events/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String resolvedUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
                if (!resolvedUploadDir.endsWith(java.io.File.separator)) {
                    resolvedUploadDir += java.io.File.separator;
                }

<<<<<<< HEAD
                // Serve uploaded files (images) for posts and events
=======
                // Serve uploaded images for posts and events from the same directory
>>>>>>> upstream/main
                registry.addResourceHandler("/api/posts/uploads/**", "/api/events/uploads/**")
                        .addResourceLocations("file:" + resolvedUploadDir);
            }
        };
    }
}
