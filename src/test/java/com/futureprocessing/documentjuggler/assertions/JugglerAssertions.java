package com.futureprocessing.documentjuggler.assertions;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.mongodb.QueryBuilder;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;

public class JugglerAssertions extends Assertions {

    public static void failExpectedException() {
        Fail.fail("Should have thrown exeption");
    }

    public static UpdateBuilderAssert assertThat(UpdateBuilder updateBuilder) {
        return new UpdateBuilderAssert(updateBuilder);
    }

    public static QueryBuilderAssert assertThat(QueryBuilder queryBuilder) {
        return new QueryBuilderAssert(queryBuilder);
    }

}
