package com.bugreport.bugreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/BugReports")
public class BugReportController {
    @Autowired
    BugReportService bugReportService;

    @PostMapping(path = "/add", consumes = "application/json")
    public @ResponseBody BugReport addNewBugReport(@RequestBody BugReport br) {
        return bugReportService.addNewBugReport(br);
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<BugReport> getAllBugReports(
            @RequestParam(required = false) Integer pageId,
            @RequestParam(required = false) String filter) {
        return bugReportService.GetAllBugReports(pageId, filter);      
    }

    @GetMapping(path = "/get")
    public @ResponseBody BugReport getBugReportbyId(@RequestParam Integer id) {
        BugReport result = bugReportService.getBugReportbyId(id);
        if (result != null) {
            return result;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Found no bug report with id " + id);
        }
    }

    @DeleteMapping(path = "/delete")
    public @ResponseBody void deleteBugReportById(@RequestParam Integer id) {
        if (!bugReportService.deleteBugReportById(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Found no bug report with id " + id);
        }
    }
}
