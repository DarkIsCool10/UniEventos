package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CambiarPasswordDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearCuentaDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.LoginDTO;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.ItemsCuponDTO;
import co.edu.uniquindio.unieventos.dto.email.EmailDTO;
import co.edu.uniquindio.unieventos.dto.evento.EventoFiltradoDTO;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
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

    /**
     * Metodo para enviar emails
     * @param emailDTO
     * @return Mensaje de confirmacion o error segun el reultado de el envio
     */
    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO<String>> enviarCorreo(@RequestBody EmailDTO emailDTO) throws EmailException  {
        try {
            emailService.enviarCorreo(emailDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true,"Correo enviado exitosamente."));
        } catch (EmailException e) {
            return ResponseEntity.status(500).body(new MensajeDTO<>(false, e.getMessage()));
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

    @GetMapping("/filtrar-eventos")
    public ResponseEntity<MensajeDTO<List<EventoFiltradoDTO>>> filtrarEventos(@Valid @RequestBody EventoFiltradoDTO eventoFiltradoDTO) throws EventoException {
        List<EventoFiltradoDTO> eventosFiltrados = eventoService.filtrarEventos(eventoFiltradoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(true, eventosFiltrados));
    }

}
