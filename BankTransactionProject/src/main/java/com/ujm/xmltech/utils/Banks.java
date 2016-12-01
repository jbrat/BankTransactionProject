package com.ujm.xmltech.utils;

/**
 * list of banks managed by the other groups (must contains 4 letters for each one)
 * 
 */
public enum Banks {

    BKNK, 
    UDEV,
    BRSC,
    NYCU, 
    ARMA, 
    KAMY;
    
    /**
     * Method to check if bank exist in the enum list
     * 
     * @param name
     * 
     * @return boolean
     */
    public static boolean checkIfBankExist(String name){

        for(Banks b: Banks.values()) {
            if (name.equals(b.name())) {
                return true;
            }
        }
        return false;
    }

}
