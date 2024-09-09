package co.edu.uniquindio.unieventos.model.documents;


import co.edu.uniquindio.unieventos.model.enums.EstadoCuenta;
import co.edu.uniquindio.unieventos.model.enums.Rol;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("Cuenta")
@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cuenta {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private Usuario usuario;
    private String email;
    private String password;
    private String codigoVerificacionRegistro;
    private String codigoVerificacionContrasenia;
    private LocalDateTime fechaRegistro;
    private Rol rol;
    private EstadoCuenta estado;

    @Builder
    public Cuenta(Usuario usuario, String email, String password, String codigoVerificacion, String codigoVerificacionContrasenia, LocalDateTime fechaRegistro, Rol rol, EstadoCuenta estado) {
        this.usuario = usuario;
        this.email = email;
        this.password = password;
        this.codigoVerificacionRegistro = codigoVerificacion;
        this.codigoVerificacionContrasenia = codigoVerificacionContrasenia;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
        this.estado = estado;
    }
}
