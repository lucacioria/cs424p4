package com.project4.datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Model;
import com.project4.datasource.Day.WeatherEnum;

import de.bezier.data.sql.MySQL;

public class DataSourceSQL {

  private MySQL sql;
  private boolean connected = false;
  private HashMap<String, Double> idf;

  public DataSourceSQL(PApplet context) {
    String user, pass, database, host;
    if (com.anotherbrick.inthewall.Config.getInstance().onLocalServer) {
      user = "root";
      pass = "root";
      database = "tweets";
      host = "localhost";
    } else {
      user = "organic";
      pass = "sharpcheddar";
      database = "crash";
      host = "inacarcrash.cnrtm99w3c2x.us-east-1.rds.amazonaws.com";
    }

    sql = new MySQL(context, host, database, user, pass);
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ArrayList<User> getUsers(String where, int minTweets) {
    ArrayList<User> users = new ArrayList<User>();
    String query;
    if (connect()) {
      query =
          "select t.user_id, group_concat(t.lat) as lat, group_concat(t.lon) as lon, "
              + "group_concat(t.creation_date_posix) as creation_date_posix, group_concat(t.id) as id"
              + " from (select * from tweets as t1 where " + where
              + " order by t1.creation_date_posix) t group by t.user_id having count(*) >= "
              + minTweets;
      query(query);
      while (sql.next()) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        // get group attributes
        String[] lat = sql.getString("lat").split(",");
        String[] lon = sql.getString("lon").split(",");
        String[] id = sql.getString("id").split(",");
        String[] creation_date_posix = sql.getString("creation_date_posix").split(",");
        // for loop..
        int userId = sql.getInt("user_id");
        for (int i = 0; i < lat.length; i++) {
          try {
            Tweet tweet =
                new Tweet(Double.valueOf(lat[i]).doubleValue(), -Double.valueOf(lon[i])
                    .doubleValue(), Integer.valueOf(creation_date_posix[i]).intValue(), Integer
                    .valueOf(id[i]).intValue(), userId);
            tweets.add(tweet);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("error in parsing tweet");
          }
        }
        users.add(new User(userId, tweets));
      }
      return users;
    }
    return null;
  }

  public ArrayList<Day> getDays(ArrayList<Filter> filters) {
    Day[] days = new Day[21];
    Arrays.fill(days, null);
    for (int i = 0; i < filters.size(); i++) {
      String query;
      if (connect()) {
        query =
            "SELECT day, count(*) as count FROM tweets WHERE " + filters.get(i).getWhere()
                + " GROUP BY day";
        query(query);
        while (sql.next()) {
          // add day if doesn't exist
          if (days[sql.getInt("day")] == null) {
            Day day = emptyDay(sql.getInt("day"), filters);
            days[sql.getInt("day")] = day;
          }
          // update count
          days[sql.getInt("day")].setCount(filters.get(i), sql.getInt("count"));
        }
      }
    }
    // insert missing days
    for (int i = 0; i < days.length; i++) {
      if (days[i] == null) {
        days[i] = emptyDay(i, filters);
      }
    }
    //
    return new ArrayList<Day>(Arrays.asList(days));
  }

  private Day emptyDay(int dayNumber, ArrayList<Filter> filters) {
    Day day = new Day(dayNumber, WeatherEnum.CLEAR, 5, 0);
    for (Filter f : filters) {
      day.setCount(f, 0);
    }
    return day;
  }

  public ArrayList<Tweet> getTweets(String where) {
    ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    String query;
    if (connect()) {
      query = "SELECT id, user_id, lat, lon, creation_date_posix FROM tweets WHERE " + where;
      query(query);
      while (sql.next()) {
        Tweet tweet =
            new Tweet(sql.getDouble("lat"), -sql.getDouble("lon"),
                sql.getInt("creation_date_posix"), sql.getInt("id"), sql.getInt("user_id"));
        tweets.add(tweet);
      }
      return tweets;
    }
    return null;
  }

  public ArrayList<Tweet> getCountByDay(String where) {
    ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    String query;
    if (connect()) {
      query = "SELECT count(id, user_id, lat, lon, creation_date_posix FROM tweets WHERE " + where;
      query(query);
      while (sql.next()) {
        Tweet tweet =
            new Tweet(sql.getDouble("lat"), -sql.getDouble("lon"),
                sql.getInt("creation_date_posix"), sql.getInt("id"), sql.getInt("user_id"));
        tweets.add(tweet);
      }
      return tweets;
    }
    return null;
  }

  public void doStuff() {
    idf = new HashMap<String, Double>();
    if (connect()) {
      // prendi le stringhe dal db
      String query = "SELECT id, LOWER(text) as text FROM tweets WHERE id < 10000";
      query(query);
      ArrayList<Tweet> tweets = new ArrayList<Tweet>();
      while (sql.next()) {
        Tweet t = new Tweet(0, 0, 0, sql.getInt("id"), 0);
        t.setText(sql.getString("text"));
        tweets.add(t);
      }
      // conta roba
      for (Tweet t : tweets) {
        String x = t.getText();
        String[] parole = x.split(" ");
        for (String parola : parole) {
          if (!idf.containsKey(parola)) {
            idf.put(parola, 0.0);
          }
          idf.put(parola, idf.get(parola) + 1);
        }
      }
      // fai idf figo
      Iterator<String> i = idf.keySet().iterator();
      while (i.hasNext()) {
        String key = i.next();
        idf.put(key, (100000 / idf.get(key)));
      }
      // conto il tf
      for (Tweet t : tweets) {
        String x = t.getText();
        t.tf = new TreeMap<String, Double>();
        String[] parole = x.split(" ");
        for (String parola : parole) {
          if (!t.tf.containsKey(parola)) {
            t.tf.put(parola, 0.0);
          }
          t.tf.put(parola, t.tf.get(parola) + 1);
        }
      }
      // moltiplico la roba
      for (Tweet t : tweets) {
        i = t.tf.keySet().iterator();
        while (i.hasNext()) {
          String key = i.next();
          t.tf.put(key, t.tf.get(key) * idf.get(key));
        }
      }
      // tengo il maggiore
      for (Tweet t : tweets) {
        i = t.tf.keySet().iterator();
        String max = null;
        Double maxn = Double.MAX_VALUE;
        while (i.hasNext()) {
          String key = i.next();
          if (t.tf.get(key) > 5000 && t.tf.get(key) < maxn) {
            maxn = t.tf.get(key);
            max = key;
          }
        }
        System.out.println(max + " (" + maxn + ") " + t.getText());
      }
    }
  }

  private boolean connect() {
    if (connected)
      return true;
    else {
      return sql.connect();
    }
  }

  public String getTweetText(int id) {
    String query;
    if (connect()) {
      query = "SELECT text FROM tweets WHERE id = " + id;
      query(query);
      while (sql.next()) {
        return sql.getString("text");
      }
    }
    return null;
  }

  private void query(String query) {
    int start = Model.getInstance().p.millis();
    sql.query(query);
    int end = Model.getInstance().p.millis();
    System.out.println("[MYSQL] (" + (end - start) + "ms) " + query);
  }
}
