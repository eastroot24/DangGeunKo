package com.danggeunko.user.dao;

import java.util.List;

import com.danggeunko.auth.dto.LoginRequest;
import com.danggeunko.user.dto.Follow;
import com.danggeunko.user.dto.User;

public interface UserDao {
	//유저 추가
	public int insertUser(User user);
	//특정 유저 조회
	public User getUserById(int userId);
	//닉네임으로 검색
	public User getUserByNickname(String nickname);
	//전체 유저 조회
	public List<User> getAllUsers();
	//유저 수정
	public int updateUser(int userId, User user);
	//유저 삭제
	public int deleteUser(int userId);
	
	//팔로우 추가
	public boolean addFollow(Follow follow);
	//팔로우 삭제
	public boolean deleteFollow(Follow follow);
	//팔로잉 조회
	public List<User> getFollowingById(int userId);
	//팔로워 조회
	public List<User> getFollowerById(int userId);
	
	//닉네임 중복 확인
	public int countByNickname(String nickname);
	//이메일 중복 확인
	public int countByEmail(String email);
	
	//로그인
	public User login(LoginRequest loginRequest);
	

}
