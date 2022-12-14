/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Producto;

/**
 *
 * @author ASUS TUF GAMING
 */
public class ModeloTablaProducto extends AbstractTableModel {

    private String[] columnas = {"Nombre", "Cantidad", "Precio"};
    public static List<Producto> filas;
    private Producto productoSelecionado;
    private int indice;

    public ModeloTablaProducto() {
        filas = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return filas.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        productoSelecionado = filas.get(rowIndex);
        this.indice = rowIndex;
        switch (columnIndex) {
            case 0:
                return productoSelecionado.getNombre();
            case 1:
                return productoSelecionado.getCantidad();
            case 2:
                return productoSelecionado.getPrecio();

            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Double.class;

            default:
                return Object.class;
        }
    }

    public String[] getColumnas() {
        return columnas;
    }

    public void setColumnas(String[] columnas) {
        this.columnas = columnas;
    }

    public List<Producto> getFilas() {
        return filas;
    }

    public void setFilas(List<Producto> filas) {
        this.filas = filas;
    }

    public Producto getProductoSelecionado() {
        return productoSelecionado;
    }

    public void setProductoSelecionado(Producto productoSelecionado) {
        this.productoSelecionado = productoSelecionado;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public void actualizar(Producto p) {
        setProductoSelecionado(null);
        if (p != null) {
            filas.add(indice, p);
            fireTableDataChanged();
        }
    }

    public void agregar(Producto p) {
        if (p != null) {
            filas.add(p);
            fireTableDataChanged();
        }
    }

    public void eliminar(Producto p) {
        if (p != null) {
            filas.remove(p);
            fireTableDataChanged();
        }

    }

}
