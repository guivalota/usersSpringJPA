package com.example.arena.arena;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.arena.arena.controller.UserController;
import com.example.arena.arena.entity.User;
import com.example.arena.arena.repository.UserRepository;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User2");
        user2.setEmail("user2@example.com");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userController.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getName());
        assertEquals("User2", result.get(1).getName());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("user1@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User1", response.getBody().getName());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("User1");
        user.setEmail("user1@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<String> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuário criado com sucesso. Id do usuário: "+ user.getId(), response.getBody());
    }

    @Test
    public void testCreateUserEmailConflict() {
        User user = new User();
        user.setName("User1");
        user.setEmail("user1@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userController.createUser(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email já está em uso.", response.getBody());
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("User1");
        existingUser.setEmail("user1@example.com");

        User updatedUserDetails = new User();
        updatedUserDetails.setName("UpdatedUser1");
        updatedUserDetails.setEmail("updateduser1@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        ResponseEntity<User> response = userController.updateUser(1L, updatedUserDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedUser1", response.getBody().getName());
        assertEquals("updateduser1@example.com", response.getBody().getEmail());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("user1@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userRepository, times(1)).delete(user);
    }
}