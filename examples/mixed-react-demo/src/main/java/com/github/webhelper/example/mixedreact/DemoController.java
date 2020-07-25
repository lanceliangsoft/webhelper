package com.github.webhelper.example.mixedreact;

import com.github.webhelper.example.mixedreact.model.ProcessStat;
import com.github.webhelper.example.mixedreact.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {
    @Autowired
    private ProcessService processService;

    @GetMapping("/proc")
    public List<ProcessStat> getProcesses() {
        return processService.getProcesses();
    }

    @GetMapping("/his/{pid}")
    public List<ProcessStat> getHistory(@PathVariable(name = "pid") int pid) {
        return processService.getHistory(pid);
    }
}
