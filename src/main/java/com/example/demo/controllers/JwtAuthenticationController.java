package com.example.demo.controllers;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.model.*;
import com.example.demo.repository.UserRedisRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;




@RestController
public class JwtAuthenticationController {

	@Autowired
	private UserRedisRepository userRedisRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	UserRepository userService;

//	@Autowired
//	BlackListTokenService blackListTokenService;

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}


	@CachePut(value = "users")
	@GetMapping("/signout")
	public String logOut(HttpServletRequest request){
		String token=request.getHeader("Authorization");
		String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
		UserRedis user= this.userRedisRepository.findById(username).orElse(new UserRedis(username));
		user.getJwts().add(token.substring(7));
		this.userRedisRepository.save(user);
		return "LOG OUT SUCCESSFUL!!!";
	}

	@GetMapping("/public")
	public String publicPage(){
		return "PUBLIC ONLY";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {

		if (userService.findByEmail(user.getEmail()) == null) {

			return ResponseEntity.ok(userDetailsService.save(user));
		} else {

			return new ResponseEntity<String>("{\"message\":\"Email Already Used\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/authenticated")
	public String getUserDetail() {

		return "Only Authenticated Allowed";

	}

	@PostMapping("/update-user")
	public User updateUser(@RequestBody User user, HttpServletRequest request) {
		String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization").substring(7));

		User current = userService.findByEmail(username);

		current.setName(user.getName());
		current.setAddress(user.getAddress());
		return userService.save(current);

	}

	@GetMapping("/checkifadmin")
	public ResponseEntity<?> testDecodeJWT(HttpServletRequest request) {
		

		if (request.getHeader("Authorization") != null) {
			String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization").substring(7));

			User current = userService.findByEmail(username);

			if (current.getRoles().contains("ADMIN")) {

				return new ResponseEntity<>("{\"role\":\"ADMIN\"}", HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("{\"role\":\"OTHERS\"}", HttpStatus.OK);

	}

	@GetMapping("/getusername")
	public String getUserName(HttpServletRequest request) {

		if (request.getHeader("Authorization") != null) {
			String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization").substring(7));
			return username;
		} else {
			return "INVALID JWT";
		}

	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
