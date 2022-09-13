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
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.Producto;
import modelo.ProductoJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producti.managerfactory;
import vista.ViewProductos;

/**
 *
 * @author ASUS TUF GAMING
 */
public class ControllerProducto {

    ViewProductos vista;
    managerfactory manage;
    ProductoJpaController modeloProducto;
    Producto producto;
    JDesktopPane panelEscritorio;
    ModeloTablaProducto modeloTablaProducto;
    ListSelectionModel listaproductomodel;

    public ControllerProducto(ViewProductos vista, managerfactory manage, ProductoJpaController modeloProducto, JDesktopPane panelEscritorio) {

        this.manage = manage;
        this.modeloProducto = modeloProducto;
        this.panelEscritorio = panelEscritorio;
        this.modeloTablaProducto = new ModeloTablaProducto();
        this.modeloTablaProducto.setFilas(this.modeloProducto.findProductoEntities());

        if (controladmin.vps == null) {
            controladmin.vps = new ViewProductos();

            this.vista = controladmin.vps;
            this.panelEscritorio.add(this.vista);
            this.vista.getTablaproducto().setModel(modeloTablaProducto);
            this.vista.show();
            inciar();
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            controladmin.vps.show();
        }

    }

    public void guardarProducto() {
        producto = new Producto();
        producto.setNombre(this.vista.getNombrePRTXT().getText());
        Double precio = Double.parseDouble(this.vista.getPrecioPRtxr().getText());
        producto.setPrecio(precio);
        Integer cantidad = Integer.parseInt(this.vista.getCantidadPrtxt().getText());
        producto.setCantidad(cantidad);

        modeloProducto.create(producto);
        modeloTablaProducto.agregar(producto);
        Resouces.success("ALERTA!!", "PRODUCTO GUARDADA CORECTAMENTE");
        JOptionPane.showMessageDialog(panelEscritorio, "PRODUCTO CREADO CORRECTAMENTE");
        limpiar();
    }

    public void editarProducto() {
        if (producto != null) {
            producto.setNombre(this.vista.getNombrePRTXT().getText());
            Double precio = Double.parseDouble(this.vista.getPrecioPRtxr().getText());
            producto.setPrecio(precio);
            Integer cantidad = Integer.parseInt(this.vista.getCantidadPrtxt().getText());
            producto.setCantidad(cantidad);
            Resouces.success("ALERTA!!", "PERSONA ACTUALIZAD CORECTAMENTE");
            try {
                modeloProducto.edit(producto);
                modeloTablaProducto.eliminar(producto);
                modeloTablaProducto.actualizar(producto);
                limpiar();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eliminarProducto() {
        if (producto != null) {
            try {
                modeloProducto.destroy(producto.getIdproducto());
                limpiar();
            } catch (NonexistentEntityException ex) {
                java.util.logging.Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            modeloTablaProducto.eliminar(producto);
            JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
            Resouces.success("ALERTA!!", "PERSONA ELIMINADA CORECTAMENTE");
        }
    }

    public void inciar() {
        this.vista.getBtguardarPR().addActionListener(l -> guardarProducto());
        this.vista.getEditarPR().addActionListener(l -> editarProducto());
        this.vista.getEliminarPR().addActionListener(l -> eliminarProducto());
        this.vista.getTablaproducto().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaproductomodel = this.vista.getTablaproducto().getSelectionModel();
        listaproductomodel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    productoSeleccionada();
                }
            }

        });
        this.vista.getEliminarPR().setEnabled(false);
        this.vista.getEditarPR().setEnabled(false);
        this.vista.getLimpiar1PR().addActionListener(l -> limpiar());
        this.vista.getLimpiarbuscadorPR().addActionListener(l -> limpiarbuscador());
        this.vista.getBuscarPR().addActionListener(l -> buscarproducto());
        this.vista.getMostrarPR().addActionListener(l -> buscarproducto());
        this.vista.getGenerallt().addActionListener(l -> reporteGeneral());
        this.vista.getInd().addActionListener(l -> reporteIndividual());
    }

    public void limpiar() {
        this.vista.getNombrePRTXT().setText("");
        this.vista.getPrecioPRtxr().setText("");
        this.vista.getCantidadPrtxt().setText("");

        this.vista.getEliminarPR().setEnabled(false);
        this.vista.getEditarPR().setEnabled(false);
        this.vista.getBtguardarPR().setEnabled(true);
        this.vista.getTablaproducto().getSelectionModel().clearSelection();
    }

    public void productoSeleccionada() {
        if (this.vista.getTablaproducto().getSelectedRow() != -1) {
            producto = modeloTablaProducto.getFilas().get(this.vista.getTablaproducto().getSelectedRow());
            this.vista.getNombrePRTXT().setText(producto.getNombre());
            String precio = String.valueOf(producto.getPrecio());
            this.vista.getPrecioPRtxr().setText(precio);
            String cantidad = String.valueOf(producto.getCantidad());
            this.vista.getCantidadPrtxt().setText(cantidad);
            ////
            this.vista.getEliminarPR().setEnabled(true);
            this.vista.getEditarPR().setEnabled(true);
            this.vista.getBtguardarPR().setEnabled(false);
        }

    }

    public void buscarproducto() {
        if (this.vista.getMostrarPR().isSelected()) {
            modeloTablaProducto.setFilas(modeloProducto.findProductoEntities());
            modeloTablaProducto.fireTableDataChanged();

        } else {
            if (!this.vista.getTxtbuscarPR().getText().equals("")) {
                modeloTablaProducto.setFilas(modeloProducto.buscarProducto(this.vista.getTxtbuscarPR().getText()));
                modeloTablaProducto.fireTableDataChanged();
            } else {
                limpiarbuscador();
            }

        }

    }

    public void limpiarbuscador() {
        this.vista.getTxtbuscarPR().setText("");
        modeloTablaProducto.setFilas(modeloProducto.findProductoEntities());
        modeloTablaProducto.fireTableDataChanged();
    }

    public void reporteGeneral() {
        Resouces.imprimirReeporte(managerfactory.getConnection(manage.getentityManagerFactory().createEntityManager()), "/reportes/Producto.jasper", new HashMap());
    }

    public void reporteIndividual() {
        if (producto != null) {
            Map parameters = new HashMap();
            parameters.put("id", producto.getIdproducto());
            Resouces.imprimirReeporte(managerfactory.getConnection(manage.getentityManagerFactory().createEntityManager()), "/reportes/ind_producto.jasper", parameters);
        } else {
            Resouces.warning("Atencion!!", "Debe selecionar una persona");
        }

    }

}
