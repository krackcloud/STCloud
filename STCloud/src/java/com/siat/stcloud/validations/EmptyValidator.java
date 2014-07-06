/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siat.stcloud.validations;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author KRACK
 */
@FacesValidator("empty")
public class EmptyValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String tag;
        HtmlInputText text = (HtmlInputText) component;
        if (text.getLabel() == null || text.getLabel().trim().equals("")) {
            tag = text.getId();
        } else {
            tag = text.getLabel();
        }        
        if (value.toString().trim().equals("")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, tag + ": Se encuentra vacío",tag + ": Se encuentra vacío"));
        }
        if (value.toString().trim().length() < 8) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, tag+ ": Debe contener al menos 8 caracteres", tag+ ": Debe contener al menos 8 caracteres"));
        }

    }
}
