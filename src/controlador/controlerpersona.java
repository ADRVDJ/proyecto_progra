/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import modelo.Persona;
import modelo.PersonaJpaController;
import proyecto_producti.managerfactory;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.exceptions.NonexistentEntityException;
import vista.Viewpersona;

/**
 *
 * @author ASUS TUF GAMING
 */
public class controlerpersona {

    ModeloTablaPersona modeloTablaPersona;
    Viewpersona vista;
    managerfactory manage;
    PersonaJpaController modeloPersona;
    Persona persona;
    JDesktopPane panelEscritorio;
    ListSelectionModel listapersonamodel;

    public controlerpersona(Viewpersona vista, managerfactory manage, PersonaJpaController modeloPersona, JDesktopPane panelEscritorio) {

        this.manage = manage;
        this.modeloPersona = modeloPersona;
        this.panelEscritorio = panelEscritorio;
        this.modeloTablaPersona = new ModeloTablaPersona();
        this.modeloTablaPersona.setFilas(modeloPersona.findPersonaEntities());

        if (controladmin.vp == null) {
            controladmin.vp = new Viewpersona();
            this.vista = controladmin.vp;
            this.panelEscritorio.add(this.vista);
            this.vista.getTablapersona().setModel(modeloTablaPersona);
            this.vista.show();
            inciar();
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            controladmin.vp.show();
        }

    }

    public void guardarPersona() {
        persona = new Persona();
        persona.setNombre(this.vista.getNombre().getText());
        persona.setApellido(this.vista.getApellido().getText());
        persona.setCedula(this.vista.getCedula().getText());
        persona.setCelular(this.vista.getCelular().getText());
        persona.setCorreo(this.vista.getCoreo().getText());
        persona.setDireccion(this.vista.getDireccio().getText());
        modeloPersona.create(persona);
        modeloTablaPersona.agregar(persona);
        Resouces.success("ALERTA!!", "PERSONA GUARDADA CORECTAMENTE");
        JOptionPane.showMessageDialog(panelEscritorio, "PERSONA CREADA CORRECTAMENTE");
        limpiar();
    }

    public void editarPersona() {
        if (persona != null) {
            persona.setNombre(this.vista.getNombre().getText());
            persona.setApellido(this.vista.getApellido().getText());
            persona.setCedula(this.vista.getCedula().getText());
            persona.setCelular(this.vista.getCelular().getText());
            persona.setCorreo(this.vista.getCoreo().getText());
            persona.setDireccion(this.vista.getDireccio().getText());
            Resouces.success("ALERTA!!", "PERSONA ACTUALIZAD CORECTAMENTE");
            try {
                modeloPersona.edit(persona);
                modeloTablaPersona.eliminar(persona);
                modeloTablaPersona.actualizar(persona);
                limpiar();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(controlerpersona.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eliminarPersona() {
        if (persona != null) {
            try {
                modeloPersona.destroy(persona.getIdpersona());
                limpiar();
            } catch (NonexistentEntityException ex) {
                java.util.logging.Logger.getLogger(controlerpersona.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            modeloTablaPersona.eliminar(persona);
            JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
            Resouces.success("ALERTA!!", "PERSONA ELIMINADA CORECTAMENTE");
        }
    }

    public void inciar() {
        this.vista.getBtguardar().addActionListener(l -> guardarPersona());
        this.vista.getEditar().addActionListener(l -> editarPersona());
        this.vista.getEliminar().addActionListener(l -> eliminarPersona());
        this.vista.getTablapersona().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listapersonamodel = this.vista.getTablapersona().getSelectionModel();
        listapersonamodel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    personaSeleccionada();
                }
            }

        });
        this.vista.getEliminar().setEnabled(false);
        this.vista.getEditar().setEnabled(false);
        this.vista.getLimpiar1().addActionListener(l -> limpiar());
        this.vista.getLimpiar2().addActionListener(l -> limpiarbuscador());
        this.vista.getBuscar().addActionListener(l -> buscarpersona());
        this.vista.getCriterio().addActionListener(l -> buscarpersona());
        this.vista.getReportegeneralbt().addActionListener(l -> reporteGeneral());
        this.vista.getReporteuni().addActionListener(l -> reporteIndividual());
    }

    public void limpiar() {
        this.vista.getNombre().setText("");
        this.vista.getApellido().setText("");
        this.vista.getCedula().setText("");
        this.vista.getCelular().setText("");
        this.vista.getCoreo().setText("");
        this.vista.getDireccio().setText("");
        this.vista.getEliminar().setEnabled(false);
        this.vista.getEditar().setEnabled(false);
        this.vista.getBtguardar().setEnabled(true);
        this.vista.getTablapersona().getSelectionModel().clearSelection();
    }

    public void personaSeleccionada() {
        if (this.vista.getTablapersona().getSelectedRow() != -1) {
            persona = modeloTablaPersona.getFilas().get(this.vista.getTablapersona().getSelectedRow());
            this.vista.getNombre().setText(persona.getNombre());
            this.vista.getApellido().setText(persona.getApellido());
            this.vista.getCedula().setText(persona.getCedula());
            this.vista.getCelular().setText(persona.getCelular());
            this.vista.getCoreo().setText(persona.getCorreo());
            this.vista.getDireccio().setText(persona.getDireccion());
            this.vista.getEliminar().setEnabled(true);
            this.vista.getEditar().setEnabled(true);
            this.vista.getBtguardar().setEnabled(false);
        }

    }

    public void buscarpersona() {
        if (this.vista.getCriterio().isSelected()) {
            modeloTablaPersona.setFilas(modeloPersona.findPersonaEntities());
            modeloTablaPersona.fireTableDataChanged();

        } else {
            if (!this.vista.getTxtbuscar().getText().equals("")) {
                modeloTablaPersona.setFilas(modeloPersona.buscarPersona(this.vista.getTxtbuscar().getText()));
                modeloTablaPersona.fireTableDataChanged();
            } else {
                limpiarbuscador();
            }

        }

    }

    public void limpiarbuscador() {
        this.vista.getTxtbuscar().setText("");
        modeloTablaPersona.setFilas(modeloPersona.findPersonaEntities());
        modeloTablaPersona.fireTableDataChanged();
    }

    public void reporteGeneral() {

        Resouces.imprimirReeporte(managerfactory.getConnection(manage.getentityManagerFactory().createEntityManager()), "/reportes/Personas.jasper", new HashMap());
    }

    public void reporteIndividual() {
        if (persona != null) {
            Map parameters = new HashMap();
            parameters.put("id", persona.getIdpersona());
            Resouces.imprimirReeporte(managerfactory.getConnection(manage.getentityManagerFactory().createEntityManager()), "/reportes/ind_persona.jasper", parameters);
        } else {
            Resouces.warning("Atencion!!", "Debe selecionar una persona");
        }

    }

}
