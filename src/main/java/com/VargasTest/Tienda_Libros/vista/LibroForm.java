package com.VargasTest.Tienda_Libros.vista;

import com.VargasTest.Tienda_Libros.modelo.Libro;
import com.VargasTest.Tienda_Libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class LibroForm extends JFrame {

    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tableLibros;
    private JTextField libroTexto;
    private JTextField idTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());


        tableLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e ->modificarLibro());
        eliminarButton.addActionListener(e ->eliminarLibro());
    }



    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth()/2);
        int y = (tamanioPantalla.height = getHeight()/2);
        setLocation(x, y);

    }

    private void agregarLibro(){
        //Leer los valores del formulario
        if(libroTexto.getText().equals("")){
            mostrarMensaje("Proporciona el nombre del libro");
            libroTexto.requestFocusInWindow();
            return;
        }
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var existencias = Integer.parseInt(existenciasTexto.getText());
        //Crear el objeto libro
        var libro = new Libro(null, nombreLibro, autor, precio, existencias);
         // libro.setNombreLibro(nombreLibro);
        //libro.setAutor(autor);
       // libro.setPrecio(precio);
      //  libro.setExistencias(existencias);
        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("Se agrego el Libro....");
        limpiarFormulario();
        listarLibros();
    }

    private void cargarLibroSeleccionado(){
        // Los indices de las columnas inican en 0
        var renglon = tableLibros.getSelectedRow();
        if(renglon != -1){ // Regresa -1 si no se selecciono ningun registro
            String idLibro = tableLibros.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro= tableLibros.getModel().getValueAt(renglon, 1).toString();
            libroTexto.setText(nombreLibro);
            String autor = tableLibros.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);
            String precio = tableLibros.getModel().getValueAt(renglon,3).toString();
            precioTexto.setText(precio);
            String existencias = tableLibros.getModel().getValueAt(renglon,4).toString();
            existenciasTexto.setText(existencias);

        }
    }

    private void eliminarLibro(){
     var renglon = tableLibros.getSelectedRow();
     if(renglon != -1){
         String idLibro = tableLibros.getModel().getValueAt(renglon,0).toString();
         var libro = new Libro();
         libro.setIdLibro(Integer.parseInt(idLibro));
         libroServicio.eliminarLibro(libro);
         mostrarMensaje("Libro " + idLibro + " eliminado");
         limpiarFormulario();
         listarLibros();
     }else{
         mostrarMensaje("No se ha seleccionado ningun Libro a eliminar");
     }
    }

    private void modificarLibro(){
        if(this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un registro...");
        }else{
            //Verificamos que el nombre del libro no sea null
            if(libroTexto.getText().equals("")){
                mostrarMensaje("Proporciona el nombre del Libro");
                libroTexto.requestFocusInWindow();
                return;
            }
            //Llenamos el objeto libro
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = Double.parseDouble(precioTexto.getText());
            var existencias = Integer.parseInt(existenciasTexto.getText());
            var libro = new Libro(idLibro, nombreLibro, autor, precio, existencias);
            libroServicio.guardarLibro(libro);
            mostrarMensaje("Se modifico el libro");
            limpiarFormulario();
            listarLibros();
        }
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");

    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //Creamos el elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);



        this.tablaModeloLibros = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };
        String[] cabeceros = {"Id", "Libro", "Autor", "Precio", "Existencias"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);
        // Instanciar el objeto Jtable
        this.tableLibros = new JTable(tablaModeloLibros);
        // Evitar que se seleccionen varios registros
        tableLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }
    private void listarLibros(){
        // Limpiar tabla
        tablaModeloLibros.setRowCount(0);
        //Obtener los libros
        var libros= libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()

            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}
