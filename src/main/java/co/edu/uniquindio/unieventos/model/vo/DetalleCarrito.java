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

    @Id
    private String idEvento;
    private int cantidad;
    private String nombreLocalidad;
    private LocalDateTime fechaAgregacion;

}
