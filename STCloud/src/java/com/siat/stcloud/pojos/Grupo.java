package com.siat.stcloud.pojos;
// Generated 15-jun-2014 1:34:04 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Grupo generated by hbm2java
 */
public class Grupo  implements java.io.Serializable {


     private Integer idGrupo;
     private Usuario usuario;
     private String grpNombre;
     private String grpDescripcion;
     private Date grpFechaCreacion;
     private Set carpetas = new HashSet(0);
     private Set usuarioGrupos = new HashSet(0);

    public Grupo() {
    }

	
    public Grupo(String grpNombre, Date grpFechaCreacion) {
        this.grpNombre = grpNombre;
        this.grpFechaCreacion = grpFechaCreacion;
    }
    public Grupo(Usuario usuario, String grpNombre, String grpDescripcion, Date grpFechaCreacion, Set carpetas, Set usuarioGrupos) {
       this.usuario = usuario;
       this.grpNombre = grpNombre;
       this.grpDescripcion = grpDescripcion;
       this.grpFechaCreacion = grpFechaCreacion;
       this.carpetas = carpetas;
       this.usuarioGrupos = usuarioGrupos;
    }
   
    public Integer getIdGrupo() {
        return this.idGrupo;
    }
    
    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getGrpNombre() {
        return this.grpNombre;
    }
    
    public void setGrpNombre(String grpNombre) {
        this.grpNombre = grpNombre;
    }
    public String getGrpDescripcion() {
        return this.grpDescripcion;
    }
    
    public void setGrpDescripcion(String grpDescripcion) {
        this.grpDescripcion = grpDescripcion;
    }
    public Date getGrpFechaCreacion() {
        return this.grpFechaCreacion;
    }
    
    public void setGrpFechaCreacion(Date grpFechaCreacion) {
        this.grpFechaCreacion = grpFechaCreacion;
    }
    public Set getCarpetas() {
        return this.carpetas;
    }
    
    public void setCarpetas(Set carpetas) {
        this.carpetas = carpetas;
    }
    public Set getUsuarioGrupos() {
        return this.usuarioGrupos;
    }
    
    public void setUsuarioGrupos(Set usuarioGrupos) {
        this.usuarioGrupos = usuarioGrupos;
    }




}


