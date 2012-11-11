package com.anotherbrick.inthewall.datasource;

import java.util.ArrayList;

import processing.core.PApplet;

import com.project4.Tweet;

import de.bezier.data.sql.MySQL;

public class DataSourceSQL {

	private MySQL sql;
	private boolean  connected = false;

	public DataSourceSQL(PApplet context) {
		String user = "organic";
		String pass = "sharpcheddar";
		String database = "crash";
		String host = "inacarcrash.cnrtm99w3c2x.us-east-1.rds.amazonaws.com";
		sql = new MySQL(context, host, database, user, pass);
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Tweet> getTweets(String where) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		String query;
		if (connect()) {
			query = "SELECT id, user_id, lat, lon, creation_date_posix FROM tweets WHERE "
					+ where;
			System.out.println(query);
			sql.query(query);
			while (sql.next()) {
				Tweet tweet = new Tweet(sql.getDouble("lat"),
						sql.getDouble("lon"),
						sql.getInt("creation_date_posix"), sql.getInt("id"),
						sql.getInt("user_id"));
				tweets.add(tweet);
			}
			return tweets;
		}
		return null;
	}

	private boolean connect() {
		if ( connected )
			return true;
		else {
			return sql.connect();
		}
	}
}
