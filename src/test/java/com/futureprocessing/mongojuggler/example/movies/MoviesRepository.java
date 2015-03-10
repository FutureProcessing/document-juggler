package com.futureprocessing.mongojuggler.example.movies;

import com.futureprocessing.mongojuggler.SimpleModelRepository;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class MoviesRepository extends SimpleModelRepository<Movie> {

    public MoviesRepository(DB db) {
        super(db.getCollection(Movie.class.getSimpleName()), Movie.class);
    }
}
