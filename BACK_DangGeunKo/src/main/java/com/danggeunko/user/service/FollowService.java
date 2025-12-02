package com.danggeunko.user.service;

import java.util.List;

import com.danggeunko.user.dto.Follow;
import com.danggeunko.user.dto.User;

public interface FollowService {
		//팔로우 등록
		public boolean addFollow(Follow follow);
		//팔로잉 목록 조회
		public List<User> getFollowingById(int userId);
		//팔로워 목록 조회
		public List<User> getFollowerById(int userId);
		//팔로우 삭제
		public boolean deleteFollow(Follow follow);
}
