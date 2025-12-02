package com.danggeunko.user.dto;

import java.time.LocalDateTime;

public class Follow {
	private int followingId;
	private int followerId;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public Follow(int followingId, int followerId) {
		this.followingId = followingId;
		this.followerId = followerId;
	}

	public int getFollowingId() {
		return followingId;
	}

	public void setFollowingId(int followingId) {
		this.followingId = followingId;
	}

	public int getFollowerId() {
		return followerId;
	}

	public void setFollowerId(int followerId) {
		this.followerId = followerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Follow [followingId=" + followingId + ", followerId=" + followerId + ", createdAt=" + createdAt + "]";
	}
	
	
	
	
}
