package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.cuenta.CrearCuentaDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.EditarCuentaDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.InformacionCuentaDTO;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import co.edu.uniquindio.unieventos.model.enums.EstadoCuenta;
import co.edu.uniquindio.unieventos.model.enums.Rol;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import co.edu.uniquindio.unieventos.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CuentaServiceImp {

    private final CuentaRepository cuentaRepository;


    /**
     * Metodo para crear una cuenta
     *
     * @param cuentaDTO
     * @return Mensaje de exito cuando se crea una cuenta
     * @throws CuentaException si la cuenta validada por el email ya existe
     */
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
        nuevaCuenta.setEstado(EstadoCuenta.INACTIVO);
        nuevaCuenta.setRol(Rol.CLIENTE);

        // Crear entidad Usuario asociada
        Usuario usuario = new Usuario();
        usuario.setNombre(cuentaDTO.nombre());
        usuario.setCedula(cuentaDTO.cedula());
        usuario.setTelefono(cuentaDTO.telefono());
        usuario.setDireccion(cuentaDTO.direccion());
        nuevaCuenta.setUsuario(usuario);

        // Guardar la cuenta en la base de datos
        cuentaRepository.save(nuevaCuenta);

        return "Cuenta creada exitosamente. Por favor, revise su email electrónico para validar su cuenta.";
    }


    /**
     * Metodo para editar los datos de una cuenta
     * @param cuentaDTO
     * @param cuentaId
     * @return Mensaje de exito de que los fueros modificados
     * @throws CuentaException
     */
    public String editarCuenta(EditarCuentaDTO cuentaDTO, String cuentaId) throws CuentaException {
        // Validar que el DTO no sea nulo y que el ID no sea nulo
        if (cuentaDTO == null || cuentaId == null || cuentaId.isEmpty()) {
            throw new CuentaException("Los datos de cuenta o el id no pueden ser nulos");
        }

        // Buscar la cuenta en la base de datos usando un identificador único
        Cuenta cuentaExistente = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaException("Cuenta no encontrada"));

        // Actualizar los datos del objeto Usuario dentro de la cuenta
        Usuario usuarioExistente = cuentaExistente.getUsuario();
        if (usuarioExistente == null) {
            throw new CuentaException("Información de usuario no encontrada en la cuenta");
        }

        usuarioExistente.setNombre(cuentaDTO.nombre());
        usuarioExistente.setTelefono(cuentaDTO.telefono());
        usuarioExistente.setDireccion(cuentaDTO.direccion());

        // Actualizar la contraseña si se proporciona
        if (cuentaDTO.password() != null && !cuentaDTO.password().isEmpty()) {
            String nuevaPassword = cuentaDTO.password();
            cuentaExistente.setPassword(nuevaPassword);
        }

        // Guardar los cambios en la base de datos
        cuentaRepository.save(cuentaExistente);

        return "Cuenta actualizada con éxito";
    }


    /**
     * Metodo para eliminar una cuenta por su ID
     * @param id
     * @return Mensaje de confirmacion de que la cuenta se elimino
     * @throws CuentaException
     */
    public String eliminarCuenta(String id) throws CuentaException {
        // Validar que el ID no sea nulo o vacío
        if (id == null || id.isEmpty()) {
            throw new CuentaException("El ID de la cuenta no puede ser nulo o vacío");
        }

        // Buscar la cuenta en la base de datos usando el ID proporcionado
        Cuenta cuentaExistente = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaException("Cuenta no encontrada"));

        // Eliminar la cuenta
        cuentaRepository.delete(cuentaExistente);

        return "Cuenta eliminada con éxito";
    }

    /**
     * Metodo para obtener la informacion de una cuenta
     * @param id
     * @return los datos obtenidos de una cuenta por su id
     * @throws CuentaException
     */
    public InformacionCuentaDTO obtenerInformacionCuenta(String id)throws CuentaException {
        // Validar que el ID no sea nulo o vacío
        if (id == null || id.isEmpty()) {
            throw new CuentaException("El ID de la cuenta no puede ser nulo o vacío");
        }
        Cuenta cuentaABuscar = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaException("Cuenta no encontrada"));

        Usuario usuario = cuentaABuscar.getUsuario();
        if(usuario == null){
            throw new CuentaException("Usuario no encontrado");
        }

        return new InformacionCuentaDTO(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                cuentaABuscar.getEmail()
        );
    }

    public String enviarCodigoRecuperacionPassword(String correo) throws CuentaException {
        // Validar que el correo no sea nulo o vacío
        if (correo == null || correo.isEmpty()) {
            throw new CuentaException("El correo no puede ser nulo o vacío");
        }

        // Buscar la cuenta en la base de datos usando el correo proporcionado
        Cuenta cuentaExistente = cuentaRepository.findByEmail(correo)
                .orElseThrow(() -> new CuentaException("Cuenta no encontrada para el correo proporcionado"));

        // Generar un código de recuperación único
        String codigoRecuperacion = UUID.randomUUID().toString();

        // Guardar el código en la cuenta (o en un lugar seguro relacionado con la cuenta)
        cuentaExistente.setCodigoVerificacionContrasenia(codigoRecuperacion);
        cuentaRepository.save(cuentaExistente);

        // Enviar el código por correo electrónico
        enviarCorreoRecuperacion(correo, codigoRecuperacion);

        return "Código de recuperación enviado con éxito";
    }

    private void enviarCorreoRecuperacion(String correo, String codigoRecuperacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correo);
        mensaje.setSubject("Código de Recuperación de Contraseña");
        mensaje.setText("Utiliza el siguiente código para recuperar tu contraseña: " + codigoRecuperacion);
        //mailSender.send(mensaje);
    }

}
