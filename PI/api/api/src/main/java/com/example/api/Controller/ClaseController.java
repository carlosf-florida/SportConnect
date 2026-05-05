package com.example.api.Controller;


import com.example.api.Model.Clase;
import com.example.api.Repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ClaseController {

    @Autowired
    private ClaseRepository repo;

    @GetMapping("/clases")
    public ResponseEntity<List<Clase>> getClases() {
        return ResponseEntity.ok(repo.findAll());
    }

    @PostMapping("/clases")
    public ResponseEntity<Clase> crearClase(@RequestBody Clase clase) {
        return ResponseEntity.ok(repo.save(clase));
    }
}