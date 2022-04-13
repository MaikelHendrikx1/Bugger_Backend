package com.bugreport.bugreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/BugReports")
public class BugReportController {
    @Autowired
    BugReportService bugReportService;

    @PostMapping(path="/add", consumes = "application/json")
    public @ResponseBody String addNewBugReport(@RequestBody BugReport br){
        return bugReportService.addNewBugReport(br);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<BugReport> getAllBugReports(@RequestParam(required = false) String filter){
        if (filter == null){
            return bugReportService.getAllBugReports();
        }
        else {
            return bugReportService.getAllBugReportsByFilter(filter);
        }
    }

    @GetMapping(path="/get")
    public @ResponseBody BugReport getBugReportbyId(@RequestParam Integer id){
        return bugReportService.getBugReportbyId(id);
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody void deleteBugReportById(@RequestParam Integer id){
        bugReportService.deleteBugReportById(id);
    }
}
