package com.account.account;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountApplicationTests {

	@Autowired
	private AccountController accountController;

	@Test
	void contextLoads() throws Exception {
		assertNotNull(accountController);
	}	
}
