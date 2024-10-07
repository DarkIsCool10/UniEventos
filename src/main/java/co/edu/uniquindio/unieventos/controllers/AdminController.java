package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import co.edu.uniquindio.unieventos.service.service.ImagesService;
import com.google.common.graph.MutableNetwork;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    EventoService eventoService;
    @Autowired
    ImagesService imagesService;
    @Autowired
    CuponService cuponService;

    @PostMapping("/crear-evento")
    public ResponseEntity<MensajeDTO<String>> crearEvento(@Valid @RequestBody CrearEventoDTO eventoDTO) throws EventoException {
        try {
            eventoService.crearEvento(eventoDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Evento creado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @PutMapping("/editar-evento")
    public ResponseEntity<MensajeDTO<String>> editarEvento(@Valid @RequestBody EditarEventoDTO eventoDTO) throws EventoException {
        try{
            eventoService.editarEvento(eventoDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Evento modificado exitosamente"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
            }
    }

    @DeleteMapping("/eliminar-evento/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarEvento(@Valid @PathVariable String id) throws EventoException {
        try{
            eventoService.eliminarEvento(id);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Evento eliminado exitosamente"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @GetMapping("/obtener-evento/{id}")
    public ResponseEntity<MensajeDTO<Evento>> obtenerInfoEvento(@Valid @PathVariable String id) throws EventoException {
        Evento info = eventoService.obtenerInformacionEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(true, info ));
    }

    @GetMapping("/obtener-localidad/{nombre}")
    public ResponseEntity<MensajeDTO<Localidad>> obtenerLocalidad(@Valid @PathVariable String nombre) throws EventoException {
        Localidad info = eventoService.obtenerLocalidadPorNombre(nombre);
        return ResponseEntity.ok(new MensajeDTO<>(true, info ));
    }

    @GetMapping("/listar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventos() throws EventoException {
        List<ItemEventoDTO> eventos = eventoService.listarEventos();
        return ResponseEntity.ok(new MensajeDTO<>(false, eventos));

    }

    @GetMapping("/filtrar-evento-tipo/{tipoEvento}")
    public ResponseEntity<MensajeDTO<List<EventoFiltradoDTO>>> filtrarTipoEvento(@Valid @PathVariable TipoEvento tipoEvento) throws EventoException {
        List<EventoFiltradoDTO> eventoPorTipo = eventoService.filtrarPorTipo(tipoEvento);
        return ResponseEntity.ok(new MensajeDTO<>(true, eventoPorTipo));
    }

    //TODO PREGUNTAR SOBRE COMO FILTRAR CORRECTAMENTE LAS FECHAS Y AUTENTICACION CON TOKEN

    @GetMapping("/filtrar-evento-fecha/{fechaEvento}")
    public ResponseEntity<MensajeDTO<List<EventoFiltradoDTO>>> filtrarFechaEvento(@Valid @PathVariable LocalDate fecha) throws EventoException {
        List<EventoFiltradoDTO> eventoPorFecha = eventoService.filtrarPorFecha(fecha);
        return ResponseEntity.ok(new MensajeDTO<>(true,eventoPorFecha));
    }

    @GetMapping("/filtrar-evento-ciudad/{ciudad}")
    public ResponseEntity<MensajeDTO<List<EventoFiltradoDTO>>> filtrarCiudadEvento(@Valid @PathVariable String ciudad) throws EventoException {
        List<EventoFiltradoDTO> eventoPorCiudad = eventoService.filtrarPorCiudad(ciudad);
        return ResponseEntity.ok(new MensajeDTO<>(true,eventoPorCiudad));
    }

    @GetMapping("/filtrar-evento-por-rango")
    public ResponseEntity<MensajeDTO<List<EventoFiltradoDTO>>> filtrarPorRangoDeFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        List<EventoFiltradoDTO> eventos = eventoService.filtrarPorRangoDeFechas(desde, hasta);
        return ResponseEntity.ok(new MensajeDTO<>(true,eventos));
    }
}
