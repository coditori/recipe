package com.example.recipe.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilter {
    @Schema(description = "entity property name", example = "instructions")
    private String columnName;
    @Schema(description = "operator", example = "EQUALS", format = "enum")
    private QueryOperator operator;
    @Schema(description = "value of the filter. Use arrays for IN operator", example = "5")
    private Object value;
}
