package com.twitter.springboot.web.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twitter.springboot.web.app.models.dao.ITweetDao;
import com.twitter.springboot.web.app.models.entity.Tweet;


@Service
public class TweetServiceImpl implements ITweetService {

	@Autowired
	private ITweetDao tweetDao;
	
	@Override
	@Transactional
	public Tweet save (Tweet tweet) {

		return tweetDao.save(tweet);
	}


	@Override
	@Transactional(readOnly = true)
	public List<Tweet> findAll() {
		
		return (List<Tweet>) tweetDao.findAll();
	}

	
	@Override
	@Transactional (readOnly = true)
	public Tweet findById(Long id) {
		
		return tweetDao.findById(id).orElse(null);
	}
}
