package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetalleCarrito {

    private int cantidad;
    private String nombreLocalidad;
    private ObjectId idEvento;
    private LocalDateTime fechaAgregacion;

    public DetalleCarrito(int cantidad,String nombreLocalidad, LocalDateTime fechaAgregacion) {
        this.nombreLocalidad = nombreLocalidad;
        this.cantidad = cantidad;
        this.fechaAgregacion = fechaAgregacion;
    }
}
