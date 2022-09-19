/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.UsuarioJpaController;
import proyecto_producti.managerfactory;
import vista.ViewUsuarios;
import modelo.Usuario;

/**
 *
 * @author ASUS TUF GAMING
 */
public class ControllerUsuario {

    ViewUsuarios vista;
    managerfactory manage;
    UsuarioJpaController modeloUsuario;
    Usuario usuario;
    JDesktopPane panelEscritorio;
    ModeloTablaUsuario modelotabla;
    ListSelectionModel listaUsernaModel;

    public ControllerUsuario(ViewUsuarios vista, managerfactory manage, UsuarioJpaController modeloUsuario, JDesktopPane panelEscritorio) {
        this.manage = manage;
        this.modeloUsuario = modeloUsuario;
        this.panelEscritorio = panelEscritorio;
        this.modelotabla = new ModeloTablaUsuario();
        this.modelotabla.setFilas(this.modeloUsuario.findUsuarioEntities());

        if (controladmin.vu == null) {
            controladmin.vu = new ViewUsuarios();
            this.vista = controladmin.vu;
            this.panelEscritorio.add(this.vista);
            this.vista.getTablausuario().setModel(modelotabla);

            this.vista.show();
            cargarCombobos();
            iniciarControl();

            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            controladmin.vu.show();

        }
    }

    public void iniciarControl() {
        this.vista.getGuardarUS().addActionListener(l -> guardarUsuario());
        this.vista.getBtactualizarUS().addActionListener(l -> editarUsuario());
        this.vista.getBteliminarUS().addActionListener(l -> eliminarUsuario());
        this.vista.getTablausuario().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsernaModel = this.vista.getTablausuario().getSelectionModel();
        listaUsernaModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    usuarioSeleccionado();
                }
            }

        });

        this.vista.getBteliminarUS().setEnabled(false);
        this.vista.getBtactualizarUS().setEnabled(false);
        this.vista.getLimpFU().addActionListener(l -> limpiar());
        this.vista.getBtlimpiar().addActionListener(l -> limpiarbuscador());
        this.vista.getBotonbusqueda().addActionListener(l -> buscarusuario());
        this.vista.getCheckmos().addActionListener(l -> buscarusuario());
        this.vista.getReportesgenral().addActionListener(l -> reporteGeneral());
        this.vista.getBtnReporteIndividualU().addActionListener(l -> reporteIndividual());
    }

    //GUARDAR PERSONA
    public void guardarUsuario() {
        usuario = new Usuario();
        usuario.setUsuario(this.vista.getUsuariotxt().getText());
        usuario.setClave(this.vista.getContraseñatxt().getText());
        usuario.setIdpersona((Persona) this.vista.getCmbopersona().getSelectedItem());

        modeloUsuario.create(usuario);
        modelotabla.agregar(usuario);
        Resouces.success("Atención!!", "USUARIO GUARDADA CORECTAMENTE");
        //  JOptionPane.showMessageDialog(panelEscritorio, "PERSONA CREADA CORRECTAMENTE");
        limpiar();
    }

    //EDITAR PERSONA
    public void editarUsuario() {
        if (usuario != null) {
            usuario.setUsuario(this.vista.getUsuariotxt().getText());
            usuario.setClave(this.vista.getContraseñatxt().getText());
            usuario.setIdpersona((Persona) this.vista.getCmbopersona().getSelectedItem());
            Resouces.success("Atención!!", "USUARIO EDITADA CORECTAMENTE");
            try {
                modeloUsuario.edit(usuario);
                modelotabla.eliminar(usuario);
                modelotabla.actualizar(usuario);
                limpiar();
            } catch (Exception ex) {

                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    //ELIMINAR PERSONA
    public void eliminarUsuario() {
        if (usuario != null) {
            try {
                modeloUsuario.destroy(usuario.getIdusuario());
                limpiar();
            } catch (Exception ex) {
                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
                limpiar();
            }
            modelotabla.eliminar(usuario);
            //  JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
            Resouces.success("ALERTA!!", "USUARIO ELIMINADO CORECTAMENTE");
        }
    }

    public void limpiar() {
        this.vista.getUsuariotxt().setText("");
        this.vista.getContraseñatxt().setText("");
        this.vista.getCmbopersona().setSelectedItem(0);

        this.vista.getBteliminarUS().setEnabled(false);
        this.vista.getBtactualizarUS().setEnabled(false);
        this.vista.getGuardarUS().setEnabled(true);
        this.vista.getTablausuario().getSelectionModel().clearSelection();
    }

    public void usuarioSeleccionado() {
        if (this.vista.getTablausuario().getSelectedRow() != -1) {
            usuario = modelotabla.getFilas().get(this.vista.getTablausuario().getSelectedRow());
            this.vista.getUsuariotxt().setText(usuario.getUsuario());
            this.vista.getContraseñatxt().setText(usuario.getClave());
            this.vista.getCmbopersona().setSelectedItem(usuario.getIdpersona());
            //
            this.vista.getBteliminarUS().setEnabled(true);
            this.vista.getBtactualizarUS().setEnabled(true);
            this.vista.getGuardarUS().setEnabled(false);
        }
    }

    public void limpiarbuscador() {
        this.vista.getBuscartxt().setText("");
        modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
        modelotabla.fireTableDataChanged();
    }

    public void cargarCombobos() {
        try {
            Vector v = new Vector();
            v.addAll(new PersonaJpaController(manage.getentityManagerFactory()).findPersonaEntities());
            this.vista.getCmbopersona().setModel(new DefaultComboBoxModel(v));

        } catch (ArrayIndexOutOfBoundsException ex) {

            System.out.println("ERROR");
        }
    }

    public void buscarusuario() {
        if (this.vista.getCheckmos().isSelected()) {
            modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
            modelotabla.fireTableDataChanged();
            limpiarbuscador();
            // System.out.println("llego");
        } else {
            if (!this.vista.getBuscartxt().getText().equals("")) {
                modelotabla.setFilas(modeloUsuario.buscarusuario(this.vista.getBuscartxt().getText()));
                modelotabla.fireTableDataChanged();
                //  System.out.println("llego2");
            } else {

            }

        }

    }

    public void reporteGeneral() {
        Resouces.imprimirReeporte(managerfactory.getConnection(manage.getentityManagerFactory().createEntityManager()), "/reportes/Usuario.jasper", new HashMap());
    }

    public void reporteIndividual() {
        if (usuario != null) {
            Map parameters = new HashMap();
            parameters.put("id", usuario.getIdusuario());
            Resouces.imprimirReeporte(managerfactory.getConnection(manage.getentityManagerFactory().createEntityManager()), "/reportes/individual.jasper", parameters);
        } else {
            Resouces.warning("Atencion!!", "Debe selecionar un usuario");
        }

    }
}
