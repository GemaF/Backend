package com.twitter.springboot.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.twitter.springboot.web.app.models.entity.Tweet;


public interface ITweetDao extends CrudRepository<Tweet, Long> {

}
