package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.PQR.InformacionPQRDTO;
import co.edu.uniquindio.unieventos.dto.PQR.ResponderPQRDTO;
import co.edu.uniquindio.unieventos.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.ItemsCuponDTO;
import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.CuponException;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import co.edu.uniquindio.unieventos.service.service.ImagesService;
import co.edu.uniquindio.unieventos.service.service.PQRService;
import com.google.common.graph.MutableNetwork;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    PQRService PQRService;

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
    public ResponseEntity<MensajeDTO<ObtenerEventoDTO>> obtenerLocalidad(@Valid @PathVariable String nombre) throws EventoException {
        ObtenerEventoDTO info = eventoService.obtenerLocalidadPorNombre(nombre);
        return ResponseEntity.ok(new MensajeDTO<>(true, info ));
    }

    //==================================== METODOS PQR =============================================//

    /**
     * Método para responder una PQR.
     * @param responderPQRDTO Datos de respuesta a la PQR.
     * @return Mensaje de éxito.
     */
    @PutMapping("/responder-pqr")
    public ResponseEntity<MensajeDTO<String>> responderPQR(@Valid @RequestBody ResponderPQRDTO responderPQRDTO) {
        try {
            PQRService.responderPQR(responderPQRDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true,"PQR respondida exitosamente"));
        } catch (PQRException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false,e.getMessage()));
        }
    }

    /**
     * Método para obtener información de una PQR por su ID.
     * @param id ID de la PQR.
     * @return Información de la PQR.
     */
    @GetMapping("/obterner-informacion-pqr/{id}")
    public ResponseEntity<MensajeDTO<InformacionPQRDTO>> obtenerInformacionPQR(@Valid @PathVariable String id) throws PQRException {
        InformacionPQRDTO informacionPQRDTO = PQRService.obtenerInformacionPQR(id);
        return ResponseEntity.ok(new MensajeDTO<>(true, informacionPQRDTO));
    }

    /**
     * Método para eliminar una PQR por su ID.
     * @param id ID de la PQR a eliminar.
     * @return Mensaje de éxito.
     */
    @DeleteMapping("/eliminar-pqr/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarPQR(@Valid @PathVariable String id) {
        try {
            PQRService.eliminarPQR(id);
            return ResponseEntity.ok(new MensajeDTO<>(true,"PQR eliminado con exito"));
        } catch (PQRException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false,e.getMessage()));
        }
    }

    //====================================== METODOS CUPON ====================================//

    @PostMapping("/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearCuponDTO cuponDTO) throws CuponException {
        try {
            cuponService.crearCupon(cuponDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true,"Cupon creado con exito"));
        } catch (CuponException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false,e.getMessage()));
            }
        }

    @PutMapping("/editar-cupon/{cuponId}")
    public ResponseEntity<MensajeDTO<String>> editarCupon(@Valid @RequestBody EditarCuponDTO cupon, @PathVariable String cuponId) throws CuponException {
        try {
            cuponService.editarCupon(cupon, cuponId);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Cupón editado con éxito"));
        } catch (CuponException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-cupon/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCupon(@PathVariable String id) throws CuponException {
        try {
            cuponService.eliminarCupon(id);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Cupón eliminado con éxito"));
        } catch (CuponException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @GetMapping("/cupon/{id}")
    public ResponseEntity<InformacionCuponDTO> obtenerInformacionCupon(@PathVariable String id) throws CuponException {
        InformacionCuponDTO cuponInfo = cuponService.obtenerInformacionCupon(id);
        return ResponseEntity.ok(cuponInfo);
    }

    @PostMapping("/filtrar-cupones")
    public ResponseEntity<List<ItemsCuponDTO>> obtenerCuponesFiltrados(@RequestBody ItemsCuponDTO itemCuponDTO) {
        List<ItemsCuponDTO> cuponesFiltrados = cuponService.obtenerCuponesFiltrados(itemCuponDTO);
        return ResponseEntity.ok(cuponesFiltrados);
    }

    //=========================================== METODOS IMAGENES ===============================================//

    @PostMapping("/subir")
    public ResponseEntity<MensajeDTO<String>> subir(@RequestParam("imagen") MultipartFile imagen) throws Exception {
        String respuesta = imagesService.subirImagen(imagen);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, respuesta));
    }

    @DeleteMapping("/eliminar/{idImagen}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String idImagen)  throws Exception{
        try {
            imagesService.eliminarImagen( idImagen );
            return ResponseEntity.ok().body(new MensajeDTO<>(false, "La imagen fue eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

}