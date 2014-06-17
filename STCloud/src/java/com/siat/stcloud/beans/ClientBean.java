/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.beans;

import javax.faces.bean.ManagedBean;

import com.siat.stcloud.net.FTPConnector;
import java.io.Serializable;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author KRACK
 */
@ManagedBean(name = "client")
@RequestScoped
public class ClientBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private FTPConnector conector;

    public ClientBean() {
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

    public void login() {
            conector=new FTPConnector("siat-soluciones.sytes.net", this.username, this.password, "fake");
        try {
            System.out.println(conector.estabilishConnection() ? "El usuario ha accedido" : "El usuario no ha accedido");
            System.out.println(conector.showDirectories());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
