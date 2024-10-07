package co.edu.uniquindio.unieventos.controllers;


import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.OrdenException;
import co.edu.uniquindio.unieventos.model.documents.Orden;
import co.edu.uniquindio.unieventos.service.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    private OrdenService ordenService;

    @PostMapping("/crear")
    public ResponseEntity<MensajeDTO<String>> crearOrden(@Valid @RequestBody CrearOrdenDTO ordenDTO) throws OrdenException {
        try {
        String ordenId = ordenService.crearOrden(ordenDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Orden creada exitosamente, ID: " + ordenId));
        }catch (OrdenException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    // Mostrar orden
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<Orden>> mostrarOrden(@Valid @PathVariable String id) throws OrdenException {
        Orden orden = ordenService.obtenerOrdenPorId(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, orden));
    }

    // Actualizar una orden existente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<MensajeDTO<String>> actualizarOrden(@Valid @PathVariable String id, @Valid @RequestBody EditarOrdenDTO ordenDTO) throws OrdenException {
        try {
            ordenService.actualizarOrden(id, ordenDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Orden actualizada exitosamente."));
        }catch (OrdenException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    // Eliminar orden
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarOrden(@Valid @PathVariable String id) throws OrdenException {
        try {
            ordenService.eliminarOrden(id);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Orden eliminada exitosamente."));
        }catch (OrdenException e){
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    // Listar todas las órdenes
    /*
    @GetMapping("/listar")
    public ResponseEntity<List<Orden>> listarOrdenes() {
        List<Orden> ordenes = ordenService.listarOrdenes();
        return ResponseEntity.ok(ordenes);
    }
     */
}