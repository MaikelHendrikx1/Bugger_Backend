package com.bugreport.bugreport;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BugreportApplicationTests {

	@Autowired
	private BugReportController bugReportController;

	@Test
	void contextLoads() throws Exception {
		assertNotNull(bugReportController);
	}	
}
