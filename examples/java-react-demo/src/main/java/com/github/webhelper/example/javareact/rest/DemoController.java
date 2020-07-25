package com.github.webhelper.example.javareact.rest;

import com.github.webhelper.example.javareact.model.ProcessStat;
import com.github.webhelper.example.javareact.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {
    @Autowired
    private ProcessService processService;

    @GetMapping("/proc")
    public Iterable<ProcessStat> getProcesses() {
        return processService.getProcesses();
    }

    @GetMapping(path = "/his/{pid}")
    public Iterable<ProcessStat> getHistory(@PathVariable(name = "pid") int pid) {
        return processService.getHistory(pid);
    }

    @GetMapping("/his")
    public Iterable<ProcessStat> getHistory() {
        return processService.getAllHistory();
    }
}
