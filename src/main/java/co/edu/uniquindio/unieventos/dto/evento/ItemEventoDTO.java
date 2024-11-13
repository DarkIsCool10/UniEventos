package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.enums.TipoEvento;

import java.time.LocalDate;

public record ItemEventoDTO(
        String id,
        String urlImagenPortada,
        String nombre,
        TipoEvento tipoEvento,
        LocalDate fecha,
        String direccion,
        String ciudad
) {
}
