package co.edu.uniquindio.unieventos.ServiceTest;

import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.CrearLocalidadDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventoServiceTest {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private EventoService eventoService;

    @Test
    public void CrearEventoTest() throws EventoException {

        List<CrearLocalidadDTO> listaLocalidades = new ArrayList<>();
            listaLocalidades.add(new CrearLocalidadDTO("Platea", 100, 50.000d));
            listaLocalidades.add(new CrearLocalidadDTO("General", 200, 200.000d));
            listaLocalidades.add(new CrearLocalidadDTO("VIP", 50, 100.000d));


        CrearEventoDTO crearEventoDTO = new CrearEventoDTO(
                "url-imagenPortada",
                "Evento de rock",
                "Concierto de rock en vivo",
                "Av. Principal 123",
                "url-imagenLocalidades",
                TipoEvento.CONCIERTO,
                EstadoEvento.ACTIVO,
                LocalDate.now(),
                "Ciudad de ejemplo",
                listaLocalidades
        );

        System.out.println(crearEventoDTO.listaLocalidades());
        eventoService.crearEvento(crearEventoDTO);

        assertNotNull(crearEventoDTO);
    }

    @Test
    public void editarEvento_EventoExistente_ModificaEvento() throws EventoException {
        // Crear y guardar un evento inicial
        Evento evento = new Evento();
        evento.setId("1");
        evento.setNombre("Evento Original");
        evento.setDescripcion("Descripción Original");
        evento.setEstado(EstadoEvento.ACTIVO);
        eventoRepository.save(evento);

        // Crear un DTO de edición con los nuevos datos
        EditarEventoDTO editarEventoDTO = new EditarEventoDTO();
        editarEventoDTO.id();
        editarEventoDTO.nombre();
        editarEventoDTO.descripcion();
        editarEventoDTO.estadoEvento();
        // Aquí puedes añadir más datos si es necesario, como localidades, imágenes, etc.

        // Editar el evento
        eventoService.editarEvento(editarEventoDTO);

        // Verificar que el evento se ha modificado correctamente
        Evento eventoActualizado = eventoRepository.findById("1").orElseThrow();
        assertEquals("Evento Modificado", eventoActualizado.getNombre());
        assertEquals("Descripción Modificada", eventoActualizado.getDescripcion());
    }

    @Test
    public void eliminarEvento_EventoExistente_EliminaEvento() throws EventoException {
        // Crear y guardar un evento inicial
        Evento evento = new Evento();
        evento.setId("1");
        evento.setNombre("Evento a Eliminar");
        evento.setEstado(EstadoEvento.ACTIVO);
        eventoRepository.save(evento);

        // Eliminar el evento
        eventoService.eliminarEvento("1");

        // Verificar que el estado del evento se ha cambiado a Eliminado
        Evento eventoEliminado = eventoRepository.findById("1").orElseThrow();
        assertEquals(EstadoEvento.ELIMINADO, eventoEliminado.getEstado());
    }

    @Test
    public void obtenerInformacionEvento_EventoExistente_RetornaEvento() throws EventoException {
        // Crear y guardar un evento inicial
        Evento evento = new Evento();
        evento.setId("1");
        evento.setNombre("Evento de Prueba");
        eventoRepository.save(evento);

        // Obtener información del evento
        Evento eventoObtenido = eventoService.obtenerInformacionEvento("1");

        // Verificar que se obtuvo la información correcta
        assertNotNull(eventoObtenido);
        assertEquals("Evento de Prueba", eventoObtenido.getNombre());
    }

}
