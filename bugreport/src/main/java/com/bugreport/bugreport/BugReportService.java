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

    public void deleteBugReportById(Integer id) {
        bugReportRepository.deleteById(id);
    }
}
