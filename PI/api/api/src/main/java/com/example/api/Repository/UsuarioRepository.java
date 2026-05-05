package com.example.api.Repository;

import com.example.api.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    boolean existsByNick(String nick);
}