/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import modelo.Usuario;
import modelo.UsuarioJpaController;
import proyecto_producti.managerfactory;
import vista.Viewadmin;
import vista.Viewlogin;

/**
 *
 * @author ASUS TUF GAMING
 */
public class ControllerLogin {

//login
    private managerfactory manager;
    private Viewlogin vista;
    private UsuarioJpaController modelo;
    Viewadmin admin = new Viewadmin();
    
    public ControllerLogin(managerfactory manager, Viewlogin vista, UsuarioJpaController modelo) {
        this.manager = manager;
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        iniciarcontrol();
    }
    
    public void iniciarcontrol() {
        vista.getEntrar().addActionListener(l -> controlLogin());
        vista.getCerrar().addActionListener(l -> System.exit(0));
    }
    
    public void controlLogin() {
        String usuario = vista.getUsuariotxt().getText();
        String contraseña = new String(vista.getContratxtc().getPassword());
        try {
            Usuario user = modelo.buscarByCredenciales(usuario, contraseña);
            
            if (user != null) {
                System.out.println("usuario correcto");
                //JOptionPane.showMessageDialog(vista, "Usuario correcto" + " " + user.getIdpersona().toString());
                admin.setVisible(true);
                new controladmin(admin, manager);
                vista.setVisible(false);
                
            } else {
                JOptionPane.showMessageDialog(vista, "Usuario incorrecto");
            }
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(vista, "no existe conecion con la base de datos");
        }
        
    }
    
}
