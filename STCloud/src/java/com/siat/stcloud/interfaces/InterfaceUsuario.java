/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.interfaces;

import com.siat.stcloud.pojos.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KRACK
 */
public interface InterfaceUsuario {
    public boolean register(Session session,Usuario usuario) throws Exception;
    public List<Usuario> list(Session session ) throws Exception;
    public Usuario getUsuario(Session session, String idUsuario) throws Exception;
    public boolean update(Session session,Usuario usuario) throws Exception;
    public Usuario getByUsuario(Session session,String userName) throws Exception;
    public boolean delete (Session session, int idUsuario) throws Exception;
    
}
