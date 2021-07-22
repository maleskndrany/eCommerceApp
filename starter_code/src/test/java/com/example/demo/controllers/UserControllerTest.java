package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void test_create_user(){

        when(bCryptPasswordEncoder.encode("pass123")).thenReturn("hashedPass123");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("maleskndrany");
        userRequest.setPassword("pass123");
        userRequest.setConfirmPassword("pass123");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("maleskndrany", user.getUsername());
        assertEquals("hashedPass123", user.getPassword());
    }

    @Test
    public void test_create_user_short_password(){

        when(bCryptPasswordEncoder.encode("pass123")).thenReturn("hashedPass123");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("maleskndrany");
        userRequest.setPassword("pass");
        userRequest.setConfirmPassword("pass");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void test_create_user_unmatched_passwords(){

        when(bCryptPasswordEncoder.encode("pass123")).thenReturn("hashedPass123");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("maleskndrany");
        userRequest.setPassword("pass");
        userRequest.setConfirmPassword("p123");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void test_get_user_by_id(){

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("maleskndrany");
        userRequest.setPassword("pass123");
        userRequest.setConfirmPassword("pass123");

        final ResponseEntity<User> userResponse = userController.createUser(userRequest);

        User user = userResponse.getBody();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));

        final ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();

        assertNotNull(returnedUser);
        assertEquals(0, returnedUser.getId());
    }

    @Test
    public void test_get_user_by_invalid_id(){

        final ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void test_get_user_by_username(){

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("maleskndrany");
        userRequest.setPassword("pass123");
        userRequest.setConfirmPassword("pass123");

        final ResponseEntity<User> userResponse = userController.createUser(userRequest);

        User user = userResponse.getBody();

        when(userRepository.findByUsername("maleskndrany")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("maleskndrany");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();

        assertNotNull(returnedUser);
        assertEquals("maleskndrany", returnedUser.getUsername());
    }

    @Test
    public void test_get_user_by_invalid_username(){

        when(userRepository.findByUsername("user123")).thenReturn(null);

        final ResponseEntity<User> response = userController.findByUserName("user123");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}
