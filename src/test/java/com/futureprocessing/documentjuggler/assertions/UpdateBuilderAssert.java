package com.futureprocessing.documentjuggler.assertions;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.mongodb.DBObject;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBuilderAssert extends AbstractAssert<UpdateBuilderAssert, UpdateBuilder> {
    protected UpdateBuilderAssert(UpdateBuilder actual) {
        super(actual, UpdateBuilderAssert.class);
    }

    public UpdateBuilderAssert hasDocumentEqualTo(DBObject expected) {
        assertThat(actual.getDocument()).isEqualTo(expected);
        return this;
    }

}
