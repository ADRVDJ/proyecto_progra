/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.PersonaJpaController;
import modelo.ProductoJpaController;
import modelo.UsuarioJpaController;
import proyecto_producti.managerfactory;
import vista.ViewProductos;
import vista.Viewadmin;
import vista.Viewpersona;
import vista.ViewUsuarios;
/**
 *
 * @author ASUS TUF GAMING
 */
public class controladmin extends javax.swing.JFrame {

    Viewadmin vista;
    managerfactory manage;

    public controladmin(Viewadmin vista, managerfactory manage) {
    this.vista = vista;
        this.manage = manage;
        this.vista.setExtendedState(MAXIMIZED_BOTH);
        controlEvento();
    }

    public void controlEvento() {
       this.vista.getItempersona().addActionListener(l -> cargarvistaPersona());
       this.vista.getItemproducto().addActionListener(l -> cargarvistaProducto());
         this.vista.getItemusuario().addActionListener(l -> cargarvistaUsuario());
    }

    public static Viewpersona vp = null;
    public static ViewProductos vps = null;
    public static ViewUsuarios vu= null;
    
    public void cargarvistaPersona() {
        new controlerpersona(vp, manage, new PersonaJpaController (manage.getentityManagerFactory()), this.vista.getEscritorio());
        System.out.println("mensaje");
    }
    public void cargarvistaProducto() {
        new ControllerProducto (vps, manage, new ProductoJpaController(manage.getentityManagerFactory()), this.vista.getEscritorio());
        System.out.println("mensaje");

    }
  public void cargarvistaUsuario() {
        new ControllerUsuario(vu, manage, new UsuarioJpaController(manage.getentityManagerFactory()), this.vista.getEscritorio());
        System.out.println("mensaje");

    }
}
