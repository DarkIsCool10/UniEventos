package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import co.edu.uniquindio.unieventos.model.enums.EstadoCuenta;
import co.edu.uniquindio.unieventos.model.enums.Rol;
import co.edu.uniquindio.unieventos.model.vo.CodigoValidacion;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import co.edu.uniquindio.unieventos.repository.CuentaRepository;
import co.edu.uniquindio.unieventos.service.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CuentaServiceImp implements CuentaService {

    private final CuentaRepository cuentaRepository;

    // Metodo de apoyo para buscar una cuenta por ID y devolver un Optional<Cuenta>
    private Optional<Cuenta> obtenerCuentaPorId(String idCuenta) {
        return cuentaRepository.findById(idCuenta);
    }

    /**
     * Metodo para crear una cuenta
     * @param cuentaDTO
     * @return Mensaje de exito cuando se crea una cuenta
     * @throws CuentaException si la cuenta validada por el email ya existe
     */
    @Override
    public String crearCuenta(CrearCuentaDTO cuentaDTO) throws CuentaException {
        // Verifica si ya existe una cuenta con el mismo email
        if (cuentaRepository.existsByEmail(cuentaDTO.email())) {
            throw new CuentaException("Ya existe una cuenta registrada con este email electrónico.");
        }
        if(cuentaRepository.equals(cuentaDTO.cedula())){
            throw new CuentaException("Ya existe una cuenta registrada con esta cedula.");
        }

        // Crear instancia de Cuenta con datos del DTO
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setEmail(cuentaDTO.email());
        nuevaCuenta.setPassword(cuentaDTO.password());
        nuevaCuenta.setFechaRegistro(LocalDateTime.now());
        nuevaCuenta.setRol(Rol.CLIENTE);
        nuevaCuenta.setUsuario(new Usuario(
                cuentaDTO.cedula(),
                cuentaDTO.nombre(),
                cuentaDTO.telefono(),
                cuentaDTO.direccion()
        ));
        nuevaCuenta.setEstado(EstadoCuenta.INACTIVO);

        // Generar y asignar el código de validación
        String codigoValidacion = generarCodigoValidacion();
        nuevaCuenta.setCodigoVerificacionRegistro(String.valueOf(new CodigoValidacion(LocalDateTime.now(), codigoValidacion)));

        // Guardar la cuenta en la base de datos
        Cuenta cuentaCreada = cuentaRepository.save(nuevaCuenta);

        //TODO: ENVIAR CORRREOS

        return cuentaCreada.getId();
    }

    // Metoodo para generar un código de validación de 4 cifras
    public String generarCodigoValidacion() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }

    /**
     * Metodo para editar los datos de una cuenta
     * @param cuenta
     * @return Mensaje de exito de que los fueros modificados
     * @throws CuentaException
     */
    @Override
    public String editarCuenta(EditarCuentaDTO cuenta) throws CuentaException {
        //Buscamos la cuenta del usuario que se quiere actualizar
        Optional<Cuenta> optionalCuenta = obtenerCuentaPorId(cuenta.id());

        //Si no se encontró la cuenta del usuario, lanzamos una excepción
        if(optionalCuenta.isEmpty()){
            throw new CuentaException("No existe un usuario con el id dado");
        }

        //Obtenemos la cuenta del usuario a modificar y actualizamos sus datos
        Cuenta cuentaModificada = optionalCuenta.get();
        cuentaModificada.getUsuario().setNombre( cuenta.nombre() );
        cuentaModificada.getUsuario().setTelefono( cuenta.telefono() );
        cuentaModificada.getUsuario().setDireccion( cuenta.direccion() );
        // Actualizar la contraseña si se proporciona
        if (cuenta.password() != null && !cuenta.password().isEmpty()) {
            String nuevaPassword = cuenta.password();
            cuentaModificada.setPassword( cuenta.password() );
        }

        //Como el objeto cuenta ya tiene un id, el save() no crea un nuevo registro sino que actualiza el que ya existe
        cuentaRepository.save(cuentaModificada);
        return cuentaModificada.getId();
    }


    /**
     * Metodo para eliminar una cuenta por su ID
     * @param id
     * @return el id de la cuenta eliminada
     * @throws CuentaException
     */
    @Override
    public String eliminarCuenta(String id) throws CuentaException {
        //Buscamos la cuenta del usuario que se quiere eliminar
        Optional<Cuenta> optionalCuenta = obtenerCuentaPorId(id);

        //Si no se encontró la cuenta, lanzamos una excepción
        if(optionalCuenta.isEmpty()){
            throw new CuentaException("No se encontró el usuario con el id "+id);
        }

        //Obtenemos la cuenta del usuario que se quiere eliminar y le asignamos el estado eliminado
        Cuenta cuenta = optionalCuenta.get();
        cuenta.setEstado(EstadoCuenta.ELIMINADO);

        cuentaRepository.save(cuenta);

        return cuenta.getId();

    }

    /**
     * Metodo para obtener la informacion de una cuenta
     * @param id
     * @return los datos obtenidos de una cuenta por su id
     * @throws CuentaException
     */
    @Override
    public InformacionCuentaDTO obtenerInformacionCuenta(String id)throws CuentaException {
        //Buscamos la cuenta del usuario que se quiere obtener
        Optional<Cuenta> optionalCuenta = obtenerCuentaPorId(id);

        //Si no se encontró la cuenta, lanzamos una excepción
        if(optionalCuenta.isEmpty()){
            throw new CuentaException("No se encontró el usuario con el id "+id);
        }

        //Obtenemos la cuenta del usuario
        Cuenta cuenta = optionalCuenta.get();

        //Retornamos la información de la cuenta del usuario
        return new InformacionCuentaDTO(
                cuenta.getId(),
                cuenta.getUsuario().getCedula(),
                cuenta.getUsuario().getNombre(),
                cuenta.getUsuario().getTelefono(),
                cuenta.getUsuario().getDireccion(),
                cuenta.getEmail()
        );
    }

    @Override
    public List<ItemCuentaDTO> listarCuentas() {

        //Obtenemos todas las cuentas de los usuarios de la base de datos
        List<Cuenta> cuentas = cuentaRepository.findAll();

        //Creamos una lista de DTOs
        List<ItemCuentaDTO> items = new ArrayList<>();

        //Recorremos la lista de cuentas y por cada uno creamos un DTO y lo agregamos a la lista
        for (Cuenta cuenta : cuentas) {
            items.add( new ItemCuentaDTO(
                    cuenta.getId(),
                    cuenta.getUsuario().getNombre(),
                    cuenta.getEmail(),
                    cuenta.getUsuario().getTelefono()
            ));
        }
        return items;
    }


    /**
     * Metodo para enviar los codigos de recuperacion para un contraseña
     * @param correo
     * @return
     * @throws CuentaException
     */
    @Override
    public String enviarCodigoRecuperacionPassword(String correo) throws CuentaException {
        // Validar que el email no sea nulo o vacío
        if (correo == null || correo.isEmpty()) {
            throw new CuentaException("El email no puede ser nulo o vacío");
        }

        // Buscar la cuenta en la base de datos usando el email proporcionado
        Cuenta cuentaExistente = cuentaRepository.findByEmail(correo)
                .orElseThrow(() -> new CuentaException("Cuenta no encontrada para el email proporcionado"));

        // Generar un código de recuperación único
        String codigoRecuperacion = generarCodigoValidacion();

        // Guardar el código en la cuenta (o en un lugar seguro relacionado con la cuenta)
        cuentaExistente.setCodigoVerificacionContrasenia(codigoRecuperacion);
        cuentaRepository.save(cuentaExistente);

        // Enviar el código por email electrónico
        //enviarCorreoRecuperacion(email, codigoRecuperacion);

        return "Código de recuperación enviado con éxito";
    }

    @Override
    public String cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws CuentaException {
        // Buscar la cuenta que contiene el código de verificación
        Optional<Cuenta> optionalCuenta = cuentaRepository.findByCodigoValidacionRegistro_Codigo(cambiarPasswordDTO.codigoVerificacion());

        // Si la cuenta no existe, lanzar excepción
        Cuenta cuenta = optionalCuenta.orElseThrow(() -> new CuentaException("Código de verificación inválido o expirado."));

        // Verificar que el código de verificación sea el mismo que el enviado
        if (!cuenta.getCodigoVerificacionContrasenia().equals(cambiarPasswordDTO.codigoVerificacion())) {
            throw new CuentaException("Código de verificación inválido.");
        }

        // Actualizar la contraseña con la nueva contraseña proporcionada
        cuenta.setPassword(cambiarPasswordDTO.passwordNueva());

        // Limpiar el código de verificación después de cambiar la contraseña
        cuenta.setCodigoVerificacionContrasenia(null);

        // Guardar los cambios en la base de datos
        cuentaRepository.save(cuenta);

        return "Contraseña cambiada con éxito.";
    }

    @Override
    public String iniciarSesion(LoginDTO loginDTO) throws CuentaException {
        // Buscar la cuenta por email electrónico
        Optional<Cuenta> optionalCuenta = cuentaRepository.findByEmail(loginDTO.email());

        // Si la cuenta no existe, lanzar excepción
        Cuenta cuenta = optionalCuenta.orElseThrow(() -> new CuentaException("Correo electrónico o contraseña incorrectos."));

        // Verificar que las contraseñas coincidan
        if (!cuenta.getPassword().equals(loginDTO.password())) {
            throw new CuentaException("Correo electrónico o contraseña incorrectos.");
        }

        // Verificar el estado de la cuenta (debe estar activa para iniciar sesión)
        if (!cuenta.getEstado().equals(EstadoCuenta.ACTIVO)) {
            throw new CuentaException("La cuenta no está activa.");
        }

        // Retornar un mensaje de éxito o un token de sesión
        return "Inicio de sesión exitoso.";
    }




}
