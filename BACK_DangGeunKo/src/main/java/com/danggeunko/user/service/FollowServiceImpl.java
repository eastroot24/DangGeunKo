package com.danggeunko.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danggeunko.user.dao.UserDao;
import com.danggeunko.user.dto.Follow;
import com.danggeunko.user.dto.User;
@Service
public class FollowServiceImpl implements FollowService {
	
	private final UserDao userDao;
	
	public FollowServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	@Override
	public boolean addFollow(Follow follow) {
		return userDao.addFollow(follow);
	}

	@Override
	public List<User> getFollowingById(int userId) {
		return userDao.getFollowingById(userId);
	}

	@Override
	public List<User> getFollowerById(int userId) {
		return userDao.getFollowerById(userId);
	}

	@Override
	public boolean deleteFollow(Follow follow) {
		return userDao.deleteFollow(follow);
	}

}
