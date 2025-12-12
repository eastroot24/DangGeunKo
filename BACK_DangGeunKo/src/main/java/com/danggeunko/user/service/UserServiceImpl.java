package com.danggeunko.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danggeunko.auth.dto.LoginRequest;
import com.danggeunko.user.dao.UserDao;
import com.danggeunko.user.dto.User;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional
	public boolean addUser(User user) {
		return userDao.insertUser(user) > 0;
	}

	@Override
	@Transactional
	public User getUserById(int id) {
		return userDao.getUserById(id);
	}

	@Override
	@Transactional
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Override
	@Transactional
	public boolean updateUser(int id, User user) {
		return userDao.updateUser(id, user) > 0;
	}

	@Override
	@Transactional
	public boolean deleteUser(int id) {
		return userDao.deleteUser(id) > 0;
	}
	
	@Override
	@Transactional
	public boolean isNicknameAvailable(String nickname) {
	    return userDao.countByNickname(nickname) == 0;
	}
	
	@Override
	@Transactional
	public boolean isEmailAvailable(String email) {
	    return userDao.countByEmail(email) == 0;
	}

	@Override
	public User login(LoginRequest loginRequest) {
		return userDao.login(loginRequest);
	}

	
	
	
}
