package com.danggeunko.user.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.danggeunko.auth.dto.LoginRequest;
import com.danggeunko.user.dao.UserDao;
import com.danggeunko.user.dto.User;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;
	private final String uploadPath = "C:/danggeunko/uploads/";
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
		// 저장 경로 폴더가 없으면 생성
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
	}

	@Override
	@Transactional
	public boolean addUser(User user, MultipartFile file) {
	    // 1. 파일이 있는 경우 처리
	    if (file != null && !file.isEmpty()) {
	        try {
	            String originalFileName = file.getOriginalFilename();
	            String saveFileName = UUID.randomUUID().toString() + "_" + originalFileName;
	            
	            File target = new File(uploadPath, saveFileName);
	            file.transferTo(target);
	            
	            user.setprofileImg(saveFileName);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false; 
	        }
	    } else {
	        user.setprofileImg("dgk-default-profile.png");
	    }
	    
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
