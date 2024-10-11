package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.enums.TipoEvento;

import java.time.LocalDate;

public record FiltroEventoDTO(
        String nombre,
        TipoEvento tipo,
        String ciudad,
        LocalDate fecha
) {
}