package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import co.edu.uniquindio.unieventos.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepo;

    /**
     * Metodo para agregar un usuario a la base de datos
     * @param usuario
     * @return la respuesta de ese ususario creado a la base de datos
     */
    @PostMapping
    public ResponseEntity<?> saveUsuario(@RequestBody Cuenta cuenta){
        try {
            Cuenta userSave = cuentaRepo.save(cuenta);
            return new ResponseEntity<Cuenta>(userSave, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la lista de usuario de la base de datos
     * @return la lista de los ususarios
     */
    @GetMapping
    public ResponseEntity<?> findAllUsuarios(){
        try {
            List<Cuenta> cuentas = cuentaRepo.findAll();
            return new ResponseEntity<List<Cuenta>>(cuentas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un usuario segun el id necesitado
     * @param id
     * @return el usuario encontrado por su id
     */
    @GetMapping(value ="/{id}")
    public ResponseEntity<?> findUsuarioById(@RequestParam String id){
        try{
            Optional<Cuenta> cuentas = cuentaRepo.findById(id);
            return new ResponseEntity<String>("Cuenta encontrada correctamente",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza los datos de un usuario
     * @param usuario
     * @return la respuesta con el usuario actualizado
     */
    @PutMapping
    public ResponseEntity<?> updateUsuario(@RequestBody Cuenta cuenta){
        try {
            Cuenta userSave = cuentaRepo.save(cuenta);
            return new ResponseEntity<Cuenta>(userSave, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un usuario por su id
     * @param id
     * @return mensaje de avuso de que el usuario fue eliminado
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") String id){
        try {
            cuentaRepo.deleteById(id);
            return new ResponseEntity<String>("Fue eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
