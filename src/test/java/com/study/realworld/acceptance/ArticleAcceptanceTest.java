package com.study.realworld.acceptance;

import com.study.realworld.domain.article.dto.ArticleSave;
import com.study.realworld.domain.auth.dto.Login;
import com.study.realworld.domain.user.dto.UserJoin;
import com.study.realworld.global.common.AccessToken;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ArticleAcceptanceTest extends AcceptanceTest {

    private UserJoin.Response user1;
    private UserJoin.Response user2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        final String user1String = "woozi@naver.com";
        user1 = 회원_가입_되어있음(user1String);

        final String user2String = "ori@naver.com";
        user2 = 회원_가입_되어있음(user2String);
    }

    @Test
    void 게시글을_등록한다() {
        final Login.Response loginResponse = 로그인_되어있음(user1.userEmail().userEmail());
        final ExtractableResponse<Response> response = 정상적인_게시글_등록_요청(loginResponse.accessToken());
        final ArticleSave.Response articleResponse = response.as(ArticleSave.Response.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(articleResponse.slug()).isEqualTo("how-to-train-your-dragon"),
                () -> assertThat(articleResponse.title()).isEqualTo("how to train your dragon"),
                () -> assertThat(articleResponse.description()).isEqualTo("Ever wonder how?"),
                () -> assertThat(articleResponse.body()).isEqualTo("It takes a Jacobian"),
                () -> assertThat(articleResponse.tags()).isEqualTo(List.of("dragons", "training")),
                () -> assertThat(articleResponse.createdAt()).isEqualTo("2016-02-18T03:22:56.637Z"),
                () -> assertThat(articleResponse.updatedAt()).isEqualTo("2016-02-18T03:48:35.824Z"),
                () -> assertThat(articleResponse.favorited()).isEqualTo(false),
                () -> assertThat(articleResponse.favoritesCount()).isEqualTo(0),
                () -> assertThat(articleResponse.author().userName()).isEqualTo("jake"),
                () -> assertThat(articleResponse.author().userBio()).isEqualTo("I work at statefarm"),
                () -> assertThat(articleResponse.author().userImage()).isEqualTo("https://i.stack.imgur.com/xHWG8.jpg"),
                () -> assertThat(articleResponse.author().following()).isEqualTo(false)
        );
    }

    protected ExtractableResponse<Response> 정상적인_게시글_등록_요청(final AccessToken accessToken) {
        final ArticleSave.Request request = 정상적인_게시글_정보();
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/articles")
                .then()
                .extract();
    }

    protected ArticleSave.Request 정상적인_게시글_정보() {
        return ArticleSave.Request.builder()
                .title("How to train your dragon")
                .description("Ever wonder how?")
                .body("You have to believe")
                .tags(List.of("reactjs", "angularjs", "dragons"))
                .build();
    }
}
