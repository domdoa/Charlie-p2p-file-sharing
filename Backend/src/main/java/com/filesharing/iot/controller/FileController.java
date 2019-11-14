package com.filesharing.iot.controller;/* Java Program Example - Merge Two Files */

import java.io.*;
import java.util.Scanner;

public class FileController
{
    public void join(String FilePath) {
        long leninfile=0, leng=0;
        int count=1, data=0;
        try {
            File filename = new File(FilePath);
            RandomAccessFile outfile = new RandomAccessFile(filename,"rw");
            while(true) {
                filename = new File(FilePath + count + ".sp");
                if (filename.exists()) {
                    RandomAccessFile infile = new RandomAccessFile(filename,"r");
                    data=infile.read();
                    while(data != -1) {
                        outfile.write(data);
                        data=infile.read();
                    }
                    leng++;
                    infile.close();
                    count++;
                } else break;
            }
            outfile.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
//    public static void main(String args[])
//    {
//
//        String srcy, srcz, merge;
//        Scanner scan = new Scanner(System.in);
//
//        /* enter the file names with extension like file.txt */
//
//        System.out.print("Enter First File Name : ");
//        srcy = scan.nextLine();
//        System.out.print("Enter Second File Name : ");
//        srcz = scan.nextLine();
//
//        System.out.print("Enter FileName to Store merged content of First and Second File : ");
//        merge = scan.nextLine();
//
//        File[] files = new File[2];
//        files[0] = new File(srcy);
//        files[1] = new File(srcz);
//
//        File mergedFile = new File(merge);
//
//        mergeFiles(files, mergedFile);
//
//    }
//
//    public static void mergeFiles(File[] files, File mergedFile)
//    {
//        FileWriter fstream = null;
//        BufferedWriter out = null;
//
//        try
//        {
//            fstream = new FileWriter(mergedFile, true);
//            out = new BufferedWriter(fstream);
//        }
//        catch(IOException e1)
//        {
//            e1.printStackTrace();
//        }
//
//        System.out.print("Merging Both File...\n");
//
//        for(File f : files)
//        {
//            FileInputStream fis;
//            try
//            {
//                fis = new FileInputStream(f);
//                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
//
//                String aLine;
//                while((aLine = in.readLine()) != null)
//                {
//                    out.write(aLine);
//                    out.newLine();
//                }
//
//                in.close();
//            }
//            catch(IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        System.out.print("\nMerged Successfully..!!");
//
//        try
//        {
//            out.close();
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
}