package com.github.webhelper.example.javareact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProcessStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int pid;

    private String processName;

    private String user;

    private double cpu;

    private double mem;

    private Date time;
}
