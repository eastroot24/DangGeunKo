package com.danggeunko.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danggeunko.user.dto.User;
import com.danggeunko.user.service.UserService;

@RestController
@RequestMapping("/api-user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//회원 등록
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@RequestBody User user){
		boolean completed = userService.addUser(user);
		if(completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	//회원 검색
	@GetMapping("user/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") int id){
		User user = userService.getUserById(id);
		if(user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	//회원 전체 조회
	@GetMapping("/user")
	public ResponseEntity<?> getAllUsers(){
		List<User> list = userService.getAllUsers();
		if(!list.isEmpty() && list.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	//회원 정보 수정
	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") int id, User user){
		boolean completed = userService.updateUser(id, user);
		if(completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	//회원 삭제
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
		boolean completed = userService.deleteUser(id);
		if(completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
}
