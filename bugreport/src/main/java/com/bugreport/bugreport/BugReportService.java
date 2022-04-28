package com.bugreport.bugreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugReportService {
    @Autowired
    private BugReportRepository bugReportRepository;    

    public BugReport addNewBugReport(BugReport br) {
        bugReportRepository.save(br);
        return br;
    }

    public Iterable<BugReport> GetAllBugReports(Integer pageId, String filter){
        if (pageId != null && filter != null){
            return bugReportRepository.findByPageIdEqualsAndTitleContainingIgnoreCase(pageId, filter);
        }
        else if (pageId != null) {
            return bugReportRepository.findByPageId(pageId);
        }
        else if (filter != null){
            return bugReportRepository.findByTitleContainingIgnoreCase(filter);
        }
        else {
            return bugReportRepository.findAll();
        }
    }

    public BugReport getBugReportbyId(Integer id) {
        return bugReportRepository.findById(id).orElse(null);
    }

    public Boolean deleteBugReportById(Integer id) {
        if (bugReportRepository.existsById(id)){
            bugReportRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }
}
