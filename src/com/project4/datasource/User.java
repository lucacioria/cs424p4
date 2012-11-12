package com.project4.datasource;

import java.util.ArrayList;


public class User {
  private int userId;
  private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
  
  public User(int userId, ArrayList<Tweet> tweets) {
    super();
    this.userId = userId;
    this.tweets = tweets;
  }

  public int getUserId() {
    return userId;
  }
  
  public ArrayList<Tweet> getTweets() {
    return tweets;
  }
}
