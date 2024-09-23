package co.edu.uniquindio.unieventos.controllers;


import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.model.enums.EstadoCupon;
import co.edu.uniquindio.unieventos.model.enums.TipoCupon;
import co.edu.uniquindio.unieventos.repository.CuponRepository;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Cupon")
public class CuponController {

    @Autowired
    private CuponRepository cuponRepo;

    @Autowired
    private CuponService cuponService;


    @PostMapping
    public ResponseEntity<?> saveCupon(@RequestBody Cupon cupon){
        try {
            Cupon cuponSave = cuponRepo.save(cupon);
            return new ResponseEntity<Cupon>(cuponSave, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCupon(@RequestBody Cupon cupon) {
        try {
            Cupon cuponSave = cuponRepo.save(cupon);
            return new ResponseEntity<Cupon>(cuponSave, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> obtenerCuponesFiltrados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String fechaVencimiento,
            @RequestParam(required = false) String fechaApertura,
            @RequestParam(required = false) Float descuento,
            @RequestParam(required = false) TipoCupon tipo,
            @RequestParam(required = false) EstadoCupon estado) {

        try {
            // Convertir las fechas si no son nulas
            LocalDateTime fechaVencimientoParsed = (fechaVencimiento != null) ? LocalDateTime.parse(fechaVencimiento) : null;
            LocalDateTime fechaAperturaParsed = (fechaApertura != null) ? LocalDateTime.parse(fechaApertura) : null;

            List<Cupon> cupones = cuponService.obtenerCuponesFiltrados(
                    nombre,
                    fechaVencimientoParsed,
                    fechaAperturaParsed,
                    descuento,
                    tipo,
                    estado
            );

            if (cupones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(cupones, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
