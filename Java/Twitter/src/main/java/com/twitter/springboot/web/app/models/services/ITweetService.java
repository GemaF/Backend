package com.twitter.springboot.web.app.models.services;

import java.util.List;

import com.twitter.springboot.web.app.models.entity.Tweet;


public interface ITweetService {
	

	public List<Tweet> findAll();
	
	public Tweet findById(Long id);

	public Tweet save (Tweet tweet);

}
