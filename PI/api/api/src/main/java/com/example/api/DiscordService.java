package com.example.api;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.*;

@Service
public class DiscordService {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1499374107895267440/aLWgIekH1jSUvn2T8nHn5he2om9lk5buKe_m2lsm_GzhNhw_JuCpdJLdtWjocm_VbOj5";

    public void enviarMensaje(ContactoRequest contacto) throws Exception {
       String contenido = String.format("""
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    📬 **NUEVO MENSAJE DE CONTACTO**
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    👤 **Nombre:** %s
    📧 **Email:** %s
    🏷️ **Tipo:** %s
    💬 **Mensaje:** %s
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    """,
    contacto.getNombre(),
    contacto.getEmail(),
    contacto.getTipo(),
    contacto.getMensaje()
);

        String json = "{\"content\": \"" + contenido.replace("\n", "\\n").replace("\"", "\\\"") + "\"}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(WEBHOOK_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
