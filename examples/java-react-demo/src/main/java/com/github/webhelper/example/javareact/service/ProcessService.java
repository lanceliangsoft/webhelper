package com.github.webhelper.example.javareact.service;

import com.github.webhelper.example.javareact.db.ProcessStatRepository;
import com.github.webhelper.example.javareact.model.ProcessStat;
import com.github.webhelper.example.javareact.util.PsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ProcessService {
  @Autowired private ProcessStatRepository repository;

  public Iterable<ProcessStat> getProcesses() {
    try (BufferedReader reader = PsUtil.exec("ps", "-eo", "pid,user,%cpu,%mem,comm")) {
      List<ProcessStat> processes = new ArrayList<>();
      Date now = new Date();
      String line;
      while ((line = reader.readLine()) != null) {
        String[] items = line.split("\\s+");
        if (items.length == 5 && items[0].matches("\\d+")) {
          ProcessStat ps =             ProcessStat.builder()
                  .time(now)
                  .pid(Integer.parseInt(items[0]))
                  .user(items[1])
                  .cpu(Double.parseDouble(items[2]))
                  .mem(Double.parseDouble(items[3]))
                  .processName(items[4])
                  .build();
          repository.save(ps);
          processes.add(ps);
        }
      }
      log.info("processes={}", processes.size());
      //repository.saveAll(processes);
      return processes;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new ApplicationException("Fail to get processes list", ex);
    }
  }

  public Iterable<ProcessStat> getHistory(int pid) {
    List<ProcessStat> his = repository.findByPid(pid);
    his.forEach(h -> System.out.println("" + h));
    return his;
  }

  public Iterable<ProcessStat> getAllHistory() {
    return repository.findAll();
  }
}
