package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record EditarEventoDTO(
        @NotNull(message = "Evento no encontrado")
        String id,

        @NotBlank(message = "Debe agregar un nombre a el evento")
        @Length(min= 5, max=100,message = "El evento debe tener un nombre maximo a 100 caracteres")
        String nombre,

        @NotBlank(message = "Debe agergar una descripcion a el evento")
        @Length(min=5,max=250,message = "La descripcion tiene un maximo de 250 caracteres")
        String descripcion,

        @NotBlank(message = "Debe agregar la direccion del evento")
        @Length(min=5,max = 45,message = "La direccion deben tener un maximo de 45 caracteres")
        String direccion,

        @NotBlank(message = "Debe agregar la ciudad donde se realizará el evento")
        @Length(min= 5, max=50,message = "La ciudad debe tener al menos 5 caracteres y un maximo de 50")
        String ciudad,

        @NotNull(message = "La fecha del evento es obligatoria.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDate fecha,

        @NotNull(message = "Debe agregar una imagen para la portada del evento")
        String imagenPortada,

        @NotNull(message = "Debe agregar las imagenes para las localidades del evento")
        String imagenLocalidad,

        @NotNull(message = "Debe agregar localidades a el evento")
        List<CrearLocalidadDTO> listaLocalidades,

        @NotNull(message = "Debe seleccionar el tipo de evento")
        TipoEvento tipoEvento,

        @NotNull(message = "Debe seleccionar el estado del evento")
        EstadoEvento estadoEvento

) {
}
