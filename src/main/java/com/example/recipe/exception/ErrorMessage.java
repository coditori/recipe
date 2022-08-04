package com.example.recipe.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
@Schema(name = "Error Message", description = "Error Message")
public class ErrorMessage {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "time", description = "error date time")
    private Date time;

    @Schema(name = "message", example = "An error occurred", description = "error message")
    private String message;

    /**
     * Exception Type
     */
    public enum ExceptionTypeEnum {
        EXCEPTION,

        API_EXCEPTION,

        NOT_FOUND_EXCEPTION;
    }

    @Schema(name = "exceptionType", example = "ApiException", description = "Exception Type")
    private ExceptionTypeEnum exceptionType;

}
