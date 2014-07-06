/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


    var control = document.getElementById('registroUsuario:txtNombre');
    var name = control.value;
    //name=name.replace("Ã±","Ñ");
    var res = name.toString().toUpperCase();
    alert(res.replace("A","S"));
    document.getElementById('registroUsuario:txtNombre').value="Holi";
