/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.beans.request;

import com.siat.stcloud.dao.DAOUsuario;
import com.siat.stcloud.pojos.Usuario;
import java.util.List;

/**
 *
 * @author KRACK
 */
public class RUsuarioBean {

    /**
     * Creates a new instance of RUsuarioBean
     */
    
    private Usuario usuario;
    private List<Usuario> listaUsuarios;
    private String password2;
    
    public RUsuarioBean() {
        this.usuario=new Usuario();
        this.usuario.setIdUsuario(null);
        this.usuario.setUsrPrivilegios("USUARIO");        
    }

    public String registrar(){
        DAOUsuario user = new DAOUsuario();
        try{
        user.register(this.usuario);
        }
        catch(Exception err){
            System.out.println(err.getMessage());
        }
        return "Registro";
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
     * @return the listaUsuarios
     */
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
     * @param listaUsuarios the listaUsuarios to set
     */
    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
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
