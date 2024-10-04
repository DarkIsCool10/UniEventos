package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.PQR.*;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.service.service.PQRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/PQR")
public class PQRController {

    @Autowired
    private PQRService PQRService;

    /**
     * Método para crear una nueva PQR.
     * @param crearPQRDTO Datos de la PQR a crear.
     * @return La PQR creada.
     */
    @PostMapping
    public ResponseEntity<?> crearPQR(@RequestBody CrearPQRDTO crearPQRDTO) {
        try {
            PQRService.crearPQR(crearPQRDTO);
            return new ResponseEntity<>("PQR creada con éxito", HttpStatus.CREATED);
        } catch (PQRException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método para obtener información de una PQR por su ID.
     * @param id ID de la PQR.
     * @return Información de la PQR.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInformacionPQR(@PathVariable String id) {
        try {
            InformacionPQRDTO informacionPQRDTO = PQRService.obtenerInformacionPQR(id);
            return new ResponseEntity<>(informacionPQRDTO, HttpStatus.OK);
        } catch (PQRException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método para listar todas las PQRs.
     * @return Lista de PQRs.
     */
    @GetMapping
    public ResponseEntity<List<ItemPQRDTO>> listarPQRs() {
        try {
            List<ItemPQRDTO> pqrList = PQRService.listarPQRs();
            return new ResponseEntity<>(pqrList, HttpStatus.OK);
        } catch (PQRException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método para responder una PQR.
     * @param responderPQRDTO Datos de respuesta a la PQR.
     * @return Mensaje de éxito.
     */
    @PutMapping("/responder")
    public ResponseEntity<?> responderPQR(@RequestBody ResponderPQRDTO responderPQRDTO) {
        try {
            String mensaje = PQRService.responderPQR(responderPQRDTO);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (PQRException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método para eliminar una PQR por su ID.
     * @param id ID de la PQR a eliminar.
     * @return Mensaje de éxito.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPQR(@PathVariable String id) {
        try {
            String mensaje = PQRService.eliminarPQR(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (PQRException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}