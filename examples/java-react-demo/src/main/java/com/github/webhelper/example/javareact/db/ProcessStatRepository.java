package com.github.webhelper.example.javareact.db;

import com.github.webhelper.example.javareact.model.ProcessStat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProcessStatRepository extends CrudRepository<ProcessStat, Long> {
    List<ProcessStat> findByPid(int pid);
}
