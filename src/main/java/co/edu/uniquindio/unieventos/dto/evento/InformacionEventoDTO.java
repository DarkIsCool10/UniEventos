package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.units.qual.N;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record InformacionEventoDTO(
        @NotNull(message = "Evento no encontrado")
        String id,

        @NotNull(message = "Debe agregar una imagen para la portada del evento")
        String imagenPortada,

        @NotBlank(message = "Debe agregar un nombre a el evento")
        @Length(min= 5, max=100,message = "El evento debe tener un nombre maximo a 100 caracteres")
        String nombre,

        @NotBlank(message = "Debe agergar una descripcion a el evento")
        @Length(min=5,max=250,message = "La descripcion tiene un maximo de 250 caracteres")
        String descripcion,

        @NotBlank(message = "Debe agregar la direccion del evento")
        @Length(min=5,max = 45,message = "La direccion deben tener un maximo de 45 caracteres")
        String direccion,

        @NotNull(message = "Debe agregar las imagenes para las localidades del evento")
        String imagenesLocalidades,

        @NotNull(message = "Debe seleccionar el tipo de evento")
        TipoEvento tipoEvento,

        @NotNull(message = "Debe seleccionar el estado del evento")
        EstadoEvento estadoEvento,

        @NotNull(message = "Seleccione la fecha para el evento")
        LocalDateTime fecha,

        @NotBlank(message = "Debe agregar la ciudad donde se realizará el evento")
        String ciudad,

        @NotNull(message = "Debe agregar localidades a el evento")
        List<Localidad> listaLocalidades
) {
}
