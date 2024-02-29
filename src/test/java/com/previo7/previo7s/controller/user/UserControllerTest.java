package com.previo7.previo7s.controller.user;

import com.previo7.previo7s.dto.UserDto;
import com.previo7.previo7s.dto.UserResponseDto;
import com.previo7.previo7s.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //todos los usuarios

    @Test
    public void get_all_users_with_data_test(){
        //Organizar
        List<UserResponseDto> users = new ArrayList<>();
        users.add(new UserResponseDto("1a", "Juan", "Perez", LocalDate.of(1950, 2, 15),"juan@mail.com"));
        users.add(new UserResponseDto("1b", "Carlos", "Cortez", LocalDate.of(1930, 2, 15),"carlos@mail.com"));
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        //actuar
        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        //afirmar la prueba
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void get_all_user_empty_list_test(){
        //Organizar
        Mockito.when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        //actuar

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        //afirmar la prueba

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertEquals(0, response.getBody().size());


    }

    @Test
    public void get_all_user_exception_test(){
        //Organizar
        Mockito.when(userService.getAllUsers()).thenThrow(new RuntimeException("An error has occurred in time of execution"));

        //actuar

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        //afirmar la prueba

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //Assertions.assertEquals(0, response.getBody().size());


    }

    //usuarios por id

    @Test
    public void find_user_by_id_existing_test(){
        //Organizar
        String userId = "a1";
        UserResponseDto userResponseDto = new UserResponseDto("1a", "Juan", "Perez", LocalDate.of(1950, 2, 15),"juan@mail.com");
        Mockito.when(userService.findUserById(userId)).thenReturn(userResponseDto);

        //Actuar

        ResponseEntity<UserResponseDto> response = userController.findUserById(userId);

        //Afirmar la prueba

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(userResponseDto, response.getBody());
    }

    @Test
    public void find_user_by_id_not_found_test() {
        // Arrange
        String userId = "456";
        Mockito.when(userService.findUserById(userId)).thenThrow(new NoSuchElementException("User not found"));

        // Act
        ResponseEntity<UserResponseDto> response = userController.findUserById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        //assertNull(response.getBody());
        assertEquals("The user " +  userId + " doesn't exist in the database", response.getBody());
    }

    //Crear usuarios

    @Test
    public void create_user_success_test() {
        // Arrange
        UserDto userDto = new UserDto("John", "Doe", LocalDate.of(1990, 5, 15), "john@example.com", "password");
        UserResponseDto userResponseDto = new UserResponseDto("123", "John", "Doe", LocalDate.of(1990, 5, 15), "john@example.com");
        Mockito.when(userService.createUser(userDto)).thenReturn(userResponseDto);

        // Act
        ResponseEntity<UserResponseDto> response = userController.createUser(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponseDto, response.getBody());
    }

    @Test
    public void create_user_failure_test() {
        // Arrange
        UserDto userDto = new UserDto("John", "Doe", LocalDate.of(1990, 5, 15), "john@example.com", "password");
        Mockito.when(userService.createUser(userDto)).thenThrow(new RuntimeException("Failed to create user"));

        // Act
        ResponseEntity<UserResponseDto> response = userController.createUser(userDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertNull(response.getBody());
    }

    //actualizar usuarios

    @Test
    public void update_user_success_test() {
        // Arrange
        String userId = "123";
        UserDto userDto = new UserDto("John", "Doe", LocalDate.of(1990, 5, 15), "john@example.com", "password");
        Mockito.when(userService.updateUser(userId, userDto)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = userController.updateUser(userId, userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void update_user_not_found_test() {
        // Arrange
        String userId = "123";
        UserDto userDto = new UserDto("John", "Doe", LocalDate.of(1990, 5, 15), "john@example.com", "password");
        Mockito.when(userService.updateUser(userId, userDto)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = userController.updateUser(userId, userDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void update_user_exception_test() {
        // Arrange
        String userId = "123";
        UserDto userDto = new UserDto("John", "Doe", LocalDate.of(1990, 5, 15), "john@example.com", "password");
        Mockito.when(userService.updateUser(userId, userDto)).thenThrow(new NoSuchElementException("User not found"));

        // Act
        ResponseEntity<Boolean> response = userController.updateUser(userId, userDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The user " + userId + " doesn't exist in the data base", response.getBody());
    }

    //Eliminar usuarios:

    @Test
    public void delete_user_success_test() {
        // Arrange
        String userId = "123";
        Mockito.when(userService.deleteUser(userId)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void delete_user_not_found_test() {
        // Arrange
        String userId = "123";
        Mockito.when(userService.deleteUser(userId)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void delete_user_exception_test() {
        // Arrange
        String userId = "123";
        Mockito.when(userService.deleteUser(userId)).thenThrow(new NoSuchElementException("User not found"));

        // Act
        ResponseEntity<Boolean> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The user " + userId + " doesn't exist in the data base", response.getBody());
    }


}