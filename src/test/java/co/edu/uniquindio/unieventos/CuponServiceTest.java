package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.model.enums.EstadoCupon;
import co.edu.uniquindio.unieventos.model.enums.TipoCupon;
import co.edu.uniquindio.unieventos.service.implement.CuponServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CuponServiceTest {

    @Autowired
    private CuponServiceImp cuponServiceImp;

    @Test
    public void crearCuponTest() {
        CrearCuponDTO cuponDTO = new CrearCuponDTO(
            "AMAYVIVE",
            "Amor y amistad",
            10.0f,
            LocalDateTime.now().plusDays(30),
            LocalDateTime.now(),
            TipoCupon.MULTIPLE,
            EstadoCupon.DISPONIBLE
        );
        assertDoesNotThrow(() -> cuponServiceImp.crearCupon(cuponDTO));
    }

    @Test
    public void actualizarCuponTest(){

        String idCupon = "66eb4fde26fa517dc05efb45";

        EditarCuponDTO editarCuponDTO = new EditarCuponDTO(
                "Octubre",
                "Hallowen",
                10.0f,
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now(),
                TipoCupon.MULTIPLE,
                EstadoCupon.DISPONIBLE

        );

        assertDoesNotThrow(() -> {
            cuponServiceImp.editarCupon(editarCuponDTO, idCupon);
        });
    }

    @Test
    public void listarCuponesSinFiltrosTest() {
        // Probar sin filtros, debería devolver todos los cupones
        assertDoesNotThrow(() -> {
            List<Cupon> cupones = cuponServiceImp.obtenerCuponesFiltrados(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            // Se espera que la lista no esté vacía
            assertFalse(cupones.isEmpty());
        });
    }


}
