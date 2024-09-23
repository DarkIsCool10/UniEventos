package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.ItemsCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.CuponException;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.model.enums.EstadoCupon;
import co.edu.uniquindio.unieventos.model.enums.TipoCupon;
import co.edu.uniquindio.unieventos.repository.CuponRepository;
import co.edu.uniquindio.unieventos.service.service.CuponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuponServiceImp implements CuponService{

    private final CuponRepository cuponRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public String crearCupon(CrearCuponDTO cuponDTO) throws CuponException {
        // Verifica si ya existe un cupon con el mismo codigo
        if (cuponRepository.existsByCodigo(cuponDTO.codigo())) {
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
                cuponABuscar.getDescuento(),
                cuponABuscar.getFechaApertura(),
                cuponABuscar.getFechaVencimiento(),
                cuponABuscar.getTipo(),
                cuponABuscar.getEstado()
        );
    }

    public List<Cupon> obtenerCuponesFiltrados(String nombre, LocalDateTime fechaVencimiento, LocalDateTime fechaApertura, Float descuento, TipoCupon tipo, EstadoCupon estado) {
        List<Cupon> cupones = cuponRepository.findAll();

        // Aplica filtros condicionalmente
        if (nombre != null && !nombre.isEmpty()) {
            cupones = cuponRepository.findByNombreContainingIgnoreCase(nombre);
        }
        if (fechaVencimiento != null) {
            cupones = cuponRepository.findByFechaVencimientoAfter(fechaVencimiento);
        }
        if (fechaApertura != null) {
            cupones = cuponRepository.findByFechaAperturaAfter(fechaApertura);
        }
        if (descuento != null) {
            cupones = cuponRepository.findByDescuento(descuento);
        }
        if (tipo != null) {
            cupones = cuponRepository.findByTipo(tipo);
        }
        if (estado != null) {
            cupones = cuponRepository.findByEstado(estado);
        }

        return cupones;
    }

    public String aplicarCupon(String codigoCupon, LocalDateTime fechaCompra) throws CuponException {
        // Buscar el cupón por código
        Optional<Cupon> cuponOpt = cuponRepository.findById(codigoCupon);

        // Verificar si el cupón existe
        if (cuponOpt.isEmpty()) {
            throw new CuponException("Cupón no existe.");
        }

        Cupon cupon = cuponOpt.get();

        // Verificar si el cupón está disponible
        if (cupon.getEstado() != EstadoCupon.DISPONIBLE) {
            throw new CuponException("El cupón no está disponible.");
        }

        // Verificar si el cupón ha expirado
        if (cupon.getFechaVencimiento().isBefore(fechaCompra)) {
            throw new CuponException("El cupón ha expirado.");
        }

        // Verificar si el cupón es único y ya ha sido utilizado
        if (cupon.getTipo() == TipoCupon.UNICO && cupon.isUtilizado()) {
            throw new CuponException("El cupón ya ha sido utilizado.");
        }

        // Si el cupón es único, marcarlo como utilizado y guardar la actualización
        if (cupon.getTipo() == TipoCupon.UNICO) {
            cupon.setUtilizado(true);
            cuponRepository.save(cupon); // Guarda el cupón actualizado
        }

        // Si todas las validaciones pasan, aplicar el descuento
        return "Cupón aplicado con éxito. Descuento del " + cupon.getDescuento() + "%.";
    }

    @Override
    public String fechaAperturaCupon(LocalDateTime fechaApertura) throws CuponException {
        return null;
    }

}