package com.ujm.xmltech.sockets;

import java.net.InetAddress;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
 
/**
 * Class to receive file from socket
 * 
 * @author UJM's students
 */
public class FileReceiver extends Thread {
 
    private Socket socket;
    private DataInputStream dataIn;
    private FileOutputStream fos;
    private File file;

    /**
     * Method to receive a file from an IP address
     * 
     * @param ipAddress
     * @param path 
     */
    public FileReceiver(InetAddress ipAddress, String path) {
        int i;
        
        try{
            socket = new Socket(ipAddress,15000);
            dataIn = new DataInputStream(socket.getInputStream());

            file = new File(path);
            fos = new FileOutputStream(file);
            while((i = dataIn.read())!= -1) {
                fos.write(i);
            }
            socket.close();
        }
        catch(IOException e){
            System.out.println("Erreur d IO");
        }
    }	
}