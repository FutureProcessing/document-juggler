package com.futureprocessing.documentjuggler.example.movies;

import com.futureprocessing.documentjuggler.annotation.Id;

public interface Movie {

    static final String TITLE = "title";
    static final String YEAR = "year";

    @Id
    String getId();

//    @DbField(TITLE)
//    Movie withTitle(String title);

//    @DbField(YEAR)
//    Movie withYear(int year);


//    @DbField(TITLE)
//    String getTitle();

//    @DbField(YEAR)
//    int getYear();
}
