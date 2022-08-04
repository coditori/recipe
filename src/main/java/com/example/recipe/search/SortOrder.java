package com.example.recipe.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortOrder {

    private List<String> ascendingOrder;

    private List<String> descendingOrder;
}
