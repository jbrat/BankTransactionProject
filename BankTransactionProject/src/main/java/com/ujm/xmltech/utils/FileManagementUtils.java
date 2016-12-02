package com.ujm.xmltech.utils;

import java.io.File;

/**
 * File management to watch in a folder
 * 
 * @author UJM's students
 */
public class FileManagementUtils {

    /**
     * Method to return file of the IN_DIRECTORY
     * 
     * @return File inputFile
     */
    public static File retrieveFileToProcess() {
        File toReturn = null;
        File folder = new File(BankSimulationConstants.IN_DIRECTORY);
        for (File file : folder.listFiles()) {
            toReturn = file;
        }
        
        return toReturn;
    }
}
