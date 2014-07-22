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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
    /*Objeto de descarga*/
    private StreamedContent file;

    public SUsuarioBean() {
        HttpSession miSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        miSession.setMaxInactiveInterval(600);
        this.daoArchivo = new DAOArchivo();
        this.daoCarpeta = new DAOCarpeta();
        this.addCarpeta = new Carpeta();
        this.addArchivo = new Archivo();
        this.archivo = new Archivo();
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
        String folderName = this.addCarpeta.getCptNombre().trim();
        if (folderName.length() <= 0) {
            this.showMessage("Carpeta sin nombre", "Asignale por favor un nombre a tu carpeta", FacesMessage.SEVERITY_ERROR);
            return;
        }
        
        if (folderName.startsWith("-")||folderName.startsWith("_")||Pattern.matches("[0-9]*", folderName.substring(0,1))){
            this.showMessage("Nombre innválido", "El nombre debe comenzar con letra", FacesMessage.SEVERITY_WARN);
            return;            
        }
        try {
            folderDAO = new DAOCarpeta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            Carpeta folder = folderDAO.getCarpetaByNombre(folderName, this.user, this.session);
            if (folder != null) {
                this.showMessage("La carpeta ya existe", "Puedes modificar desde las opciones de la derecha", FacesMessage.SEVERITY_WARN);
                this.transaccion.rollback();
                return;
            }
            this.addCarpeta.setCptNombre(folderName);
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

    public void deleteFolder() {
        String folderName = this.carpeta.getCptNombre();
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            this.daoCarpeta.deleteCarpeta(this.carpeta, this.session);
            this.transaccion.commit();
            File folderPath = new File(this.carpeta.getCptRuta());
            this.deletePhysicalFolder(folderPath);
            folderPath.delete();
            this.showMessage("Carpeta Eliminada", "Se ha eliminado satisfactoriamente la carpeta " + folderName, FacesMessage.SEVERITY_INFO);

        } catch (Exception err) {
            System.out.println(err.getMessage());
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            this.showMessage("Problemas al eliminar", "Consulte con su administrador. Error tipo: " + err.getMessage(), FacesMessage.SEVERITY_ERROR);
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void updateFolder() {
        String folderName = this.addCarpeta.getCptNombre().trim();
        if (folderName.length() <= 0) {
            this.showMessage("Carpeta sin nombre", "Asignale por favor un nuevo nombre a esta carpeta", FacesMessage.SEVERITY_ERROR);
            return;
        }
        
        if (folderName.startsWith("-")||folderName.startsWith("_")||Pattern.matches("[0-9]*", folderName.substring(0,1))){
            this.showMessage("Nombre innválido", "El nombre debe comenzar con letra", FacesMessage.SEVERITY_WARN);
            return;            
        }
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            Carpeta folder = this.daoCarpeta.getCarpetaByNombre(folderName, this.user, this.session);
            if (folder != null) {
                this.showMessage("La carpeta ya existe", "Puedes modificar desde las opciones de la derecha", FacesMessage.SEVERITY_WARN);
                this.transaccion.rollback();
                return;
            }
            this.carpeta.setCptNombre(folderName);
            /*Actualización de la carpeta*/
            String actualPath = carpeta.getCptRuta();
            String folderPath = actualPath.substring(0, carpeta.getCptRuta().lastIndexOf("\\"));
            String newNamePath = folderPath + "\\" + folderName;
            carpeta.setCptRuta(newNamePath);
            this.daoCarpeta.updateCarpeta(this.carpeta, this.session);
            /*Renombrando archivos de la carpeta*/
            List<Archivo> archivos = this.daoArchivo.list(this.session, this.carpeta);
            for (int i = 0; i < archivos.size(); i++) {
                Archivo temp = archivos.get(i);
                temp.setRuta(newNamePath + "\\" + temp.getNombre());
                daoArchivo.updateFile(temp, this.session);
            }
            /*Renombramos la carpeta dentro del servidor*/
            File f = new File(actualPath);
            f.renameTo(new File(newNamePath));
            this.showMessage("Carpeta renombrada", "El nuevo nombre será " + folderName, FacesMessage.SEVERITY_INFO);
            this.transaccion.commit();
            this.addCarpeta = new Carpeta();
        } catch (Exception err) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            this.showMessage("Operacion no realizada", "Consulte con el administrador. Error tipo: " + err.getMessage(), FacesMessage.SEVERITY_WARN);

        } finally {
            if (this.session != null) {
                session.close();
            }
        }
    }

    private void deletePhysicalFolder(File folder) {
        File[] temps = folder.listFiles();
        for (int i = 0; i < temps.length; i++) {
            File temp = temps[i];
            if (temp.isDirectory()) {
                deletePhysicalFolder(temp);
            }
            temp.delete();
        }
    }

    public void downloadFile() {
        File f = new File(this.archivo.getRuta());
        try {
            InputStream input = new FileInputStream(f);
            ExternalContext container = FacesContext.getCurrentInstance().getExternalContext();
            this.file = new DefaultStreamedContent(input,container.getMimeType(f.getName()),f.getName());
        } catch (Exception err) {
            this.showMessage("Problemas al descargar", "Consulta con tu administrador. Error tipo: "+err.getMessage(), FacesMessage.SEVERITY_WARN);
        }
    }   
    
    public void deleteFile(){
       try{
           this.session = HibernateUtil.getSessionFactory().openSession();
           this.transaccion = session.beginTransaction();           
           File f = new File(this.archivo.getRuta());
           f.delete();
           this.daoArchivo.deleteFile(this.archivo, this.session);
           this.transaccion.commit();
           this.showMessage("Eliminado", "El archivo se ha eliminado correctamente", FacesMessage.SEVERITY_INFO);
           this.archivo=new Archivo();
       }catch(Exception err){
           if (this.transaccion!=null){
               this.transaccion.rollback();
           }
           this.showMessage("Ha ocurrido un problema","Consulte con su administrador. Error: "+err.getMessage(),FacesMessage.SEVERITY_WARN);
       }
       finally{
           if (this.session!=null){
               this.session.close();
           }
       }
    }
    
    
    public void updateFile(){
        String nombreArchivo = this.addArchivo.getNombre();
        
        if (nombreArchivo.trim().length()<=0){
            this.showMessage("Archivo sin nombre", "Asignale un nombre a este archivo por favor", FacesMessage.SEVERITY_WARN);
            return;
        }
        if (nombreArchivo.startsWith("-")||nombreArchivo.startsWith("_")||Pattern.matches("[0-9]*", nombreArchivo.substring(0,1))){
            this.showMessage("Nombre innválido", "El nombre debe comenzar con letra", FacesMessage.SEVERITY_WARN);
            return;            
        }
        try{
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            String ext = this.archivo.getNombre().substring(this.archivo.getNombre().lastIndexOf(".")+1);
            String path = this.archivo.getRuta();
            String newPath = path.substring(0,path.lastIndexOf("\\"))+"\\"+nombreArchivo+"."+ext;
            File f = new File(path);
            this.archivo.setNombre(nombreArchivo+"."+ext);
            this.archivo.setRuta(newPath);
            daoArchivo.updateFile(this.archivo, this.session);
            f.renameTo(new File(newPath));
            this.transaccion.commit();
            this.showMessage("Cambio realizado", "El archivo ha sido renombrado", FacesMessage.SEVERITY_INFO);
            this.addArchivo = new Archivo();
        }catch(Exception err){
            if (this.transaccion!=null){
                this.transaccion.rollback();
            }
            this.showMessage("Ha ocurrido un problema","Consulte con su administrador. Error "+err.getMessage(), FacesMessage.SEVERITY_ERROR);
        }finally{
            if (this.session!=null){
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

    /**
     * @return the file
     */
    public StreamedContent getFile() {
        return file;
    }
}
