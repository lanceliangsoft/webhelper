package com.github.webhelper.core.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class IOUtilTest {
    @Test
    public void testRelativePath() {
        assertEquals("p1/p2/file.ext",
            IOUtil.relativePath(new File("/home/user/project/src/main/typescript/p1/p2/file.ext"),
                new File("/home/user/project/src/main/typescript")));
    }
}
