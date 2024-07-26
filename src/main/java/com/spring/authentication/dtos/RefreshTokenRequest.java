package com.spring.authentication.dtos;

public class RefreshTokenRequest {
	public String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public String setRefreshToken(String refreshToken) {
		return this.refreshToken = refreshToken;
	}

}
