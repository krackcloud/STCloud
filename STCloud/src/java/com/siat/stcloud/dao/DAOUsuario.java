/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.dao;

import com.siat.stcloud.interfaces.InterfaceUsuario;
import com.siat.stcloud.pojos.Usuario;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KRACK
 */
public class DAOUsuario implements InterfaceUsuario {
    

    @Override
    public boolean register(Session session,Usuario usuario) throws Exception {
        try{
        session.save(usuario);
        return true;
        }catch(Exception err){
            System.out.println(err.getMessage());
        }
        return false;
    }

    @Override
    public List<Usuario> list(Session session) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario getUsuario(Session session,String idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Session session,Usuario usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario getByUsuario(Session session, String userName) throws Exception {
        String hql = "from Usuario where usrUsuario=:userName";
        Query query = session.createQuery(hql);
        query.setParameter("userName", userName);
        Usuario user = (Usuario) query.uniqueResult();
        return user;
    }

    @Override
    public boolean delete(Session session,int idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
