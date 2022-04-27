package com.bugreport.bugreport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BugReportRepository bugReportRepository;

    @BeforeEach
    public void ResetPersistence(){
        List<BugReport> initialEntities = List.of(
            new BugReport("Test report 1", "Test report 1 description"),
            new BugReport("Test report 2", "Test report 2 description"),
            new BugReport("Test report 3", "Test report 3 description"),
            new BugReport("Test report 4", "Test report 4 description"),
            new BugReport("Test report 5", "Test report 5 description"),
            new BugReport("Test report 6", "Test report 6 description"),
            new BugReport("Test report 7", "Test report 7 description")
        );

        bugReportRepository.saveAll(initialEntities);
    }

    @Test
    public void GetShouldReturnCorrectRecord() throws Exception{
        MvcResult result = this.mockMvc.perform(get("/BugReports/get").param("id", "1")).andDo(print()).andReturn();

        String content = result.getResponse().getContentAsString();

        Assert.isTrue(content != null && content.length() > 0, "Expected a record to be returned, but got 'null' or and empty string.");

        BugReport bugReportResult = objectMapper.readValue(content, BugReport.class);

        Assert.isTrue(bugReportResult.id == 1, "An incorrect bugreport was returned.");
    }

    @Test
    public void GetShouldReturn400WhenIdDoesNotExist() throws Exception{
        MvcResult result = this.mockMvc.perform(get("/BugReports/get").param("id", "832758")).andDo(print()).andReturn();

        Integer returnedStatus = result.getResponse().getStatus();

        Assert.isTrue(returnedStatus == 400, "Get returned '" + returnedStatus + "' instead of expected '400'.");

        String returnedJson = result.getResponse().getContentAsString();

        Assert.isTrue(returnedJson == null || returnedJson.length() == 0, "Get returned '" + returnedJson + "' instead of expected 'null' or ''");
    }
}
