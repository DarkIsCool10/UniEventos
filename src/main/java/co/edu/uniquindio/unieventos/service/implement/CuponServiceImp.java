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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuponServiceImp implements CuponService{

    private final CuponRepository cuponRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void crearCupon(CrearCuponDTO cuponDTO) throws CuponException {
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

        // Verificar si la fecha de apertura está presente en el DTO
        if (cuponDTO.fechaApertura() != null) {
            nuevoCupon.setFechaApertura(cuponDTO.fechaApertura());
        } else {
            // Si no se especifica, se asigna la fecha y hora actuales
            nuevoCupon.setFechaApertura(LocalDateTime.now());
        }

        // Guardar el cupon en la base de datos
        cuponRepository.save(nuevoCupon);
    }

    @Override
    public void editarCupon(EditarCuponDTO cuponDTO, String cuponId) throws CuponException {
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


        if (cuponDTO.fechaApertura() != null) {
            cuponExistente.setFechaApertura(cuponDTO.fechaApertura());
        } else {
            cuponExistente.setFechaApertura(LocalDateTime.now());
        }

        // Guardar los cambios en la base de datos
        cuponRepository.save(cuponExistente);
    }

    @Override
    public void eliminarCupon(String id) throws CuponException {
        // Validar que el ID no sea nulo o vacío
        if (id == null || id.isEmpty()) {
            throw new CuponException("El ID del cupon no puede ser nulo o vacío");
        }

        // Buscar el cupon en la base de datos usando el ID proporcionado
        Cupon cuponExistente = cuponRepository.findById(id)
                .orElseThrow(() -> new CuponException("Cupon no encontrado"));

        // Eliminar el cupon
        cuponRepository.delete(cuponExistente);
    }

    @Override
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

    @Override
    public List<ItemsCuponDTO> obtenerCuponesFiltrados(ItemsCuponDTO itemCuponDTO) {
        List<Cupon> cuponesFiltrados = cuponRepository.findAll();

        String nombre = itemCuponDTO.nombre();
        cuponesFiltrados = cuponRepository.findByNombreContainingIgnoreCase(nombre);

        LocalDate fechaVencimiento = itemCuponDTO.fechaVencimiento();
        cuponesFiltrados = cuponRepository.findByFechaVencimientoAfter(fechaVencimiento);

        LocalDate fechaApertura = itemCuponDTO.fechaApertura();
            cuponesFiltrados = cuponRepository.findByFechaAperturaAfter(fechaApertura);

        Float descuento = itemCuponDTO.descuento();
            cuponesFiltrados = cuponRepository.findByDescuento(descuento);

        TipoCupon tipo = itemCuponDTO.tipo();
            cuponesFiltrados = cuponRepository.findByTipo(tipo);

        EstadoCupon estado = itemCuponDTO.estado();
        cuponesFiltrados = cuponRepository.findByEstado(estado);

        // Convertir la lista de Cupon a ItemsCuponDTO
        List<ItemsCuponDTO> itemsCuponDTO = cuponesFiltrados.stream()
                .map(cupon -> new ItemsCuponDTO(
                        cupon.getNombre(),
                        itemCuponDTO.fechaVencimiento(),
                        itemCuponDTO.fechaApertura(),
                        cupon.getDescuento(),
                        cupon.getTipo(),
                        cupon.getEstado()))
                .collect(Collectors.toList());

        return itemsCuponDTO;
    }

    public List<InformacionCuponDTO> obtenerTodosLosCupones() {
        List<Cupon> cupones = cuponRepository.findAll();
        return cupones.stream()
                .map(cupon -> new InformacionCuponDTO(
                        cupon.getNombre(),
                        cupon.getCodigo(),
                        cupon.getDescuento(),
                        cupon.getFechaApertura(),
                        cupon.getFechaVencimiento(),
                        cupon.getTipo(),
                        cupon.getEstado()))
                .collect(Collectors.toList());
    }

    @Override
    public Cupon aplicarCupon(String codigoCupon, LocalDateTime fechaCompra) throws CuponException {
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
            return cupon;
        }
        return cupon;
    }

}