package com.ujm.xmltech.sockets;

import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.File;
import java.io.IOException;
 
/**
 * Class to manage a sender file thread
 * 
 * @author UJM's students
 */
public class FileSender extends Thread {
 
    private ServerSocket socketListener;
    private Socket socket;
    private File fileToSend;
    private InetAddress ipAddress;

    public FileSender(File fileToSend) {
        this.fileToSend = fileToSend;
        try{
            socketListener = new ServerSocket(15000);
            ipAddress = socketListener.getInetAddress();
        }
        catch(IOException e){
            System.out.println("Impossible to listen on this port");
        }
        
        start();
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void run() {
        try{
            socket = socketListener.accept();
            new FileTransfer(socket, fileToSend);
            socket.close();
            socketListener.close();
        } catch(IOException e) {
            System.out.println("Erreur d IO");
        }
    }
}