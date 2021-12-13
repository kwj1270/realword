package com.study.realworld.domain.favorite.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.study.realworld.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@JsonTypeName("errors")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class FavoriteErrorResponse {

    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonProperty("body")
    private List<String> body;

    public static FavoriteErrorResponse from(final ErrorCode errorCode) {
        return new FavoriteErrorResponse(errorCode.httpStatus(), List.of(errorCode.message()));
    }

    public ResponseEntity<FavoriteErrorResponse> toResponseEntity() {
        return ResponseEntity.status(this.httpStatus).body(this);
    }

    public List<String> body() {
        return body;
    }
}
