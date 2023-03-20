package br.edu.infnet.tp3javaweb.repository;

import org.springframework.data.repository.CrudRepository;

import br.edu.infnet.tp3javaweb.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    
}
