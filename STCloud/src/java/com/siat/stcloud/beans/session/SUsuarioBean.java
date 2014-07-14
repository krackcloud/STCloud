/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.beans.session;

import com.siat.stcloud.dao.DAOArchivo;
import com.siat.stcloud.dao.DAOCarpeta;
import com.siat.stcloud.dao.DAOUsuario;
import com.siat.stcloud.hibernate.HibernateUtil;
import com.siat.stcloud.net.Encrypt;
import com.siat.stcloud.pojos.Archivo;
import com.siat.stcloud.pojos.Carpeta;
import com.siat.stcloud.pojos.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author KRACK
 */
public class SUsuarioBean implements Serializable {

    /**
     * Creates a new instance of SUsuarioBean
     */
    private String usuario;
    private String contrasenia;
    private Usuario user;
    private Session session;
    private Transaction transaccion;
    /*Manejo de las carpetas del usuario*/
    private DAOCarpeta daoCarpeta;
    private List<Carpeta> carpetasUsuarios;
    private List<Carpeta> carpetaSeleccion;
    private Carpeta carpeta;
    private Carpeta addCarpeta;
    /*Manejo de los archivos del usuario*/
    private DAOArchivo daoArchivo;
    private List<Archivo> archivosUsuarios;
    private List<Archivo> archivoSeleccion;
    private Archivo archivo;
    private Archivo addArchivo;
    

    public SUsuarioBean() {
        HttpSession miSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        miSession.setMaxInactiveInterval(600);
        this.daoArchivo = new DAOArchivo();
        this.daoCarpeta = new DAOCarpeta();
        this.daoArchivo = new DAOArchivo();
        this.addCarpeta = new Carpeta();
        this.addArchivo = new Archivo();
    }

    public void onReload() {
        this.archivo = null;
        System.gc();
    }

    public String login() {
        this.session = null;
        this.transaccion = null;
        try {
            DAOUsuario daoUsuario = new DAOUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            this.user = daoUsuario.getByUsuario(session, this.usuario.toUpperCase());
            if (user != null) {
                if (user.getUsrContrasenia().equals(Encrypt.sha512(this.contrasenia))) {
                    HttpSession sesionWeb = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    sesionWeb.setAttribute("usuario", this.usuario);
                    return "index";
                }
            }
            this.transaccion.commit();
            this.usuario = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ups! parece que olvidaste algo", "Verifica tu usuario y contraseña"));
        } catch (Exception err) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un problema. Contacte a su administrador", "Error de tipo :" + err.getMessage()));
        } finally {
            if (this.session != null) {
                session.close();
            }
        }
        return "login";
    }

    public String closeSession() {
        this.usuario = null;
        this.contrasenia = null;
        this.carpeta = null;
        HttpSession sesionWeb = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sesionWeb.invalidate();
        return "login";
    }

    public void createNewFolder() {
        DAOCarpeta folderDAO;
        try {
            folderDAO = new DAOCarpeta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            Carpeta folder = folderDAO.getCarpetaByNombre(this.addCarpeta.getCptNombre(), this.user, this.session);
            if (folder != null) {
                this.showMessage("La carpeta ya existe", "Puedes modificar desde las opciones de la derecha", FacesMessage.SEVERITY_WARN);
                this.transaccion.rollback();
                return;
            }
            this.addCarpeta.setArchivos(null);
            this.addCarpeta.setCptFechaCreacion(new Date());
            this.addCarpeta.setArchivos(null);
            this.addCarpeta.setCptRuta("C:\\Servers\\FileZillaClients\\DIR_KRACKINATOR\\DIR_" + this.user.getUsrUsuario().toUpperCase() + "\\" + this.addCarpeta.getCptNombre());
            this.addCarpeta.setIdCarpeta(null);
            this.addCarpeta.setGrupo(null);
            this.addCarpeta.setUsuario(this.user);
            folderDAO.createCarpeta(this.addCarpeta, this.session);
            this.showMessage("Carpeta creada", "El directorio se ha creado correctamente", FacesMessage.SEVERITY_INFO);
            this.transaccion.commit();
            File f = new File(this.addCarpeta.getCptRuta());
            f.mkdir();
            this.addCarpeta = new Carpeta();

        } catch (Exception err) {
            this.showMessage("Ha ocurrido un problema. Contacte a su administrador", "Error de tipo: " + err.getMessage(), FacesMessage.SEVERITY_FATAL);
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        if (this.carpeta == null) {
            this.showMessage("Selecciona una carpeta", "Clica sobre la carpeta en que deseas guardar", FacesMessage.SEVERITY_WARN);
            return;
        }
        if (uploadFile(event.getFile())) {
            this.showMessage("Envio exitoso", "El archivo " + event.getFile().getFileName() + " se ha subido correctamente", FacesMessage.SEVERITY_INFO);
        } else {
            this.showMessage("El archivo no es compatible con este sistema", "Verifica que tu archivo no este dañado", FacesMessage.SEVERITY_WARN);
        }

    }

    private boolean uploadFile(UploadedFile f) {
        OutputStream salida;
        InputStream entrada;
        String nombre;
        try {
            nombre = f.getFileName();
            String path = this.carpeta.getCptRuta() + "\\" + nombre;
            this.addArchivo.setIdArchivo(null);
            this.addArchivo.setFechaCreacion(new Date());
            this.addArchivo.setNombre(nombre);
            this.addArchivo.setCarpeta(this.carpeta);
            this.addArchivo.setRuta(path);
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            Archivo temp = daoArchivo.getFileByName(nombre, this.carpeta, this.session);
            if (temp != null) {
                this.showMessage("El archivo ya existe", "Este archivo ya existe. Si deseas modificarlo clica sobre el archivo existente", FacesMessage.SEVERITY_WARN);
                this.transaccion.rollback();
                return false;
            }
            this.daoArchivo.createFile(this.addArchivo, this.session);
            entrada = f.getInputstream();
            salida = new FileOutputStream(new File(path));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = entrada.read(bytes)) != -1) {
                salida.write(bytes, 0, read);
            }
            entrada.close();
            salida.close();
            this.transaccion.commit();
            this.addArchivo = new Archivo();
            return true;
        } catch (Exception err) {
            this.showMessage("Ha ocurrido un problema. Contacta con tu administrador", "El error es: " + err.getMessage(), FacesMessage.SEVERITY_FATAL);
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            return false;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    private void showMessage(String titulo, String detalle, FacesMessage.Severity severidad) {
        FacesContext fc = FacesContext.getCurrentInstance();
        FacesMessage mensaje = new FacesMessage(severidad, titulo, detalle);
        fc.addMessage(null, mensaje);
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the contrasenia
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @param contrasenia the contrasenia to set
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * @return the carpetasUsuarios
     */
    public List<Carpeta> getCarpetasUsuarios() {
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            this.carpetasUsuarios = daoCarpeta.list(this.user, this.session);
            this.transaccion.commit();
        } catch (Exception err) {
            System.out.println(err.getMessage());
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            this.carpetasUsuarios = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return carpetasUsuarios;
    }

    /**
     * @param carpetasUsuarios the carpetasUsuarios to set
     */
    public void setCarpetasUsuarios(List<Carpeta> carpetasUsuarios) {
        this.carpetasUsuarios = carpetasUsuarios;
    }

    /**
     * @return the carpetaSeleccion
     */
    public List<Carpeta> getCarpetaSeleccion() {
        return carpetaSeleccion;
    }

    /**
     * @param carpetaSeleccion the carpetaSeleccion to set
     */
    public void setCarpetaSeleccion(List<Carpeta> carpetaSeleccion) {
        this.carpetaSeleccion = carpetaSeleccion;
    }

    /**
     * @return the carpeta
     */
    public Carpeta getCarpeta() {
        return carpeta;
    }

    /**
     * @param carpeta the carpeta to set
     */
    public void setCarpeta(Carpeta carpeta) {
        this.carpeta = carpeta;
    }

    /**
     * @return the archivosUsuarios
     */
    public List<Archivo> getArchivosUsuarios() {
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            this.archivosUsuarios = this.daoArchivo.list(this.session, this.carpeta);
            this.transaccion.commit();
        } catch (Exception err) {
            System.out.println(err.getMessage());
            this.transaccion.rollback();

        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return archivosUsuarios;
    }

    /**
     * @param archivosUsuarios the archivosUsuarios to set
     */
    public void setArchivosUsuarios(List<Archivo> archivosUsuarios) {
        this.archivosUsuarios = archivosUsuarios;
    }

    /**
     * @return the archivoSeleccion
     */
    public List<Archivo> getArchivoSeleccion() {
        return archivoSeleccion;
    }

    /**
     * @param archivoSeleccion the archivoSeleccion to set
     */
    public void setArchivoSeleccion(List<Archivo> archivoSeleccion) {
        this.archivoSeleccion = archivoSeleccion;
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
     * @return the addCarpeta
     */
    public Carpeta getAddCarpeta() {
        return addCarpeta;
    }

    /**
     * @param addCarpeta the addCarpeta to set
     */
    public void setAddCarpeta(Carpeta addCarpeta) {
        this.addCarpeta = addCarpeta;
    }

    /**
     * @return the addArchivo
     */
    public Archivo getAddArchivo() {
        return addArchivo;
    }

    /**
     * @param addArchivo the addArchivo to set
     */
    public void setAddArchivo(Archivo addArchivo) {
        this.addArchivo = addArchivo;
    }
}
