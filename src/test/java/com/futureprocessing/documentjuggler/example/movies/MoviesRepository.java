package com.futureprocessing.documentjuggler.example.movies;

import com.futureprocessing.documentjuggler.BaseRepository;
import com.mongodb.DB;

public class MoviesRepository extends BaseRepository<Movie> {

    public MoviesRepository(DB db) {
        super(db.getCollection(Movie.class.getSimpleName()), Movie.class);
    }
}
