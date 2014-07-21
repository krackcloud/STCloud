/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.dao;

import com.siat.stcloud.interfaces.InterfaceArchivo;
import com.siat.stcloud.pojos.Archivo;
import com.siat.stcloud.pojos.Carpeta;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KRACK
 */
public class DAOArchivo implements InterfaceArchivo {

    @Override
    public void createFile(Archivo archivo, Session session) throws Exception {
        try {
            session.save(archivo);
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    @Override
    public Archivo getFileByName(String nombreArchivo, Carpeta carpeta, Session session) throws Exception {
        String hql = "from Archivo where nombre=:nombreArchivo and carpeta=:carpeta";
        Query query = session.createQuery(hql);
        query.setParameter("nombreArchivo", nombreArchivo);
        query.setParameter("carpeta", carpeta);
        Archivo archivo = (Archivo) query.uniqueResult();
        return archivo;
    }

    @Override
    public List<Archivo> list(Session session, Carpeta carpeta) throws Exception {
        String hql = "from Archivo where carpeta=:carpeta";
        Query consulta = session.createQuery(hql);
        consulta.setParameter("carpeta", carpeta);
        List<Archivo> lista = (List<Archivo>) consulta.list();
        return lista;
    }

    @Override
    public void deleteFile(Archivo archivo, Session session) throws Exception {
        session.delete(archivo);
    }

    @Override
    public void updateFile(Archivo toReplace, Session session) throws Exception {
        session.saveOrUpdate(toReplace);
    }
}
