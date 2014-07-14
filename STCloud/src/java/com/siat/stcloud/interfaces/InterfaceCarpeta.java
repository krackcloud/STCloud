/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.interfaces;

import com.siat.stcloud.pojos.Carpeta;
import com.siat.stcloud.pojos.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KRACK
 */
public interface InterfaceCarpeta {
    
    public boolean createCarpeta(Carpeta carpeta, Session session) throws Exception;
    public Carpeta getCarpetaByNombre(String nombreCarpeta,Usuario owner, Session session)throws Exception;
    public List<Carpeta> list(Usuario usuario, Session session) throws Exception;
    public void updateCarpeta(Carpeta carpeta, Session session)throws Exception;
    public void deleteCarpeta(Carpeta carpeta, Session session)throws Exception;
    
}
