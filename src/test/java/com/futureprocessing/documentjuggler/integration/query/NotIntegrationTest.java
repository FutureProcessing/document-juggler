package com.futureprocessing.documentjuggler.integration.query;

import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.query.Exists;
import com.futureprocessing.documentjuggler.annotation.query.Not;
import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.futureprocessing.documentjuggler.integration.BaseIntegrationTest;
import com.futureprocessing.documentjuggler.model.DefaultModel;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import org.junit.Test;

import java.util.List;

import static com.futureprocessing.documentjuggler.assertions.JugglerAssertions.failExpectedException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class NotIntegrationTest extends BaseIntegrationTest {

    @CollectionName("collection")
    private interface ModelWithNot extends DefaultModel<ModelWithNot> {

        @DbField("field")
        Integer getField();

        @DbField("field")
        ModelWithNot withField(int value);


        @DbField("field")
        ModelWithNot whereField(Comparison<Integer> field);

        @DbField("name")
        ModelWithNot withName(String name);

        @DbField("name")
        String getName();

        @Not
        @Exists
        @DbField("name")
        ModelWithNot withNameNotExists(boolean notExists);
    }

    @Test
    public void shouldFindModelsWithoutFieldValue() {
        //given
        Repository<ModelWithNot> repository = new Repository<>(db(), ModelWithNot.class);
        String objA = repository.insert(o -> o.withField(1));
        String objB = repository.insert(o -> o.withField(2));
        String objC = repository.insert(o -> o.withField(3));
        String objD = repository.insert(o -> o.withName("SomeName"));

        //when
        List<ModelWithNot> found = repository.find(o -> o.whereField(field -> field.not().greaterThan(2))).all();

        //then
        assertThat(found).hasSize(3);
        assertThat(extractProperty("field").from(found)).containsExactly(1, 2, null);
        assertThat(extractProperty("name").from(found)).containsExactly(null, null, "SomeName");
        assertThat(extractProperty("id").from(found)).containsExactly(objA, objB, objD);
    }

    @Test
    public void shouldThrowForbiddenOperationExceptionForNegatedExists() {
        //given
        Repository<ModelWithNot> repository = new Repository<>(db(), ModelWithNot.class);

        //when
        try {
            List<ModelWithNot> found = repository.find(o -> o.withNameNotExists(true)).all();
        } catch (Exception e) {
            //then
            assertThat(e)
                    .isInstanceOf(ForbiddenOperationException.class)
                    .hasMessageContaining("Method withNameNotExists is forbidden in QUERY context");
            return;
        }
        failExpectedException();
    }
}
