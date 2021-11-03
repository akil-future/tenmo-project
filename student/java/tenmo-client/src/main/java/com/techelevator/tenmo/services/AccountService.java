package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }

    public Account getAccount(AuthenticatedUser user) throws AccountServiceException {
		HttpEntity<AuthenticatedUser> entity = createRequestEntity(user);
		return sendAccountRequest(entity);
	}

	private Account sendAccountRequest(HttpEntity<AuthenticatedUser> entity) throws AccountServiceException {
		try {
			ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "account", HttpMethod.GET, entity, Account.class);
			return response.getBody();
		} catch (RestClientResponseException ex) {
			String message = createAccountServiceException(ex);
			throw new AccountServiceException(message);
		}
	}

	private HttpEntity<AuthenticatedUser> createRequestEntity(AuthenticatedUser user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(user.getToken());
		return new HttpEntity<>(user, headers);
	}

	private String createAccountServiceException(RestClientResponseException ex) {
		String message = null;
		if (ex.getRawStatusCode() == 401 && ex.getResponseBodyAsString().length() == 0) {
		    message = ex.getRawStatusCode() + " : {\"timestamp\":\"" + LocalDateTime.now() + "+00:00\",\"status\":401,\"error\":\"Invalid or expired token\",\"message\":\"Fetch account details failed: Invalid or expired token\",\"path\":\"/account\"}";
		}
		else {
		    message = ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString();
		}
		return message;
	}

}
