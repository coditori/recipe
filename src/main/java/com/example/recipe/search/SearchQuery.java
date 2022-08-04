package com.example.recipe.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery {
    private List<SearchFilter> searchFilters;
    @Schema(description = "page number", example = "0")
    private int pageNumber;
    @Schema(description = "page size", example = "10")
    private int pageSize;
    @Schema(hidden = true)
    private SortOrder sortOrder;
    private List<JoinColumnProps> joinColumnProps;
}
