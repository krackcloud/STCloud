/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.net;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author KRACK
 */
public class Encrypt {
    
    public static String sha512(String string){
        StringBuilder container = new StringBuilder();
        try{
            MessageDigest security = MessageDigest.getInstance("SHA-512");
            security.update(string.getBytes());
            byte[] datos =security.digest();
            for(int i=0;i<datos.length;i++){
                container.append(Integer.toString((datos[i]&0xff)+0x100,16).substring(1));
            }
        }
        catch(NoSuchAlgorithmException err){
            err.printStackTrace();;
        }
        return container.toString();
    }
    
}
