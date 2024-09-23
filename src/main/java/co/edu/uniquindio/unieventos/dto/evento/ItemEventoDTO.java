package co.edu.uniquindio.unieventos.dto.evento;

import java.time.LocalDateTime;

public record ItemEventoDTO(
        String urlImagenPortada,
        String nombre,
        LocalDateTime fecha,
        String direccion
) {
}
