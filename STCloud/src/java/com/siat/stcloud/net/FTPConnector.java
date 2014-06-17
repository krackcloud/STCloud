/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author KRACK
 * @version 1.0
 *
 * Provee los medios de conexion necesarios para acceder a un servidor ftp.
 */
public class FTPConnector {

    private String server;
    private String username;
    private String password;
    private String remoteDirectory;
    private String localRoot;
    private FTPClient cliente;

    public FTPConnector(String server, String username, String password, String remoteDirectory) {
        cliente = new FTPClient();
        this.server = server;
        this.username = username;
        this.password = password;
        this.remoteDirectory = remoteDirectory;
    }

    /**
     * @return Si la conexión fue establecida o no
     */
    public boolean estabilishConnection() throws SocketException, IOException {
        boolean isConected=false;
        cliente.connect(this.server);
        isConected = cliente.login(username, password);
        cliente.changeWorkingDirectory(remoteDirectory);
        return isConected;
        
    }

    public String[] getDirectories(String remote) {
        String[] result = {};
        try {
            FTPFile[] directorios = this.cliente.listDirectories(remote);
            result = new String[directorios.length];
            for (int i = 0; i < directorios.length; i++) {
                result[i]=directorios[i].getName();
            }

        } catch (Exception err) {
            System.out.println(err.getMessage());
        }


        return result;

    }

    public String showDirectories() {
        String valores = "_root/";
        try {
            //Con esto podemos cambiar la carpeta actual
            cliente.changeWorkingDirectory("/Musica/Megadeth");
            cliente.makeDirectory("Killing Is My Business");
            System.out.println("La ruta actual es: " + cliente.printWorkingDirectory());
            //Con esto podemos obtener los directorios de la carpeta actual
            FTPFile[] directorios = cliente.listDirectories();
            for (int i = 0; i < directorios.length; i++) {
                valores += directorios[i].getName() + "/";
            }
            //Con esto podemos volver al directorio raíz
            cliente.changeToParentDirectory();
            System.out.println("La ruta nueva es es: " + cliente.printWorkingDirectory());

        } catch (IOException err) {
            valores = "El error es: " + err.getMessage();
        }
        return valores;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the localRoot
     */
    public String getLocalRoot() {
        return localRoot;
    }

    /**
     * @param localRoot the localRoot to set
     */
    public void setLocalRoot(String localRoot) {
        this.localRoot = localRoot;
    }

    /**
     * @return the cliente
     */
    public FTPClient getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(FTPClient cliente) {
        this.cliente = cliente;
    }

    public void setRemoteDirectory(String remoteDirectory) {
        this.remoteDirectory = remoteDirectory;
    }

    public String getRemoteDirectory() {
        return this.remoteDirectory;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
