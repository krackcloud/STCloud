/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.beans.request;

import com.siat.stcloud.dao.DAOUsuario;
import com.siat.stcloud.hibernate.HibernateUtil;
import com.siat.stcloud.net.Encrypt;
import com.siat.stcloud.pojos.Usuario;
import java.io.File;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KRACK
 */
public class RUsuarioBean implements Serializable {

    /**
     * Creates a new instance of RUsuarioBean
     */
    private Usuario usuario;
    private String password2;
    private Session session;
    private Transaction transaccion;

    public RUsuarioBean() {
        this.usuario = new Usuario();
        this.usuario.setIdUsuario(null);
        this.usuario.setUsrPrivilegios("USUARIO");
    }

    private void showMessage(String titulo, String detalle, Severity severidad) {
        FacesContext fc = FacesContext.getCurrentInstance();
        FacesMessage mensaje = new FacesMessage(severidad, titulo, detalle);
        fc.addMessage(null, mensaje);
    }

    public void registrar() throws Exception {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            if (!this.getUsuario().getUsrContrasenia().equals(this.password2)) {
                this.showMessage("Las contraseñas no coinciden", "Las contraseñas no coinciden", FacesMessage.SEVERITY_ERROR);
                return;
            }
            String tempUser = this.getUsuario().getUsrUsuario().trim().toUpperCase();
            this.getUsuario().setUsrUsuario(tempUser);
            DAOUsuario user = new DAOUsuario();
            if (user.getByUsuario(session, this.usuario.getUsrUsuario()) != null) {
                this.showMessage("El usuario ya existe", "El usuario ya existe", FacesMessage.SEVERITY_ERROR);
                this.usuario.setUsrUsuario(null);
                return;
            }
            String tempName = this.getUsuario().getUsrNombre().trim().replace("Ã±", "Ñ").toUpperCase();

            this.getUsuario().setUsrNombre(tempName);
            this.getUsuario().setUsrContrasenia(Encrypt.sha512(this.getUsuario().getUsrContrasenia()));
            user.register(this.session, this.usuario);
            this.transaccion.commit();
            File f = new File("C:\\Servers\\FileZillaClients\\DIR_KRACKINATOR\\DIR_" + this.getUsuario().getUsrUsuario());
            f.mkdir();
            this.usuario = new Usuario();
            System.gc();
            this.showMessage("El usuario ha sido registrado exitosamente", "El usuario ha sido registrado exitosamente", FacesMessage.SEVERITY_INFO);

        } catch (Exception err) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            this.showMessage(err.getMessage(), err.getMessage(), FacesMessage.SEVERITY_FATAL);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the password2
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * @param password2 the password2 to set
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
