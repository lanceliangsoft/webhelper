package com.github.webhelper.example.mixedreact.service;

import com.github.webhelper.example.mixedreact.db.ProcessStatRepository;
import com.github.webhelper.example.mixedreact.model.ProcessStat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProcessService {
    @Autowired
    private ProcessStatRepository repository;

    public List<ProcessStat> getProcesses() {
        return new ArrayList<>();
    }

    public List<ProcessStat> getHistory(int pid) {
        return repository.findByPid(pid);
    }
}
