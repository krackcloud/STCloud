/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.dao;

import com.siat.stcloud.hibernate.HibernateUtil;
import com.siat.stcloud.interfaces.InterfaceUsuario;
import com.siat.stcloud.pojos.Usuario;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KRACK
 */
public class DAOUsuario implements InterfaceUsuario {
    
    private Session session;

    @Override
    public boolean register(Usuario usuario) throws Exception {
        try{
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaccion = session.beginTransaction();
        session.save(usuario);
        transaccion.commit();
        session.close();
        return true;
        }catch(Exception err){
            System.out.println(err.getMessage());
        }
        return false;
    }

    @Override
    public List<Usuario> list() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario getUsuario(String idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Usuario usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(int idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
