package com.signupfacebook.Newlife_project_1.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Test {

    public static void main(String[] args) {
        File fileOrDir = new File("C:\\Users\\newlife_pc20\\.hidemyacc\\profiles");
        Test myFiles = new Test();
        myFiles.traverseDepthFiles(fileOrDir);
    }

    public void traverseDepthFiles(final File fileOrDir) {
        // check xem fileOrDir la file hay foder
        if (fileOrDir.isDirectory()) {
            // in ten folder ra man hinh
//            System.out.println(fileOrDir.getAbsolutePath());

            final File[] children = fileOrDir.listFiles();
            if (children == null) {
                return;
            }
            for(File file : children) {
                String fileName = fileOrDir.getAbsolutePath() + file.getAbsolutePath();
                System.out.println(fileName.replace('\\', '/'));
            }
        }
    }
}
