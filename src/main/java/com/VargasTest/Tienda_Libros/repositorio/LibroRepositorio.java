package com.VargasTest.Tienda_Libros.repositorio;

import com.VargasTest.Tienda_Libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Integer> {

}
