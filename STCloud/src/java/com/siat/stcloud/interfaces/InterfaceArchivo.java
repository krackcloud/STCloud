/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.interfaces;

import com.siat.stcloud.pojos.Archivo;
import com.siat.stcloud.pojos.Carpeta;
import com.siat.stcloud.pojos.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KRACK
 */
public interface InterfaceArchivo {
    
    public void createFile(Archivo archivo, Session session) throws Exception;
    public Archivo getFileByName(String nombreArchivo, Session session)throws Exception;
    public List<Archivo> list(Session session, Carpeta carpeta)throws Exception;
    public void deleteFile(Archivo archivo, Session session) throws Exception;
    public void updateFile(Archivo toReplace, Session session)throws Exception;
}
