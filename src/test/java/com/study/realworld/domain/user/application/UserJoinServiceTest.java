package com.study.realworld.domain.user.application;

import com.study.realworld.domain.user.domain.Email;
import com.study.realworld.domain.user.domain.User;
import com.study.realworld.domain.user.domain.UserRepository;
import com.study.realworld.domain.user.domain.UserTest;
import com.study.realworld.domain.user.dto.UserJoinRequest;
import com.study.realworld.domain.user.dto.UserJoinRequestTest;
import com.study.realworld.domain.user.dto.UserJoinResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.study.realworld.domain.user.domain.EmailTest.EMAIL;
import static com.study.realworld.domain.user.domain.UserTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserJoinServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserJoinService userJoinService;

    @DisplayName("UserJoinService 인스턴스 생성자 테스트")
    @Test
    void constructor_test() {
        final UserJoinService userJoinService = new UserJoinService(userRepository);

        assertAll(
                () -> assertThat(userJoinService).isNotNull(),
                () -> assertThat(userJoinService).isExactlyInstanceOf(UserJoinService.class)
        );
    }

    @DisplayName("UserJoinService 인스턴스 join() 테스트")
    @Test
    void join_test() {
        final User user = UserTest.userBuilder(new Email(EMAIL), USERNAME, PASSWORD, BIO, IMAGE);
        given(userRepository.save(any())).willReturn(user);

        final UserJoinRequest userJoinRequest = UserJoinRequestTest.userJoinRequest(USERNAME, new Email(EMAIL), PASSWORD);
        final UserJoinResponse userJoinResponse = userJoinService.join(userJoinRequest);

        then(userRepository).should(times(1)).save(any());
        assertAll(
                () -> assertThat(userJoinResponse).isNotNull(),
                () -> assertThat(userJoinResponse).isExactlyInstanceOf(UserJoinResponse.class)
        );
    }

}