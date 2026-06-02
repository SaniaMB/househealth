package com.project.househealth.controllers;

import com.project.househealth.dto.request.CreateUserRequest;
import com.project.househealth.dto.response.UserResponse;
import com.project.househealth.entity.User;
import com.project.househealth.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

   private final UserService userService;

   public UserController(UserService userService){
      this.userService = userService;
   }

   @PostMapping
   public UserResponse createUser(@RequestBody CreateUserRequest request){

      User user = new User(request.getName(), request.getPassword(), request.getEmail());

      User savedUser = userService.createUser(user);

      return new UserResponse(savedUser.getUserId(), savedUser.getName(),
              savedUser.getEmail(), savedUser.getCreatedAt());
   }

   @GetMapping("/{id}")
   public UserResponse getUserById(@PathVariable Long id){

      User user = userService.getUserById(id);

      return new UserResponse(user.getUserId(), user.getName(),
              user.getEmail(), user.getCreatedAt()
      );
   }

   @GetMapping("/me")
   public String me() {
      return SecurityContextHolder
              .getContext()
              .getAuthentication()
              .getName();
   }

}
