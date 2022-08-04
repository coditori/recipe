package com.example.recipe.config;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;

public class SqlFunctionsRegistry implements MetadataBuilderContributor {

    public static String FUNCTION_FULL_TEXT_SEARCH = "fts";

    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction(FUNCTION_FULL_TEXT_SEARCH,
                new SQLFunctionTemplate(BooleanType.INSTANCE,
                        "to_tsvector(?1) @@ plainto_tsquery(?2)"));
    }
}
