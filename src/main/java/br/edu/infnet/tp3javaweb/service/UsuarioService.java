package br.edu.infnet.tp3javaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.infnet.tp3javaweb.model.Usuario;
import br.edu.infnet.tp3javaweb.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Iterable<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
}
