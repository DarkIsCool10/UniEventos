package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record EditarCuentaDTO(

        @NotNull(message = "Usuario no encontrado")
        String id,

        @NotBlank(message = "El nombre es requerido")
        @Length(min= 10,max = 50, message = "El nombre debe tener un minimo de 10 y un máximo de 50 caracteres")
        String nombre,

        @NotBlank(message = "El teléfono es requerido")
        @Length(min= 10, max = 10, message = "El teléfono debe tener un máximo de 10 caracteres")
        String telefono,

        @Length(max = 100, message = "La dirección debe tener un máximo de 100 caracteres")
        String direccion,

        @NotBlank(message = "La contraseña es requerida")
        @Length(min = 7, max = 20, message = "La contraseña debe tener un mínimo de 7 caracteres y un máximo de 20 caracteres")
        String password
) {
}
