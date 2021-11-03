package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {

        this.baseUrl = url;
    }
    public User[] getUsers(AuthenticatedUser user) throws UserServiceException {
        HttpEntity<AuthenticatedUser> entity = createRequestEntity(user);
        return sendUsersRequest(entity);
    }

    private User [] sendUsersRequest(HttpEntity<AuthenticatedUser> entity) throws UserServiceException {
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "users", HttpMethod.GET,
                    entity, User[].class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            String message = createUserServiceException(ex);
            throw new UserServiceException(message);
        }
    }

    private HttpEntity<AuthenticatedUser> createRequestEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(user, headers);
    }

    private String createUserServiceException(RestClientResponseException ex) {
        String message = null;
        if (ex.getRawStatusCode() == 401 && ex.getResponseBodyAsString().length() == 0) {
            message = ex.getRawStatusCode() + " : {\"timestamp\":\"" + LocalDateTime.now() + "+00:00\",\"status\":401,\"error\":\"Invalid or expired token\",\"message\":\"Fetch users details failed: Invalid or expired token\",\"path\":\"/users\"}";
        }
        else {
            message = ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString();
        }
        return message;
    }
}
