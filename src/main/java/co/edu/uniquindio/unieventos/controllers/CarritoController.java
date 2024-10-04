package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.carrito.CrearCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.DetalleCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.model.documents.Carrito;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import co.edu.uniquindio.unieventos.service.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    @PostMapping("/crear")
    public ResponseEntity<MensajeDTO<String>> crearCarrito(@Valid @RequestBody CrearCarritoDTO carritoDTO) throws CarritoException {
        String carritoId = carritoService.crearCarrito(carritoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Carrito creado exitosamente, ID: " + carritoId));
    }

    @GetMapping("/obtener/{idUsuario}")
    public ResponseEntity<MensajeDTO<Carrito>> obtenerCarritoPorUsuario(@PathVariable String idUsuario) throws CarritoException {
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    @PutMapping("/agregar-items/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> agregarItemsAlCarrito(@PathVariable String idUsuario, @Valid @RequestBody List<DetalleCarritoDTO> itemsCarritoDTO) throws CarritoException {
        carritoService.agregarItemsAlCarrito(idUsuario, itemsCarritoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Items agregados exitosamente al carrito."));
    }

    @DeleteMapping("/eliminar-item/{idUsuario}/{nombreLocalidad}")
    public ResponseEntity<MensajeDTO<String>> eliminarItemDelCarrito(@PathVariable String idUsuario, @PathVariable String nombreLocalidad) throws CarritoException {
        carritoService.eliminarItemDelCarrito(idUsuario, nombreLocalidad);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Item eliminado del carrito exitosamente."));
    }

    @DeleteMapping("/vaciar/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> vaciarCarrito(@PathVariable String idUsuario) throws CarritoException {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Carrito vaciado exitosamente."));
    }

    @GetMapping("/listar-productos/{idUsuario}")
    public ResponseEntity<MensajeDTO<List<DetalleCarrito>>> listarProductosEnCarrito(@PathVariable String idUsuario) throws CarritoException {
        List<DetalleCarrito> itemsCarrito = carritoService.listarProductosEnCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, itemsCarrito));
    }

    // Método para calcular el total del carrito
    @GetMapping("/total/{idUsuario}")
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