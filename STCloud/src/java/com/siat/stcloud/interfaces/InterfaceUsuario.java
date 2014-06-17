/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.interfaces;

import com.siat.stcloud.pojos.Usuario;
import java.util.List;

/**
 *
 * @author KRACK
 */
public interface InterfaceUsuario {
    public boolean register(Usuario usuario) throws Exception;
    public List<Usuario> list() throws Exception;
    public Usuario getUsuario(String idUsuario) throws Exception;
    public boolean update(Usuario usuario) throws Exception;
    public boolean delete (int idUsuario) throws Exception;
    
}
