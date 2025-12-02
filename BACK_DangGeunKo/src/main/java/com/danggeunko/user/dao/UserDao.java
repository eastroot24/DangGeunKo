package com.danggeunko.user.dao;

import java.util.List;

import com.danggeunko.user.dto.User;

public interface UserDao {
	//유저 추가
	public int insertUser(User user);
	//특정 유저 조회
	public User getUserById(int userId);
	//전체 유저 조회
	public List<User> getAllUsers();
	//유저 수정
	public int updateUser(int userId, User user);
	//유저 삭제
	public int deleteUser(int userId);
	
}
