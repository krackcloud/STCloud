/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author KRACK
 */
@ManagedBean(name = "user1")
@RequestScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String user;
    private String password;
    private String nameDirectory;
    private String serverName;
    //Variables para el manejo de ambito del usuario
    private final HttpServletRequest request;
    private final FacesContext contexto;
    private FacesMessage mensaje;

    public UserBean() {
        contexto = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) contexto.getExternalContext().getRequest();
    }

    public String login() {
        if (this.user.equals("krackinator") && this.password.equals("Krack4564")) {
            request.getSession().setAttribute("sesionUsuario", this.user);
            this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso Correcto", null);
            contexto.addMessage(null, mensaje);
            return "index";
        } else {
            this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acceso Denegado", null);
            contexto.addMessage(null, mensaje);
            return "login";
        }
    }

    public String logout() {
        request.getSession().removeAttribute("sesionUsuario");
        this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usted ha cerrado sesión", null);
        contexto.addMessage(null, mensaje);
        return "login";
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the nameDirectory
     */
    public String getNameDirectory() {
        return nameDirectory;
    }

    /**
     * @param nameDirectory the nameDirectory to set
     */
    public void setNameDirectory(String nameDirectory) {
        this.nameDirectory = nameDirectory;
    }
}
