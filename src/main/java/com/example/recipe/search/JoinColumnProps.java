package com.example.recipe.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinColumnProps {
    @Schema(description = "child entity property name", example = "description")
    private String joinColumnName;
    private SearchFilter searchFilter;
}
