/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.dao;

import com.siat.stcloud.interfaces.InterfaceCarpeta;
import com.siat.stcloud.pojos.Carpeta;
import com.siat.stcloud.pojos.Usuario;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KRACK
 */
public class DAOCarpeta implements InterfaceCarpeta {

    @Override
    public boolean createCarpeta(Carpeta carpeta, Session session) throws Exception {

        try {
            session.save(carpeta);
            return true;
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        return false;
    }

    @Override
    public Carpeta getCarpetaByNombre(String nombreCarpeta,Usuario usuario, Session session) throws Exception {
        String hql = "from Carpeta where cptNombre=:nombreCarpeta and usuario=:usuario";
        Query consulta = session.createQuery(hql);
        consulta.setParameter("nombreCarpeta", nombreCarpeta);
        consulta.setParameter("usuario", usuario);
        return (Carpeta) consulta.uniqueResult();
    }

    @Override
    public List<Carpeta> list(Usuario usuario, Session session) throws Exception {
        String hql = "from Carpeta where usuario=:usuario  order by cptFechaCreacion desc";
        Query consulta = session.createQuery(hql);
        consulta.setParameter("usuario", usuario);
        return (List<Carpeta>) consulta.list();
    }

    @Override
    public void updateCarpeta(Carpeta carpeta, Session session) throws Exception {
        session.saveOrUpdate(carpeta);
    }

    @Override
    public void deleteCarpeta(Carpeta carpeta, Session session) throws Exception {
        session.delete(carpeta);
    }
}
