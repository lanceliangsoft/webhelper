package com.github.webhelper.example.javareact.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PsUtil {
    public static BufferedReader exec(String... command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
            .start();

        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
}
