package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.CuponException;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.repository.CuponRepository;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuponServiceImp implements CuponService{

    private final CuponRepository cuponRepository;

    public String crearCupon(CrearCuponDTO cuponDTO) throws CuponException {
        // Verifica si ya existe un cupon con el mismo codigo
        if (cuponRepository.existsByCode(cuponDTO.codigo())) {
            throw new CuponException("Ya existe un cupon registrada con este codigo.");
        }

        // Crear instancia de Cupon con datos del DTO
        Cupon nuevoCupon = new Cupon();
        nuevoCupon.setNombre(cuponDTO.nombre());
        nuevoCupon.setCodigo(cuponDTO.codigo());
        nuevoCupon.setFechaVencimiento(cuponDTO.fechaVencimiento());
        nuevoCupon.setDescuento(cuponDTO.porcentajeDescuento());
        nuevoCupon.setTipo(cuponDTO.tipoCupon());
        nuevoCupon.setEstado(cuponDTO.estadoCupon());

        // Guardar el cupon en la base de datos
        cuponRepository.save(nuevoCupon);

        return "Cupon creado exitosamente";
    }

    public String editarCupon(EditarCuponDTO cuponDTO, String cuponId) throws CuponException {
        // Validar que el DTO no sea nulo y que el ID no sea nulo
        if (cuponDTO == null || cuponId == null || cuponId.isEmpty()) {
            throw new CuponException("Los datos de cuenta o el id no pueden ser nulos");
        }

        // Buscar el cupon en la base de datos usando un identificador único
        Cupon cuponExistente = cuponRepository.findById(cuponId)
                .orElseThrow(() -> new CuponException("Cuenta no encontrada"));

        // Actualizar los datos del objeto Cupon
        cuponExistente.setNombre(cuponDTO.nombre());
        cuponExistente.setCodigo(cuponDTO.codigo());
        cuponExistente.setEstado(cuponDTO.estadoCupon());
        cuponExistente.setTipo(cuponDTO.tipoCupon());
        cuponExistente.setDescuento(cuponDTO.porcentajeDescuento());
        cuponExistente.setFechaVencimiento(cuponDTO.fechaVencimiento());

        // Guardar los cambios en la base de datos
        cuponRepository.save(cuponExistente);

        return "Cupon actualizado con éxito";
    }

    public String eliminarCupon(String id) throws CuponException {
        // Validar que el ID no sea nulo o vacío
        if (id == null || id.isEmpty()) {
            throw new CuponException("El ID del cupon no puede ser nulo o vacío");
        }

        // Buscar el cupon en la base de datos usando el ID proporcionado
        Cupon cuponExistente = cuponRepository.findById(id)
                .orElseThrow(() -> new CuponException("Cupon no encontrado"));

        // Eliminar el cupon
        cuponRepository.delete(cuponExistente);

        return "Cupon eliminado con éxito";
    }

    public InformacionCuponDTO obtenerInformacionCupon(String id) throws CuponException {
        // Validar que el ID no sea nulo o vacío
        if (id == null || id.isEmpty()) {
            throw new CuponException("El ID del cupon no puede ser nulo o vacío");
        }
        Cupon cuponABuscar = cuponRepository.findById(id)
                .orElseThrow(() -> new CuponException("Cupon no encontrado"));

        return new InformacionCuponDTO(
                cuponABuscar.getNombre(),
                cuponABuscar.getCodigo(),
                cuponABuscar.getFechaVencimiento(),
                cuponABuscar.getDescuento(),
                cuponABuscar.getTipo(),
                cuponABuscar.getEstado()
        );
    }
}
