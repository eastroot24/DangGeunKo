package com.danggeunko.user.dao;

import java.util.List;

import com.danggeunko.user.dto.User;

public interface UserDao {
	//유저 추가
	public int insertUser(User user);
	//특정 유저 조회
	public User getUserById(int id);
	//전체 유저 조회
	public List<User> getAllUser();
	//유저 수정
	public int updateUser(int id, User user);
	//유저 삭제
	public int deleteUser(int id);
	
}
