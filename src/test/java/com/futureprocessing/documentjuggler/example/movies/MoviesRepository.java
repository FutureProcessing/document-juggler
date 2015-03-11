package com.futureprocessing.documentjuggler.example.movies;

import com.futureprocessing.documentjuggler.Repository;
import com.mongodb.DB;

public class MoviesRepository extends Repository<Movie> {

    public MoviesRepository(DB db) {
        super(db.getCollection(Movie.class.getSimpleName()), Movie.class);
    }
}
