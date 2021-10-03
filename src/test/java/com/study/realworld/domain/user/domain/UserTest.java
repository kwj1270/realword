package com.study.realworld.domain.user.domain;

import com.study.realworld.domain.follow.domain.Follow;
import com.study.realworld.domain.user.error.exception.PasswordMissMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.study.realworld.domain.follow.domain.FollowTest.followBuilder;
import static com.study.realworld.domain.user.domain.BioTest.BIO;
import static com.study.realworld.domain.user.domain.EmailTest.EMAIL;
import static com.study.realworld.domain.user.domain.ImageTest.IMAGE;
import static com.study.realworld.domain.user.domain.NameTest.USERNAME;
import static com.study.realworld.domain.user.domain.PasswordTest.PASSWORD;
import static com.study.realworld.domain.user.domain.PasswordTest.PASSWORD_ENCODER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTest {

    @DisplayName("User 인스턴스 기본 생성자 테스트")
    @Test
    void default_constructor_test() {
        final User user = new User();

        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user).isExactlyInstanceOf(User.class)
        );
    }

    @DisplayName("User 인스턴스 빌더 테스트")
    @Test
    void builder_test() {
        final User user = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE));

        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user).isExactlyInstanceOf(User.class)
        );
    }

    @DisplayName("User 인스턴스 getter() 테스트")
    @Test
    void getter_test() {
        final Long id = 1L;
        final User user = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE));
        ReflectionTestUtils.setField(user, "id", id);

        assertAll(
                () -> assertThat(user.id()).isEqualTo(id),
                () -> assertThat(user.email().email()).isEqualTo(EMAIL),
                () -> assertThat(user.username().name()).isEqualTo(USERNAME),
                () -> assertThat(user.password().password()).isEqualTo(PASSWORD),
                () -> assertThat(user.bio().bio()).isEqualTo(BIO),
                () -> assertThat(user.image().path()).isEqualTo(IMAGE)
        );
    }

    @DisplayName("User 인스턴스 passwordMatches() 테스트")
    @Test
    void passwordMatches_test() {
        final Password invalidPassword = new Password("INVALID_PASSWORD");
        final User user = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE)).encode(PASSWORD_ENCODER);

        assertThatThrownBy(() -> user.login(invalidPassword, PASSWORD_ENCODER))
                .isInstanceOf(PasswordMissMatchException.class)
                .hasMessage("패스워드가 일치하지 않습니다.");
    }

    @DisplayName("User 인스턴스 change() 테스트")
    @Test
    void change_test() {
        final Email updateEmail = new Email("updateEmail");
        final Bio updateBio = new Bio("updateBio");
        final Image updateImage = new Image("updateImage");
        final User user = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE)).encode(PASSWORD_ENCODER);

        user.changeEmail(updateEmail).changeBio(updateBio).changeImage(updateImage);

        assertAll(
                () -> assertThat(user.email()).isEqualTo(updateEmail),
                () -> assertThat(user.bio()).isEqualTo(updateBio),
                () -> assertThat(user.image()).isEqualTo(updateImage)
        );
    }

    @DisplayName("User 인스턴스 change() 시에 Null 들어갈 경우 실패 테스트")
    @Test
    void argumentNull_test() {
        final User user = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE)).encode(PASSWORD_ENCODER);
        assertAll(
                () -> assertThatThrownBy(() -> user.changeEmail(null)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> user.changeBio(null)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> user.changeImage(null)).isInstanceOf(IllegalArgumentException.class)
        );
    }


    @DisplayName("User 인스턴스 following 테스트")
    @Test
    void following_test() {
        final User following = userBuilder(new Email(EMAIL), new Name(USERNAME), new Password(PASSWORD), new Bio(BIO), new Image(IMAGE));
        final User follower = userBuilder(new Email("Email2@email.com"), new Name("differentUserName"), new Password("Password2"), new Bio("Bio2"), new Image("Image2"));
        final Follow follow = followBuilder(following, follower);
        follower.addfollowing(follow);

        assertAll(
                () -> assertThat(follower.followings().size()).isEqualTo(1),
                () -> assertThat(follower.followings().contains(follow)).isTrue()
        );
    }

    public static final User userBuilder(final Email email, final Name username,
                                         final Password password, final Bio bio, final Image image) {
        return User.Builder()
                .email(email)
                .username(username)
                .password(password)
                .bio(bio)
                .image(image)
                .build();
    }

}