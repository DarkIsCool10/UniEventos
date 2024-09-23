package co.edu.uniquindio.unieventos.dto.cupon;

import co.edu.uniquindio.unieventos.model.enums.EstadoCupon;
import co.edu.uniquindio.unieventos.model.enums.TipoCupon;

import java.time.LocalDateTime;

public record ItemsCuponDTO (
        String nombre,
        LocalDateTime fechaVencimiento,
        LocalDateTime fechaApertura,
        Float descuento,
        TipoCupon tipo,
        EstadoCupon estado
){
}