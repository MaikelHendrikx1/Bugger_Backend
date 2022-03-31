package com.bugreport.bugreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/BugReports")
public class BugReportController {
    @Autowired
    private BugReportRepository bugReportRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewBugReport(@RequestParam String title, @RequestParam String description){
        BugReport br = new BugReport(title, description);
        bugReportRepository.save(br);
        return "Succesfully added " +  br.toString();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<BugReport> getAllBugReports(){
        return bugReportRepository.findAll();
    }

    @GetMapping(path="/get")
    public @ResponseBody BugReport getBugReportbyId(@RequestParam Integer id){
        return bugReportRepository.findById(id).orElseThrow();
    }

    @DeleteMapping(path="delete")
    public @ResponseBody void deleteBugReportById(@RequestParam Integer id){
        bugReportRepository.deleteById(id);
    }
}
