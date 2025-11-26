package com.danggeunko.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danggeunko.user.dao.UserDao;
import com.danggeunko.user.dto.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public boolean addUser(User user) {
		return userDao.insertUser(user) > 0;
	}

	@Override
	public User getUserById(int id) {
		return userDao.getUserById(id);
	}

	@Override
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Override
	public boolean updateUser(int id, User user) {
		return userDao.updateUser(id, user) > 0;
	}

	@Override
	public boolean deleteUser(int id) {
		return userDao.deleteUser(id) > 0;
	}
	
	
	
}
