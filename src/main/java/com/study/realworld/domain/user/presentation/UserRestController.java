package com.study.realworld.domain.user.presentation;

import com.study.realworld.domain.auth.application.JwtUserDetailsService;
import com.study.realworld.domain.auth.dto.ResponseToken;
import com.study.realworld.global.config.security.jwt.JwtAuthentication;
import com.study.realworld.domain.auth.infrastructure.TokenProvider;
import com.study.realworld.domain.user.application.UserJoinService;
import com.study.realworld.domain.user.domain.Email;
import com.study.realworld.domain.user.domain.User;
import com.study.realworld.domain.user.dto.UserJoinRequest;
import com.study.realworld.domain.user.dto.UserResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class UserRestController {

    private static final String JOIN_REQUEST_LOG_MESSAGE = "join request: {}";

    private final Logger log = getLogger(getClass()); // 유틸성이라 개행

    private final UserJoinService userJoinService;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final TokenProvider tokenProvider;

    public UserRestController(final UserJoinService userJoinService,
                              final JwtUserDetailsService jwtUserDetailsService,
                              final TokenProvider tokenProvider) {
        this.userJoinService = userJoinService;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> join(@Valid @RequestBody final UserJoinRequest userJoinRequest) {
        log.info(JOIN_REQUEST_LOG_MESSAGE, userJoinRequest.toString());
        final User user = userJoinService.join(userJoinRequest.toUser());
        final ResponseToken responseToken = responseToken(user);
        final UserResponse userResponse = UserResponse.fromUserWithToken(user, responseToken);
        return ResponseEntity.ok().body(userResponse);
    }

    private ResponseToken responseToken(final User user) {
        final Email securityUsername = user.email();
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(securityUsername.email());
        final JwtAuthentication jwtAuthentication = JwtAuthentication.ofUserDetails(userDetails);
        return tokenProvider.createToken(jwtAuthentication);
    }

}
