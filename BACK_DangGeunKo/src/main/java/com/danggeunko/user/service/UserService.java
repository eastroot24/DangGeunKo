package com.danggeunko.user.service;

import java.util.List;

import com.danggeunko.auth.dto.LoginRequest;
import com.danggeunko.user.dto.User;

public interface UserService {
	//회원 등록
	public boolean addUser(User user);
	//회원 조회
	public User getUserById(int id);
	//회원 전체 조회
	public List<User> getAllUsers();
	//회원 정보 수정
	public boolean updateUser(int id, User user);
	//회원 삭제
	public boolean deleteUser(int id);
	//닉네임 중복 확인
	public boolean isNicknameAvailable(String nickname);
	//이메일 중복 확인
	public boolean isEmailAvailable(String email);
	//로그인
	public User login(LoginRequest loginRequest);
	
}
