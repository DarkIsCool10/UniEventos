package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.model.documents.Carrito;
import co.edu.uniquindio.unieventos.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Carrito")
public class CarritoController {

    @Autowired
    private CarritoRepository carritoRepo;

    /**
     * Metodo para agregar un carrito a la base de datos
     * @param carrito
     * @return la respuesta de ese carrito creado a la base de datos
     */
    @PostMapping
    public ResponseEntity<?> saveCarrito(@RequestBody Carrito carrito) {
        try {
            Carrito carritoGuardado = carritoRepo.save(carrito);
            return new ResponseEntity<Carrito>(carritoGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la lista de carritos de la base de datos
     * @return la lista de los carritos
     */
    @GetMapping
    public ResponseEntity<?> findAllCarritos() {
        try {
            List<Carrito> carritos = carritoRepo.findAll();
            return new ResponseEntity<List<Carrito>>(carritos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un carrito según el id necesario
     * @param id
     * @return el carrito encontrado por su id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findCarritoById(@PathVariable("id") String id) {
        try {
            Optional<Carrito> carrito = carritoRepo.findById(id);
            if (carrito.isPresent()) {
                return new ResponseEntity<Carrito>(carrito.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Carrito no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza los datos de un carrito
     * @param carrito
     * @return la respuesta con el carrito actualizado
     */
    @PutMapping
    public ResponseEntity<?> updateCarrito(@RequestBody Carrito carrito) {
        try {
            Carrito carritoActualizado = carritoRepo.save(carrito);
            return new ResponseEntity<Carrito>(carritoActualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un carrito por su id
     * @param id
     * @return mensaje de aviso de que el carrito fue eliminado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarrito(@PathVariable("id") String id) {
        try {
            carritoRepo.deleteById(id);
            return new ResponseEntity<String>("Carrito eliminado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
