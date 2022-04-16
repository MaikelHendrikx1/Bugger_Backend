package com.buggerpage.buggerpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuggerPageService {
    @Autowired
    private BuggerPageRepository buggerPageRepository;

    public BuggerPage getBugReportbyId(Integer pageId) {
        return buggerPageRepository.findById(pageId).orElseThrow();
    }

    public Iterable<BuggerPage> getAll(String filter, Integer amount) {
        if (filter == null){
            return buggerPageRepository.getAll(amount);
        }
        else{
            return buggerPageRepository.getAll(amount, filter);
        }
    }    
}
