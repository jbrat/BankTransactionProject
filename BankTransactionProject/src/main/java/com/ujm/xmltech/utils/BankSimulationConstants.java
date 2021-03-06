package com.ujm.xmltech.utils;

public class BankSimulationConstants {

  /**
   * Directory where are files to process
   */
  public final static String IN_DIRECTORY = "/home/julien/Documents/Master/DocumentNumerique/BankSimulator/folders/in/";
  /**
   * Directory where are reports
   */
  public final static String OUT_DIRECTORY = "/home/julien/Documents/Master/DocumentNumerique/BankSimulator/folders/out/";
  /**
   * Directory where are files under process
   */
  public final static String WORK_DIRECTORY = "/home/julien/Documents/Master/DocumentNumerique/BankSimulator/folders/work/";
  /**
   * Directory where are files already processed
   */
  public final static String ARCHIVE_DIRECTORY = "/home/julien/Documents/Master/DocumentNumerique/BankSimulator/folders/archive/";

  /**
   * must contain only 4 upper case letters. Real example : BNPP
   */
  public static final String MY_BANK_IDENTIFIER = "JAKY";

  /**
   * persistence unit name in the spring configuration
   */
  public static final String PERSISTENCE_UNIT = "bank-unit";
  /**
   * name of the transaction manager
   */
  public static final String TRANSACTION_MANAGER = "bankTransactionManager";

}
