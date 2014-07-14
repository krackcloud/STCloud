/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.beans.request;

import com.siat.stcloud.dao.DAOArchivo;
import com.siat.stcloud.pojos.Archivo;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KRACK
 */
public class RFileBean implements Serializable{
    private Archivo archivo;
    private List<Archivo> listaArchivos;
    private String nombreArchivo;
    private Session session;
    private Transaction transaccion;
    private List<Archivo> usuariosFiltrados;
    private DAOArchivo dao;

    /**
     * Creates a new instance of RFileBean
     */
    public RFileBean() {
        dao = new DAOArchivo();
    }

    /**
     * @return the archivo
     */
    public Archivo getArchivo() {
        return archivo;
    }

    /**
     * @param archivo the archivo to set
     */
    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    /**
     * @return the listaArchivos
     */
    public List<Archivo> getListaArchivos() {
        return listaArchivos;
    }

    /**
     * @param listaArchivos the listaArchivos to set
     */
    public void setListaArchivos(List<Archivo> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * @return the usuariosFiltrados
     */
    public List<Archivo> getUsuariosFiltrados() {
        return usuariosFiltrados;
    }

    /**
     * @param usuariosFiltrados the usuariosFiltrados to set
     */
    public void setUsuariosFiltrados(List<Archivo> usuariosFiltrados) {
        this.usuariosFiltrados = usuariosFiltrados;
    }    
}
