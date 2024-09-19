package com.godigit.taskAppivation.service;

import com.godigit.taskAppivation.dto.TaskDto;
import com.godigit.taskAppivation.dto.UserDto;
import com.godigit.taskAppivation.exception.ResourceAlreadyExistException;
import com.godigit.taskAppivation.model.TaskModel;
import com.godigit.taskAppivation.model.UserModel;
import com.godigit.taskAppivation.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    private UserDto convertToDto(UserModel userModel) {
        return modelMapper.map(userModel, UserDto.class);
    }

    private UserModel convertToEntity(UserDto user) {
        return modelMapper.map(user, UserModel.class);
    }

    public UserDto registerUser(UserModel userData)  {
        UserModel userByUsername = userRepository.findByUsername(userData.getUsername());
        UserModel userByEmail = userRepository.findByEmail(userData.getEmail());

        if ((userByUsername != null) || (userByEmail != null)) {
            throw new ResourceAlreadyExistException("User Already Exists with that credential");
        }
        UserModel save_user = userRepository.save(userByUsername);

        return convertToDto(save_user);
    }

    public void saveUser(UserModel userModel) {
        convertToDto(userRepository.save(userModel));
    }

    public void updateUserDetails(long userId, UserModel user) {
        UserModel userById = getUserById(userId);

        if (user.getUsername() != null)
            userById.setUsername(user.getUsername());
        if (user.getPassword() != null)
            userById.setPassword(user.getPassword());

        userRepository.save(userById);
    }


    public String deleteUser(long userId) {
        // Code to delete a user
        userRepository.deleteById(userId);
        return "user with the id " + userId + " has been Deleted";
    }

    public List<TaskDto> getUserTasks(long userId) {
        UserModel user = userRepository.findById(userId).orElseThrow();
        return user.getTasks().stream().map(this::convertToDto).toList();
    }

    private TaskDto convertToDto(TaskModel taskModel) {
        return modelMapper.map(taskModel, TaskDto.class);
    }

    private TaskModel convertToEntity(TaskDto taskDto) {
        return modelMapper.map(taskDto, TaskModel.class);
    }

    public UserModel getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}
