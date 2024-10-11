package co.edu.uniquindio.unieventos.ServiceTest;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import co.edu.uniquindio.unieventos.model.enums.EstadoCuenta;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import co.edu.uniquindio.unieventos.repository.CuentaRepository;
import co.edu.uniquindio.unieventos.service.implement.CuentaServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CuentaServiceTest {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private CuentaServiceImp cuentaService;

    @Test
    public void actualizarCuentaTest(){

        //Se define el id de la cuenta del usuario a actualizar, este id está en el dataset.js
        String idCuenta = "66a2a9aaa8620e3c1c5437be";

        //Se crea un objeto de tipo EditarCuentaDTO
        EditarCuentaDTO editarCuentaDTO = new EditarCuentaDTO(
                idCuenta,
                "Pepito perez",
                "12121",
                "Nueva dirección",
                "password"
        );

        assertDoesNotThrow(() -> {
            //Se actualiza la cuenta del usuario con el id definido
            cuentaService.editarCuenta(editarCuentaDTO);

            //Obtenemos el detalle de la cuenta del usuario con el id definido
            InformacionCuentaDTO detalle = cuentaService.obtenerInformacionCuenta(idCuenta);

            //Se verifica que la dirección del usuario sea la actualizada
            assertEquals("Nueva dirección", detalle.direccion());
        });
    }

    @Test
    public void crearCuentaTest(){

        CrearCuentaDTO cuentaDTO = new CrearCuentaDTO(
               "25023",
               "Juan Herrera Hemocho",
               "3245642384",
               "igual",
               "juanH@email.com",
               "contraseña"
        );

        // Primero, verificamos que no se lance ninguna excepción al crear la cuenta
        assertDoesNotThrow(() -> cuentaService.crearCuenta(cuentaDTO));
    }

    @Test
    public void eliminarCuentaTest() throws CuentaException {

        String idCuenta = "66e2f7338a612f6cda3acad2";

        assertDoesNotThrow(() -> cuentaService.eliminarCuenta(idCuenta));

    }

    @Test
    public void validarCodigo_CodigoCorrecto_ActivaCuenta() throws CuentaException {
        // Crear y guardar una cuenta con un código de verificación
        Cuenta cuenta = new Cuenta();
        cuenta.setEmail("test@example.com");
        cuenta.setCodigoVerificacionRegistro("123456");
        cuenta.setEstado(EstadoCuenta.INACTIVO);
        cuentaRepository.save(cuenta);

        // Crear DTO con el email y código correctos
        ValidarCodigoDTO validarCodigoDTO = new ValidarCodigoDTO("test@example.com", "123456");

        // Ejecutar el método a probar
        ValidarCodigoDTO resultado = cuentaService.validarCodigo(validarCodigoDTO);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(validarCodigoDTO.email(), resultado.email());

        // Verificar que la cuenta ahora está activa
        Cuenta cuentaActivada = cuentaRepository.findByEmail(validarCodigoDTO.email()).orElse(null);
        assertNotNull(cuentaActivada);
        assertEquals(EstadoCuenta.ACTIVO, cuentaActivada.getEstado());
        assertNull(cuentaActivada.getCodigoVerificacionRegistro());
    }

    @Test
    public void editarCuenta_CuentaExistente_ActualizaDatos() throws CuentaException {
        // Crear y guardar una cuenta inicial
        Cuenta cuenta = new Cuenta();
        cuenta.setEmail("test@example.com");
        cuenta.setPassword("123456");
        cuenta.setUsuario(new Usuario("123456789", "Juan Pérez", "3001234567", "Calle 1"));
        cuentaRepository.save(cuenta);

        // Crear DTO con los nuevos datos
        EditarCuentaDTO cuentaEditada = new EditarCuentaDTO(
                cuenta.getId(), // ID de la cuenta existente
                "Juan Carlos Pérez", // Nuevo nombre
                "3007654321", // Nuevo teléfono
                "Calle 2", // Nueva dirección
                "nuevaPassword" // Nueva contraseña
        );

        // Ejecutar el método a probar
        cuentaService.editarCuenta(cuentaEditada);

        // Verificar que la cuenta se haya actualizado correctamente
        Cuenta cuentaModificada = cuentaRepository.findById(cuenta.getId()).orElse(null);
        assertNotNull(cuentaModificada);
        assertEquals("Juan Carlos Pérez", cuentaModificada.getUsuario().getNombre());
        assertEquals("3007654321", cuentaModificada.getUsuario().getTelefono());
        assertEquals("Calle 2", cuentaModificada.getUsuario().getDireccion());
        // Verificar que la contraseña esté encriptada
        assertNotEquals("nuevaPassword", cuentaModificada.getPassword());
    }

    @Test
    public void obtenerInformacionCuenta_CuentaExistente_RetornaInformacion() throws CuentaException {
        // Crear y guardar una cuenta inicial
        Cuenta cuenta = new Cuenta();
        cuenta.setEmail("test@example.com");
        cuenta.setPassword("123456");
        cuenta.setUsuario(new Usuario("123456789", "Juan Pérez", "3001234567", "Calle 1"));
        cuentaRepository.save(cuenta);

        // Obtener la información de la cuenta utilizando su ID
        InformacionCuentaDTO informacionCuenta = cuentaService.obtenerInformacionCuenta(cuenta.getId());

        // Verificar que se devuelven los datos correctos
        assertNotNull(informacionCuenta);
        assertEquals(cuenta.getId(), informacionCuenta.id());
        assertEquals(cuenta.getUsuario().getCedula(), informacionCuenta.cedula());
        assertEquals(cuenta.getUsuario().getNombre(), informacionCuenta.nombre());
        assertEquals(cuenta.getUsuario().getTelefono(), informacionCuenta.telefono());
        assertEquals(cuenta.getUsuario().getDireccion(), informacionCuenta.direccion());
        assertEquals(cuenta.getEmail(), informacionCuenta.email());
    }

}
