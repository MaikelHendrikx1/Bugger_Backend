package com.bugreport.bugreport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import de.cronn.testutils.h2.H2Util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(H2Util.class)
public class BugReportEndToEndTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BugReportRepository bugReportRepository;

    private List<BugReport> initialEntities = List.of(
            new BugReport("Alpha report", "Test report 1 description", 1),
            new BugReport("Report alpha", "Test report 2 description", 1),
            new BugReport("Bravo report", "Test report 3 description", 1),
            new BugReport("Alpha report bravo", "Test report 4 description", 2),
            new BugReport("bravo", "Test report 5 description", 3));

    @BeforeEach
    public void InitializeEntities() {
        bugReportRepository.saveAll(initialEntities);
    }

    @AfterEach
    public void ResetTable(@Autowired H2Util h2Util) {
        h2Util.resetDatabase();
    }

    

    @Test
    public void GetShouldReturnCorrectRecord() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/BugReports/get").param("id", "1"))
                .andDo(print()).andReturn();

        String content = result.getResponse().getContentAsString();

        Assert.isTrue(content != null && content.length() > 0,
                "Expected a record to be returned, but got 'null' or and empty string.");

        BugReport bugReportResult = objectMapper.readValue(content, BugReport.class);

        Assert.isTrue(bugReportResult.id == 1,
                "An incorrect bugreport was returned.");
    }

    @Test
    public void GetShouldReturn400WhenIdDoesNotExist() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/BugReports/get").param("id", "832758"))
                .andDo(print()).andReturn();

        Integer returnedStatus = result.getResponse().getStatus();

        Assert.isTrue(returnedStatus == 400, "Get returned '" + returnedStatus + "' instead of expected '400'.");

        String returnedJson = result.getResponse().getContentAsString();

        Assert.isTrue(returnedJson == null || returnedJson.length() == 0,
                "Get returned '" + returnedJson + "' instead of expected 'null' or ''");
    }

    @Test
    public void AddShouldReturnNewlyCreatedRecord() throws Exception {
        BugReport newBugReport = new BugReport("Test bug report", "test description", 1);

        MvcResult result = this.mockMvc.perform(post("/BugReports/add")
                .content(objectMapper.writeValueAsString(newBugReport))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andReturn();

        BugReport resultContent = objectMapper.readValue(result.getResponse().getContentAsString(), BugReport.class);

        Assert.isTrue(resultContent.id == initialEntities.size() + 1,
                "The resulted id '" + resultContent.id + "' does not match the expected id '" + initialEntities.size() + 1);

        Assert.isTrue(
                resultContent.title.equals(newBugReport.title) && resultContent.description.equals(newBugReport.description),
                    "The resulted description '" + resultContent.description + "' not equal to '" + newBugReport.description + 
                    "' and/or the resulted title'" + resultContent.title + "' not equal to '" + newBugReport.title + "'");
    }

    @Test
    public void DeleteShouldRemoveRecord() throws Exception {
        this.mockMvc.perform(delete("/BugReports/delete").param("id", "3"))
                .andDo(print()).andReturn();

        BugReport returnedBugReport = bugReportRepository.findById(3).orElse(null);

        Assert.isNull(returnedBugReport, "Repository returned a bug report which should have been removed.");
    }

    @Test
    public void DeleteShouldReturn400WhenIdDoesNotExist() throws Exception {
        MvcResult result = this.mockMvc.perform(delete("/BugReports/delete").param("id", "42069"))
                .andDo(print()).andReturn();

        Integer returnedStatus = result.getResponse().getStatus();

        Assert.isTrue(returnedStatus == 400, "Get returned '" + returnedStatus + "' instead of expected '400'.");
    }

    @Test
    public void GetAllShouldReturnAllRecordsWhenNoParamsSupplied() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/BugReports/all")).andDo(print()).andReturn();

        BugReport[] returnedBugreports = objectMapper.readValue(result.getResponse().getContentAsString(),
                BugReport[].class);

        Assert.isTrue(returnedBugreports.length == initialEntities.size(),
                "Expected the same amount of entities to be returned, but got '" + returnedBugreports.length
                + "' instead of '" + initialEntities.size() + "'");

        Assert.noNullElements(returnedBugreports,
                "Expected no nulls in returned list, but there is atleast 1 null element.");
    }

    @Test
    public void GetAllShouldOnlyReturnRecordsWithPageIdWhenPageIdSupplied() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/BugReports/all").param("pageId", "1"))
                .andDo(print()).andReturn();

        BugReport[] returnedBugreports = objectMapper.readValue(result.getResponse().getContentAsString(),
                BugReport[].class);

        Assert.isTrue(returnedBugreports.length == 3, "Expected 3 entities, but got " + returnedBugreports.length);

        for (BugReport bugReport : returnedBugreports) {
            Assert.isTrue(bugReport.pageId == 1,
                    "Got a returned bugReport with pageId " + bugReport.pageId + " instead of expected 1");
        }
    }

    @Test
    public void GetAllShouldOnlyReturnRecordsWhereTitleContainsFilterWhenFilterSupplied() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/BugReports/all").param("filter", "alpha"))
                .andDo(print()).andReturn();

        BugReport[] returnedBugreports = objectMapper.readValue(result.getResponse().getContentAsString(),
                BugReport[].class);

        Assert.isTrue(returnedBugreports.length == 3, "Expected 3 entities, but got " + returnedBugreports.length);

        for (BugReport bugReport : returnedBugreports) {
            Assert.isTrue(bugReport.title.toLowerCase().contains("alpha"),
                    "Got a returned bugReport with title '" + bugReport.title + "' instead of expected containing 'alpha'");
        }
    }

    @Test
    public void GetAllShouldOnlyReturnRecordsWithPageIdAndWhereTitleContainsFilterWhenBothSupplied() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/BugReports/all")
                .param("filter", "alpha").param("pageId", "1"))
                .andDo(print()).andReturn();

        BugReport[] returnedBugreports = objectMapper.readValue(result.getResponse().getContentAsString(),BugReport[].class);

        Assert.isTrue(returnedBugreports.length == 2, "Expected 2 entities, but got " + returnedBugreports.length);

        for (BugReport bugReport : returnedBugreports) {
            Assert.isTrue(bugReport.title.toLowerCase().contains("alpha"),
                    "Got a returned bugReport with title '" + bugReport.title + "' instead of expected containing 'alpha'");

            Assert.isTrue(bugReport.pageId == 1, "Got a returned bugReport with pageId " + bugReport.pageId + " instead of expected 1");
        }
    }
}
