package com.i18nsolutions.themeddoc;

public class ReviewDetails
{
    public String name,comment;
    public int Ratings;
    public ReviewDetails(String name,String comment,int Ratings)
    {
        this.name=name;
        this.comment=comment;
        this.Ratings=Ratings;
    }
    public ReviewDetails()
    {
    }
}