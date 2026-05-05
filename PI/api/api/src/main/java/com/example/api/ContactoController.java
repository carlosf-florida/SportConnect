package com.example.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContactoController {

    private final DiscordService discordService;

    public ContactoController(DiscordService discordService) {
        this.discordService = discordService;
    }

    @PostMapping("/contacto")
    public ResponseEntity<String> recibirContacto(@RequestBody ContactoRequest contacto) {
        try {
            discordService.enviarMensaje(contacto);
            return ResponseEntity.ok("Mensaje enviado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el mensaje");
        }
    }
}
