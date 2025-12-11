package com.danggeunko.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danggeunko.user.dto.Follow;
import com.danggeunko.user.dto.User;
import com.danggeunko.user.service.FollowService;
import com.danggeunko.user.service.UserService;

@RestController
@RequestMapping("/api-user/user")
@CrossOrigin("*")
public class UserController {

	private final UserService userService;
	private final FollowService followService;

	public UserController(UserService userService, FollowService followService) {
		this.userService = userService;
		this.followService = followService;
	}

	// 회원 등록
	@PostMapping("/")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		boolean completed = userService.addUser(user);
		if (completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 회원 검색
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
		User user = userService.getUserById(id);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 회원 전체 조회
	@GetMapping("/")
	public ResponseEntity<?> getAllUsers() {
		List<User> list = userService.getAllUsers();
		if (!list.isEmpty() && list.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 회원 정보 수정
	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
		boolean completed = userService.updateUser(userId, user);
		if (completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 회원 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
		boolean completed = userService.deleteUser(id);
		if (completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 팔로우 등록
	@PostMapping("/follow")
	public ResponseEntity<?> addFollow(@RequestBody Follow follow) {
		boolean completed = followService.addFollow(follow);
		if (completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 팔로우 삭제
	@DeleteMapping("/follow")
	public ResponseEntity<?> deleteFollow(@RequestBody Follow follow) {
		boolean completed = followService.deleteFollow(follow);
		if (completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 팔로잉 목록 조회
	@GetMapping("/follow/following/{userId}")
	public ResponseEntity<?> getFollowingById(@PathVariable("userId") int userId) {
		List<User> following = followService.getFollowingById(userId);
		if (following != null) {
			return ResponseEntity.status(HttpStatus.OK).body(following);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 팔로워 목록 조회
	@GetMapping("/follow/follower/{userId}")
	public ResponseEntity<?> getFollowerById(@PathVariable("userId") int userId) {
		List<User> follower = followService.getFollowerById(userId);
		if (follower != null) {
			return ResponseEntity.status(HttpStatus.OK).body(follower);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	// 닉네임 중복확인
	@GetMapping("/check/nickname/{nickname}")
	public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
	    boolean isAvailable = userService.isNicknameAvailable(nickname);
	    return ResponseEntity.ok().body(Map.of("available", isAvailable));
	}

	// 이메일 중복확인
	@GetMapping("/check/email/{email}")
	public ResponseEntity<?> checkEmail(@PathVariable String email) {
	    boolean isAvailable = userService.isEmailAvailable(email);
	    return ResponseEntity.ok().body(Map.of("available", isAvailable));
	}


}
