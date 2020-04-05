package com.example.myproject;

import android.os.Parcel;
import android.os.Parcelable;

//every game from RAWG database becomes Result object

public class Result {


    public int _id = 0;
    public String _slug = "";
    public String _name = "";
    public String _released = "";
    public boolean _tba = false;
    public String _backgroundImg = "";
    public Double _rating = 0.0;
    public Double _rating_top = 5.0;


    Result(int id, String slug, String name, String released, Boolean tba, String back, Double rating, Double ra_top){

        _id = id;
        _slug = slug;
        _name = name;
        _backgroundImg = back;
        _released = released;
        _tba = tba;
        _rating = rating;
        _rating_top = ra_top;

    }


    public String getId(){

        return Integer.toString(_id);
    }

    public String getSlug(){
        return _slug;
    }

    public String getName(){
        return _name;
    }
    public String getbackGroundImgURL(){
        return _backgroundImg;
    }
    public String getReleaseDate(){
        return _released;
    }

    public boolean getTBA(){
        return _tba;
    }

    public Double getRating(){

        return _rating;
    }
    public Double getRatingTop(){

        return _rating_top;
    }


}
