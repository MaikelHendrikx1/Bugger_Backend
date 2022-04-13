package com.bugreport.bugreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugReportService {
    @Autowired
    private BugReportRepository bugReportRepository;    

    public String addNewBugReport(BugReport br) {
        bugReportRepository.save(br);
        return "Succesfully added " + br.toString();
    }

    public Iterable<BugReport> getAllBugReports(){
        return bugReportRepository.findAll();
    }

    public Iterable<BugReport> getAllBugReportsByFilter(String filter){
        return bugReportRepository.findByTitleContaining(filter);
    }

    public BugReport getBugReportbyId(Integer id) {
        return bugReportRepository.findById(id).orElseThrow();
    }

    public void deleteBugReportById(Integer id) {
        bugReportRepository.deleteById(id);
    }
}
