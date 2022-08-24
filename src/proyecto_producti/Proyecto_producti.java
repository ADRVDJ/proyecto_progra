/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producti;

import controlador.ControllerLogin;
import modelo.UsuarioJpaController;
import vista.Viewlogin;

/**
 *
 * @author ASUS TUF GAMING
 */
public class Proyecto_producti {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        managerfactory manager = new managerfactory();
        vista.Viewlogin vista = new Viewlogin();
        UsuarioJpaController modelo = new UsuarioJpaController(manager.getentityManagerFactory());
        ControllerLogin controlador = new ControllerLogin(manager, vista, modelo);
        
    }

}
