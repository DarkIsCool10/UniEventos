package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.ItemsCuponDTO;
import co.edu.uniquindio.unieventos.dto.email.EmailDTO;
import co.edu.uniquindio.unieventos.dto.evento.EventoFiltradoDTO;
import co.edu.uniquindio.unieventos.dto.evento.FiltroEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.service.service.CuentaService;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import co.edu.uniquindio.unieventos.service.service.EmailService;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publico")
@RequiredArgsConstructor
public class PublicoController {

    @Autowired
    CuentaService cuentaService;
    @Autowired
    EmailService emailService;
    @Autowired
    CuponService cuponService;
    @Autowired
    EventoService eventoService;

    //==================================== METODOS CUENTA =============================================//



    //==================================== METODOS EVENTO =============================================//

    @PostMapping("/filtrar-eventos")
    public ResponseEntity<MensajeDTO<List<EventoFiltradoDTO>>> filtrarEventos(@RequestBody FiltroEventoDTO filtroEventoDTO) throws EventoException {
        List<EventoFiltradoDTO> eventosFiltrados = eventoService.filtrarEventos(filtroEventoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(true, eventosFiltrados));
    }

    @GetMapping("/listar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventos() throws EventoException {
        List<ItemEventoDTO> eventos = eventoService.listarEventos();
        return ResponseEntity.ok(new MensajeDTO<>(false, eventos));
    }

    @GetMapping("/obtener-evento/{id}")
    public ResponseEntity<Evento> obtenerInfoEvento(@Valid @PathVariable("id") String id) throws EventoException {
        return ResponseEntity.ok(eventoService.obtenerInformacionEvento(id));
    }

}
