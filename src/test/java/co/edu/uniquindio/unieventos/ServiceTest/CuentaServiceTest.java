package co.edu.uniquindio.unieventos.ServiceTest;

import co.edu.uniquindio.unieventos.dto.cuenta.CrearCuentaDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.EditarCuentaDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.InformacionCuentaDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.ItemCuentaDTO;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
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
    public void listarTest(){
        //Se obtiene la lista de las cuentas de los usuarios
        List<ItemCuentaDTO> lista = cuentaService.listarCuentas();


        //Se verifica que la lista no sea nula y que tenga 3 elementos (o los que hayan)
        assertEquals(5, lista.size());
    }


}
