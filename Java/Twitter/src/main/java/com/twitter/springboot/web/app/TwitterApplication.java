package com.twitter.springboot.web.app;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.twitter.springboot.web.app.controllers.TweetRestController;

import twitter4j.TwitterException;

@SpringBootApplication
public class TwitterApplication {

	public static void main(String[] args) throws TwitterException, IOException {
		
		SpringApplication.run(TwitterApplication.class, args);
		
		TweetRestController stream = new TweetRestController();
		stream.execute();
	}
}
