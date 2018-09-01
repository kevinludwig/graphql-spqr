package io.leangen.graphql;

import com.fasterxml.jackson.annotation.JsonCreator;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLInterface;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TypeResolverTest {

    @Test
    public void testTypeResolutionWithBaseClass() {
        GraphQLSchema schema = new TestSchemaGenerator()
                .withResolverBuilders(new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(new RootQuery())
                .generate();

        GraphQL exe = GraphQL.newGraphQL(schema).build();
        ExecutionResult res = exe.execute("{contents {id title}}");
        System.out.println(res.getErrors());
        assertTrue(res.getErrors().isEmpty());
    }

    @Test
    public void testTypeResolutionWithoutBaseClass() {
        GraphQLSchema schema = new TestSchemaGenerator()
                .withResolverBuilders(new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(new RootQuery2())
                .generate();

        GraphQL exe = GraphQL.newGraphQL(schema).build();
        ExecutionResult res = exe.execute("{contents {id title}}");
        System.out.println(res.getErrors());
        assertTrue(res.getErrors().isEmpty());
    }

    public static class RootQuery {
        @GraphQLQuery
        public List<Content> contents() {
            return Arrays.asList(new Trailer("1", "Argo"),
                    new Trailer("2", "Gravity"),
                    new Movie("3", "The Ring", "R"),
                    new Movie("4", "Brazil", "R"),
                    new TVShow("5", "Simpsons", 1, 2));
        }
    }

    public static class RootQuery2 {
        @GraphQLQuery
        public List<Content> contents() {
            return Arrays.asList(new Trailer2("1", "Argo"),
                    new Trailer2("2", "Gravity"),
                    new Movie2("3", "The Ring", "R"),
                    new Movie2("4", "Brazil", "R"),
                    new TVShow2("5", "Simpsons", 1, 2));
        }
    }

    @GraphQLInterface(name = "Content", implementationAutoDiscovery = true)
    public interface Content {
        @GraphQLQuery
        String id();

        @GraphQLQuery
        String title();
    }

    public static class ContentBase implements Content {
        private String id;
        private String title;
        ContentBase(String id, String title) {
            this.title = title;
            this.id = id;
        }
        @Override
        @GraphQLQuery
        public String id() {return id;}

        @Override
        @GraphQLQuery
        public String title() {return title;}
    }

    public static class Movie extends ContentBase {
        private String rating;
        Movie(String id, String title, String rating) {
            super(id, title);
            this.rating = rating;
        }

        @GraphQLQuery
        public String rating() {return rating;}
    }

    public static class Trailer extends ContentBase {
        Trailer(String id, String title) {
            super(id, title);
        }
    }

    public static class TVShow extends ContentBase {
        private Integer seasonNumber;
        private Integer episodeNumber;
        TVShow(String id, String title, Integer seasonNumber, Integer episodeNumber) {
            super(id, title);
            this.seasonNumber = seasonNumber;
            this.episodeNumber = episodeNumber;
        }

        @GraphQLQuery
        public Integer seasonNumber() {return seasonNumber;}

        @GraphQLQuery
        public Integer episodeNumber() {return episodeNumber;}
    }

    public static class Movie2 implements Content {
        private String id;
        private String title;
        private String rating;
        Movie2(String id, String title, String rating) {
            this.id = id;
            this.title = title;
            this.rating = rating;
        }

        @Override
        @GraphQLQuery
        public String id() {return id;}

        @Override
        @GraphQLQuery
        public String title() {return title;}

        @GraphQLQuery
        public String rating() {return rating;}
    }

    public static class Trailer2 implements Content {
        private String id;
        private String title;
        Trailer2(String id, String title) {
            this.id = id;
            this.title = title;
        }
        @Override
        @GraphQLQuery
        public String id() {return id;}

        @Override
        @GraphQLQuery
        public String title() {return title;}
    }

    public static class TVShow2 implements Content {
        private Integer seasonNumber;
        private Integer episodeNumber;
        private String id;
        private String title;
        TVShow2(String id, String title, Integer seasonNumber, Integer episodeNumber) {
            this.id = id;
            this.title = title;
            this.seasonNumber = seasonNumber;
            this.episodeNumber = episodeNumber;
        }

        @Override
        @GraphQLQuery
        public String id() {return id;}

        @Override
        @GraphQLQuery
        public String title() {return title;}

        @GraphQLQuery
        public Integer seasonNumber() {return seasonNumber;}

        @GraphQLQuery
        public Integer episodeNumber() {return episodeNumber;}
    }
}
