package com.spring.authentication.dtos;

import lombok.Data;
import com.spring.authentication.enums.UserRole;

@Data
public class SignUpDto {
	
	 String login;
	 String password;
	 UserRole role;

}
