package com.ujm.xmltech.utils;

import java.io.File;

public class FileManagementUtils {

    public static File retrieveFileToProcess() {
        File toReturn = null;
        File folder = new File(BankSimulationConstants.IN_DIRECTORY);
        System.out.println(folder.getPath());
        for (File file : folder.listFiles()) {
            toReturn = file;
        }
        System.out.println("totot");
        return toReturn;
    }
}
