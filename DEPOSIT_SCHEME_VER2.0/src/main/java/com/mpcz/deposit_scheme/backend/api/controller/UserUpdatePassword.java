package com.mpcz.deposit_scheme.backend.api.controller;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.backend.api.domain.User;
import com.mpcz.deposit_scheme.backend.api.dto.UserUpdatePasswordDTO;
import com.mpcz.deposit_scheme.backend.api.jwt.security.util.PasswordUtil;
import com.mpcz.deposit_scheme.backend.api.repository.UserRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.UserService;

//  new class for update password for user 
@RestController
@RequestMapping("/api/updateUser")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserUpdatePassword {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@PutMapping("/password")
	public Response<?> changePassword(@RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO) {
		Response<?> response = new Response<>();
		User save = null;
		try {
			User user = userService.findByUserId(userUpdatePasswordDTO.getUserid());
			if (!userUpdatePasswordDTO.getPassword().equals(userUpdatePasswordDTO.getConfirmPassword())) {
				response.setCode("100");
				response.setMessage("password or confirm password not match");
				response.setList(null);
				return response;
			} else {
				final String password = PasswordUtil.getPasswordHash(userUpdatePasswordDTO.getPassword());

				user.setUserCredentials(password);
				save = userRepository.save(user);
			}

		} catch (Exception e) {
			response.setCode("100");
			response.setMessage("user not found plesase given valid Id");
			return response;
		}
		if (save != null) {
			response.setCode("200");
			response.setMessage("password Sucessfully changed");
			return response;
		} else {
			response.setCode("100");
			response.setMessage("password not changed");
			return response;
		}

	}

}
