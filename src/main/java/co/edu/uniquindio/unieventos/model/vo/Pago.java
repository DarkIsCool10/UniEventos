package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pago {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private LocalDateTime fecha;
    private String estado;
    private String detalleEstado;
    private String tipoPago;
    private String moneda;
    private String codigoAutorizacion;
    private float ValorTransaccion;

    private String metodoPago;

}
