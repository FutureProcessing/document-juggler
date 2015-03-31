package com.futureprocessing.documentjuggler.read;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static com.futureprocessing.documentjuggler.helper.Sets.asSet;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProjectionCreatorTest {

    @Mock
    private ReadMapper mapperMock;

    @Test
    public void shouldCreateDbObjectContainingFieldsProvidedAsArgument() {
        //given
        final Set<String> fields = asSet("A", "B", "C");
        ProjectionCreator projectionCreator = ProjectionCreator.create(null);

        //when
        DBObject projection = projectionCreator.getProjection(fields);

        //then
        DBObject expectedProjection = new BasicDBObject("A", 1).append("B", 1).append("C", 1);
        assertThat(projection).isEqualTo(expectedProjection);
    }

    @Test
    public void shouldCreateDbObjectContainingFieldsFromMapperWhenNotProvidedAsArgument() {
        //given
        BDDMockito.given(mapperMock.getSupportedFields()).willReturn(asSet("D", "E"));

        ProjectionCreator projectionCreator = ProjectionCreator.create(mapperMock);

        //when
        DBObject projection = projectionCreator.getProjection(emptySet());

        //then
        DBObject expectedProjection = new BasicDBObject("D", 1).append("E", 1);
        assertThat(projection).isEqualTo(expectedProjection);
    }
}
