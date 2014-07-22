/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function onlyText(event) {
    var charCode = (event.which) ? event.which : event.keyCode;
    if (charCode === 209 || charCode === 241) {
        return true;
    }
    if (charCode !== 32 && (charCode < 64 || charCode > 92) && charCode < 95 || charCode > 122) {
        return false;
    }
    return true;
}

function onlyAlphaNumeric(event) {
    var charCode = (event.which) ? event.which : event.keyCode;
    if ((charCode < 48 || charCode > 57) && (charCode < 64 || charCode > 92) && (charCode < 95 || charCode > 122)) {
        return false;
    }
    return true;
}

function noEnie(event) {
    var charCode = (event.which) ? event.which : event.keyCode;
    var letra = String.fromCharCode(charCode)

    var res = letra.match(/^[\w\.\-\_\,\+\ ]+$/i);
    return (res !== null);
}

function rename(event) {
    var charCode = (event.which) ? event.which : event.keyCode;
    var letra = String.fromCharCode(charCode)

    var res = letra.match(/^[\w\-\_\ ]+$/i);
    return (res !== null);
}