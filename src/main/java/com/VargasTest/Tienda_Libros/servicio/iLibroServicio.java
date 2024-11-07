package com.VargasTest.Tienda_Libros.servicio;

import com.VargasTest.Tienda_Libros.modelo.Libro;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface iLibroServicio {
    public List<Libro> listarLibros();

    public Libro buscarLibroPorId(Integer idLibro);

    public void guardarLibro(Libro libro);

    public void eliminarLibro(Libro libro);


}
