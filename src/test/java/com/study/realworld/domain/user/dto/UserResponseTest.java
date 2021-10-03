package com.study.realworld.domain.user.dto;

import com.study.realworld.domain.auth.dto.ResponseToken;
import com.study.realworld.domain.auth.dto.token.AccessToken;
import com.study.realworld.domain.auth.dto.token.RefreshToken;
import com.study.realworld.domain.user.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.study.realworld.domain.user.domain.BioTest.BIO;
import static com.study.realworld.domain.user.domain.EmailTest.EMAIL;
import static com.study.realworld.domain.user.domain.ImageTest.IMAGE;
import static com.study.realworld.domain.user.domain.NameTest.USERNAME;
import static com.study.realworld.domain.user.domain.PasswordTest.PASSWORD;
import static com.study.realworld.domain.user.domain.UserTest.userBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserResponseTest {

    @DisplayName("UserJoinResponse 인스턴스 기본 생성자 테스트")
    @Test
    void default_constructor_test() {
        final UserResponse userResponse = new UserResponse();

        assertAll(
                () -> assertThat(userResponse).isNotNull(),
                () -> assertThat(userResponse).isExactlyInstanceOf(UserResponse.class)
        );
    }

    @DisplayName("UserJoinResponse 인스턴스 fromUser() 테스트")
    @Test
    void fromUser_test() {
        final User user = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE));
        final ResponseToken responseToken = new ResponseToken(new AccessToken("accessToken"), new RefreshToken("responseToken"));
        final UserResponse userResponse = UserResponse.fromUserWithToken(user, responseToken);

        assertAll(
                () -> assertThat(userResponse).isNotNull(),
                () -> assertThat(userResponse).isExactlyInstanceOf(UserResponse.class)
        );
    }

}