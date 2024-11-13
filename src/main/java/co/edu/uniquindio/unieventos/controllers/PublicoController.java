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

    /**
     * Metodo para crear la cuenta de un usuario
     * @param cuentaDTO
     * @return El DTO de la cuenta creada
     * @throws CuentaException
     * @throws CarritoException
     */
    @PostMapping("/crear-cuenta")
    public ResponseEntity<MensajeDTO<String>> crearCuenta(@Valid @RequestBody CrearCuentaDTO cuentaDTO) throws CuentaException, CarritoException {
        try {
            cuentaService.crearCuenta(cuentaDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Cuenta creada correctamente"));
        } catch (CuentaException | CarritoException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @PostMapping("/validar-codigo-registro")
    public ResponseEntity<MensajeDTO<String>> validarCodigo(@Valid @RequestBody ValidarCodigoDTO validarCodigoDTO)throws CuentaException {
        try {
            cuentaService.validarCodigo(validarCodigoDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Cuenta activada con exito"));
        }catch (CuentaException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @PostMapping("/enviar-codigo-recuperacion-contasenia")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacion(@Valid @RequestBody CodigoContraseniaDTO codigoContraseniaDTO) throws CuentaException{
        try {
            cuentaService.enviarCodigoRecuperacionPassword(codigoContraseniaDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Codigo enviado con a su emial correctamente"));
        }catch (CuentaException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, "Error al enviar el correo"));
        }
    }

    /**
     * Metodo para modificar la contraseña, ya sea usuario o admin
     * @param cambiarPasswordDTO
     * @return El DTO con la password modificada
     * @throws CuentaException
     */
    @PutMapping("/cambiar-password")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@Valid @RequestBody CambiarPasswordDTO cambiarPasswordDTO) throws CuentaException{
        try {
            cuentaService.cambiarPassword(cambiarPasswordDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña modificada con exito"));
        }catch (CuentaException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    //==================================== METODOS AUTENTICACION =============================================//

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<MensajeDTO<TokenDTO>> iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) throws Exception{
        TokenDTO token = cuentaService.iniciarSesion(loginDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, token));
    }

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
    public ResponseEntity<MensajeDTO<Evento>> obtenerInfoEvento(@Valid @PathVariable String id) throws EventoException {
        Evento info = eventoService.obtenerInformacionEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(true, info ));
    }

}
