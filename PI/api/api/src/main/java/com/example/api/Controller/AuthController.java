package com.example.api.Controller;

import com.example.api.Model.Usuario;
import com.example.api.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository repo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario u) {
        Optional<Usuario> found = repo.findByEmailAndPassword(u.getEmail(), u.getPassword());
        if (found.isPresent()) {
            return ResponseEntity.ok(found.get());
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario u) {
        if (repo.existsByEmail(u.getEmail()))
            return ResponseEntity.status(400).body("Email ya registrado");
        if (repo.existsByNick(u.getNick()))
            return ResponseEntity.status(400).body("Nick ya en uso");
        u.setRolId(3); // rol usuario por defecto
        return ResponseEntity.ok(repo.save(u));
    }
}