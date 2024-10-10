package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.PQR.CrearPQRDTO;
import co.edu.uniquindio.unieventos.dto.PQR.InformacionPQRDTO;
import co.edu.uniquindio.unieventos.dto.PQR.ItemPQRDTO;
import co.edu.uniquindio.unieventos.dto.carrito.CrearCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.DetalleCarritoDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import co.edu.uniquindio.unieventos.exceptions.CuponException;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.model.documents.Carrito;
import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import co.edu.uniquindio.unieventos.model.documents.PQR;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import co.edu.uniquindio.unieventos.repository.CuentaRepository;
import co.edu.uniquindio.unieventos.service.service.CarritoService;
import co.edu.uniquindio.unieventos.service.service.CuentaService;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import co.edu.uniquindio.unieventos.service.service.PQRService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.event.MailEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cuenta")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CuentaController {

    @Autowired
    CuentaService cuentaService;
    @Autowired
    PQRService PQRService;
    @Autowired
    CuponService cuponService;
    @Autowired
    CarritoService carritoService;

    @PutMapping("/editar-perfil")
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@Valid @RequestBody EditarCuentaDTO cuenta) throws CuentaException{
        try {
            cuentaService.editarCuenta(cuenta);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
        }catch (CuentaException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta(@Valid @PathVariable String id) throws CuentaException {
        try {
            cuentaService.eliminarCuenta(id);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
        }catch (CuentaException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @GetMapping("/obtener-informacion/{id}")
    public ResponseEntity<MensajeDTO<InformacionCuentaDTO>> obtenerInformacionCuenta(@Valid @PathVariable String id) throws CuentaException{
        InformacionCuentaDTO info = cuentaService.obtenerInformacionCuenta(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, info));
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


    //==================================== METODOS PQR =============================================//

    /**
     * Método para crear una nueva PQR.
     * @param crearPQRDTO Datos de la PQR a crear.
     * @return La PQR creada.
     */
    @PostMapping("/crear-pqr")
    public ResponseEntity<MensajeDTO<String>> crearPQR(@Valid @RequestBody CrearPQRDTO crearPQRDTO) {
        try {
            PQRService.crearPQR(crearPQRDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true,"PQR creada exitosamente"));
        } catch (PQRException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false,e.getMessage()));
        }
    }

    /**
     * Método para listar todas las PQRs.
     * @return Lista de PQRs.
     */
    @GetMapping("/listar-pqr")
    public ResponseEntity<MensajeDTO<List<ItemPQRDTO>>> listarPQRs() throws PQRException {
        List<ItemPQRDTO> pqrList = PQRService.listarPQRs();
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true,pqrList));
    }

    @GetMapping("/obtener-pqr-usuario/{id}")
    public ResponseEntity<MensajeDTO<List<PQR>>> obtenerPQRsPorUsuario(@Valid @PathVariable String id) throws PQRException {
        List<PQR> pqrsObtenidos = PQRService.obtenerPQRsPorUsuario(id);
        return ResponseEntity.ok(new MensajeDTO<>(true, pqrsObtenidos));
    }

    //==================================== METODOS CUPON =============================================//

    @PostMapping("/aplicar-cupon")
    public ResponseEntity<MensajeDTO<String>> aplicarCupon(@RequestParam String codigoCupon, @RequestParam LocalDateTime fechaCompra) throws CuponException {
        try {
            cuponService.aplicarCupon(codigoCupon, fechaCompra);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Cupón aplicado con éxito"));
        } catch (CuponException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

        //==================================== METODOS CARRITO =============================================//

    @GetMapping("/obtener-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<Carrito>> obtenerCarritoPorUsuario(@PathVariable String idUsuario) throws CarritoException {
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    @PutMapping("/agregar-items-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> agregarItemsAlCarrito(@PathVariable String idUsuario, @Valid @RequestBody List<DetalleCarritoDTO> itemsCarritoDTO) throws CarritoException {
        carritoService.agregarItemsAlCarrito(idUsuario, itemsCarritoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Items agregados exitosamente al carrito."));
    }

    @DeleteMapping("/eliminar-item-carrito/{idUsuario}/{nombreLocalidad}")
    public ResponseEntity<MensajeDTO<String>> eliminarItemDelCarrito(@PathVariable String idUsuario, @PathVariable String nombreLocalidad) throws CarritoException {
        carritoService.eliminarItemDelCarrito(idUsuario, nombreLocalidad);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Item eliminado del carrito exitosamente."));
    }

    @DeleteMapping("/vaciar-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> vaciarCarrito(@PathVariable String idUsuario) throws CarritoException {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Carrito vaciado exitosamente."));
    }

    @GetMapping("/listar-productos-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<List<DetalleCarrito>>> listarProductosEnCarrito(@PathVariable String idUsuario) throws CarritoException {
        List<DetalleCarrito> itemsCarrito = carritoService.listarProductosEnCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, itemsCarrito));
    }

    // Método para calcular el total del carrito
    @GetMapping("/total-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<Double>> calcularTotalCarrito(@PathVariable String idUsuario) throws CarritoException {
        double total = carritoService.calcularTotalCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, total));
    }

    // Método para validar la disponibilidad de entradas
    @GetMapping("/validar-disponibilidad/{idUsuario}")
    public ResponseEntity<MensajeDTO<Boolean>> validarDisponibilidadEntradas(@PathVariable String idUsuario) throws CarritoException {
        boolean disponible = carritoService.validarDisponibilidadEntradas(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, disponible));
    }

}
