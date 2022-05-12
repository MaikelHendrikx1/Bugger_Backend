package com.buggerpage.buggerpage;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/BuggerPages")
public class BuggerPageController {
    @Autowired
    BuggerPageService buggerPageService;

    @GetMapping(path = "/get")
    public @ResponseBody BuggerPage getBuggerPage(@RequestParam Integer pageId) {
        return buggerPageService.getBugReportbyId(pageId);
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<BuggerPage> getBuggerPages(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "5") Integer amount) {
        return buggerPageService.getAll(filter, amount);
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public @ResponseBody String addBuggerPage(@RequestBody BuggerPage bp){
        return buggerPageService.addBuggerPage(bp);
    }

    @GetMapping(path = "/allByUser")
    public @ResponseBody Iterable<BuggerPage> getPagesByUserId(@RequestParam Integer userID){
        return buggerPageService.getAllByUser(userID);
    }

    @PostMapping(path = "/addMaintainers")
    public @ResponseBody void addMaintainers(@RequestBody Set<Integer> userIds, @RequestParam Integer pageId){
        buggerPageService.addMaintainers(userIds, pageId);
    }

    @PostMapping(path = "/deleteMaintainers")
    public @ResponseBody void deleteMaintainers(@RequestBody Set<Integer> userIds, @RequestParam Integer pageId) {
        buggerPageService.deleteMaintainers(userIds, pageId);
    }
}
