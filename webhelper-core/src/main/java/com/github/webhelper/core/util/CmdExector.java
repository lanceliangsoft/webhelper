package com.github.webhelper.core.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CmdExector {
    private File dir;

    public CmdExector setDir(File dir) {
        this.dir = dir;
        return this;
    }

    public int exec(String... command) throws IOException, InterruptedException {
        return exec(Arrays.asList(command));
    }

    public int exec(List<String> command) throws IOException, InterruptedException {
        System.out.println(command.stream().collect(Collectors.joining(" ")));
        Process process = new ProcessBuilder(command)
            .directory(dir)
            .start();

        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int retCode = process.waitFor();
        return retCode;
    }
}
