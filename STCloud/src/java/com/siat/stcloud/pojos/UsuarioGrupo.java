package com.siat.stcloud.pojos;
// Generated 15-jun-2014 1:34:04 by Hibernate Tools 3.2.1.GA



/**
 * UsuarioGrupo generated by hbm2java
 */
public class UsuarioGrupo  implements java.io.Serializable {


     private Integer idLogUnion;
     private Grupo grupo;
     private Usuario usuario;
     private String log;

    public UsuarioGrupo() {
    }

    public UsuarioGrupo(Grupo grupo, Usuario usuario, String log) {
       this.grupo = grupo;
       this.usuario = usuario;
       this.log = log;
    }
   
    public Integer getIdLogUnion() {
        return this.idLogUnion;
    }
    
    public void setIdLogUnion(Integer idLogUnion) {
        this.idLogUnion = idLogUnion;
    }
    public Grupo getGrupo() {
        return this.grupo;
    }
    
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getLog() {
        return this.log;
    }
    
    public void setLog(String log) {
        this.log = log;
    }




}


