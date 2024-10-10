db = connect( 'mongodb://localhost:27017/TestUniEventos' );

db.cuentas.insertMany([
    {
        _id: ObjectId('66a2a9aaa8620e3c1c5437be'),
        rol: 'CLIENTE',
        estado: 'INACTIVO',
        email: 'pepeperez@email.com',
        password: '$2a$10$Jm7gRmJhZLxbF.r6XZ2ybeyu6b8BJZj8HJ.ZKH/aT2nR7MfukrLaG', // Encriptado
        usuario: {
            cedula: '1213444',
            nombre: 'Pepito Perez',
            telefono: '3012223333',
            direccion: 'Calle 12 # 12-12'
        },
        fechaRegistro: ISODate('2024-07-25T21:41:57.849Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66a2c14dd9219911cd34f2c0'),
        rol: 'CLIENTE',
        estado: 'ACTIVO',
        email: 'rosalopez@email.com',
        password: '$2a$10$yfpGuQ7xfv9sHKq3c.ZWxeFODtdnAz/vTxZkP/3LOhz/CL3NDjIxS', // Encriptado
        usuario: {
            cedula: '1213445',
            nombre: 'Rosa Lopez',
            telefono: '3128889191',
            direccion: 'Calle ABC # 12-12'
        },
        fechaRegistro: ISODate('2024-08-02T21:41:57.849Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66a2c1517f3b340441ffdeb0'),
        rol: 'ADMINISTRADOR',
        estado: 'ACTIVO',
        email: 'camiloalbran2018@gmail.com',
        password: '$2a$10$k1DGfL5Dh3C/NjRtY1.XjuGrEMHQv9yJY43Q6Cf2FwxfOqQlJ.kCy', // Encriptado
        usuario: {
            nombre: 'Admin 1'
        },
        fechaRegistro: ISODate('2024-10-09T21:41:57.849Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66a3d2424d629f0716d1b111'),
        rol: 'CLIENTE',
        estado: 'INACTIVO',
        email: 'marianamoreno@email.com',
        password: '$2a$10$yxOEaAad1MP0urZjGjiLTOKLlRt0WmROogYvhNhXz7mKpS7bxCQey', // Encriptado
        usuario: {
            cedula: '1213556',
            nombre: 'Mariana Moreno',
            telefono: '3001234567',
            direccion: 'Calle 25 # 20-15'
        },
        fechaRegistro: ISODate('2024-09-15T14:30:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66a3d24a7f421707baf98ab0'),
        rol: 'CLIENTE',
        estado: 'ACTIVO',
        email: 'jorgeramirez@email.com',
        password: '$2a$10$aWKl3B3m1Pbxv4nXNUdhNuKkCb/Mcn/59pWx9PQVDaCpTjYj0MCmC', // Encriptado
        usuario: {
            cedula: '1213449',
            nombre: 'Jorge Ramirez',
            telefono: '3209876543',
            direccion: 'Calle 30 # 25-18'
        },
        fechaRegistro: ISODate('2024-09-25T09:15:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66b3c1a28e2100008f40a2d2'),
        rol: 'CLIENTE',
        estado: 'ACTIVO',
        email: 'julianmorales@email.com',
        password: '$2a$10$examplePasswordHash', // Encriptado
        usuario: {
            cedula: '1213446',
            nombre: 'Julian Morales',
            telefono: '3135556666',
            direccion: 'Carrera 5 # 10-20'
        },
        fechaRegistro: ISODate('2024-10-10T21:00:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    }
]);


db.eventos.insertMany([
    {
        _id: ObjectId('66a2c476991cff088eb80aaf'),
        nombre: 'Concierto de despedida del 2024',
        descripcion: 'Concierto con los mejores artistas del 2024 para despedir el año',
        fecha: ISODate('2024-11-11T01:00:00.000Z'),
        tipo: 'FESTIVAL',
        estadoEvento: 'ACTIVO',
        direccion: 'Coliseo de eventos, calle 10 # 10-10',
        ciudad: 'Armenia',
        localidades: [
            {
                nombre: 'VIP',
                precio: 80000,
                capacidadMaxima: 50
            },
            {
                nombre: 'PLATEA',
                precio: 50000,
                capacidadMaxima: 100
            },
            {
                nombre: 'GENERAL',
                precio: 20000,
                capacidadMaxima: 200
            }
        ],
        imagenPortada: 'Url de la imagen del poster del concierto',
        imagenLocalidad: 'Url de la imagen de la distribución de las localidades',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Evento'
    },
    {
        _id: ObjectId('66b3d476123cff088eb81bb1'),
        nombre: 'Feria de Artesanías y Gastronomía',
        descripcion: 'Evento que reúne a los mejores artesanos y chefs de la región',
        fecha: ISODate('2024-12-15T09:00:00.000Z'),
        tipo: 'FERIA',
        estadoEvento: 'ACTIVO',
        direccion: 'Centro Cultural, calle 15 # 5-67',
        ciudad: 'Pereira',
        localidades: [
            {
                nombre: 'PREFERENCIAL',
                precio: 60000,
                capacidadMaxima: 80
            },
            {
                nombre: 'GENERAL',
                precio: 30000,
                capacidadMaxima: 150
            }
        ],
        imagenPortada: 'Url de la imagen del poster de la feria',
        imagenLocalidad: 'Url de la imagen de las localidades en la feria',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Evento'
    },
    {
        _id: ObjectId('66b3d476123cff088eb81bb2'),
        nombre: 'Torneo Nacional de Ajedrez',
        descripcion: 'Competencia de ajedrez con los mejores jugadores del país',
        fecha: ISODate('2025-01-05T08:00:00.000Z'),
        tipo: 'COMPETENCIA',
        estadoEvento: 'ELIMINADO',
        direccion: 'Polideportivo Central, carrera 20 # 45-30',
        ciudad: 'Cali',
        localidades: [
            {
                nombre: 'ZONA COMPETIDORES',
                precio: 0,
                capacidadMaxima: 20
            },
            {
                nombre: 'ZONA ESPECTADORES',
                precio: 10000,
                capacidadMaxima: 100
            }
        ],
        imagenPortada: 'Url de la imagen del torneo',
        imagenLocalidad: 'Url de la imagen de la distribución de las zonas',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Evento'
    },
    {
        _id: ObjectId('66b3f7d4123cff088eb81cc1'),
        nombre: 'Festival de Música Electrónica 2024',
        descripcion: 'El mejor festival de música electrónica con DJs internacionales',
        fecha: ISODate('2024-12-31T20:00:00.000Z'),
        tipo: 'FESTIVAL',
        estadoEvento: 'ACTIVO',
        direccion: 'Parque Metropolitano, Avenida 7 # 45-67',
        ciudad: 'Medellín',
        localidades: [
            {
                nombre: 'FRONT STAGE',
                precio: 150000,
                capacidadMaxima: 80
            },
            {
                nombre: 'GENERAL',
                precio: 50000,
                capacidadMaxima: 200
            }
        ],
        imagenPortada: 'Url de la imagen del festival de música electrónica',
        imagenLocalidad: 'Url de la imagen de la distribución de las localidades',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Evento'
    },
    {
        _id: ObjectId('66b3f7d4123cff088eb81cc2'),
        nombre: 'Maratón Internacional',
        descripcion: 'Evento deportivo para corredores de todo el mundo',
        fecha: ISODate('2024-11-20T06:00:00.000Z'),
        tipo: 'DEPORTIVO',
        estadoEvento: 'ACTIVO',
        direccion: 'Estadio Olímpico, calle 50 # 20-10',
        ciudad: 'Bogotá',
        localidades: [
            {
                nombre: 'PARTICIPANTES',
                precio: 0,
                capacidadMaxima: 500
            },
            {
                nombre: 'ESPECTADORES',
                precio: 20000,
                capacidadMaxima: 300
            }
        ],
        imagenPortada: 'Url de la imagen del maratón',
        imagenLocalidad: 'Url de la imagen de la distribución de las zonas',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Evento'
    }
]);

db.ordenes.insertMany([
    {
        _id: ObjectId('66b5d2b28e2100008f60b2a1'),
        idCliente: ObjectId('66a2c14dd9219911cd34f2c0'), // Rosa Lopez
        idCupon: ObjectId('66b3c2a58e2100008f50a1a4'), // Cupón con descuento
        codigoPasarela: 'PAY-12345-LOPEZ',
        fecha: ISODate('2024-10-10T10:30:00.000Z'),
        detalleOrden: [
            {
                producto: 'Entrada al evento - VIP',
                cantidad: 2,
                precioUnitario: 50.0,
                subtotal: 100.0
            }
        ],
        total: 80.0,  // Descuento aplicado por el cupón
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Orden'
    },
    {
        _id: ObjectId('66b5d2b28e2100008f60b2a2'),
        idCliente: ObjectId('66a3d24a7f421707baf98ab0'), // Jorge Ramirez
        idCupon: null,  // No usó cupón
        codigoPasarela: 'PAY-67890-RAMIREZ',
        fecha: ISODate('2024-10-09T14:20:00.000Z'),
        detalleOrden: [
            {
                producto: 'Entrada al evento - General',
                cantidad: 3,
                precioUnitario: 20.0,
                subtotal: 60.0
            }
        ],
        total: 60.0,
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Orden'
    },
    {
        _id: ObjectId('66b5d2b28e2100008f60b2a3'),
        idCliente: ObjectId('66a2a9aaa8620e3c1c5437be'), // Pepito Perez
        idCupon: ObjectId('66b3c2a58e2100008f50a1a5'),  // Usó cupón
        codigoPasarela: 'PAY-11223-PEREZ',
        fecha: ISODate('2024-10-08T09:00:00.000Z'),
        detalleOrden: [
            {
                producto: 'Entrada al evento - Platino',
                cantidad: 1,
                precioUnitario: 100.0,
                subtotal: 100.0
            }
        ],
        total: 85.0,  // Descuento aplicado por el cupón
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Orden'
    },
    {
        _id: ObjectId('66b5d2b28e2100008f60b2a4'),
        idCliente: ObjectId('66a3d2424d629f0716d1b111'), // Mariana Moreno
        idCupon: null,  // No usó cupón
        codigoPasarela: 'PAY-33445-MORENO',
        fecha: ISODate('2024-10-07T16:45:00.000Z'),
        detalleOrden: [
            {
                producto: 'Entrada al evento - VIP',
                cantidad: 1,
                precioUnitario: 50.0,
                subtotal: 50.0
            }
        ],
        total: 50.0,
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Orden'
    },
    {
        _id: ObjectId('66b5d2b28e2100008f60b2a5'),
        idCliente: ObjectId('66b3c1a28e2100008f40a2d2'), // Julian Morales
        idCupon: ObjectId('66b3c2a58e2100008f50a1a3'),  // Usó cupón
        codigoPasarela: 'PAY-55667-MORALES',
        fecha: ISODate('2024-10-06T18:00:00.000Z'),
        detalleOrden: [
            {
                producto: 'Entrada al evento - General',
                cantidad: 2,
                precioUnitario: 20.0,
                subtotal: 40.0
            }
        ],
        total: 32.0,  // Descuento aplicado por el cupón
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Orden'
    }
]);


db.carritos.insertMany([
    {
        _id: ObjectId('66b3a12e8e2100008f40a1a1'),
        idUsuario: '66a2a9aaa8620e3c1c5437be', // Pepito Perez
        itemsCarrito: [
            {
                cantidad: 2,
                nombreLocalidad: 'Camisa Concierto 2024',
                idEvento: '66a2c476991cff088eb80aaf', // Concierto de despedida del 2024
                precio: 50000
            },
            {
                cantidad: 1,
                nombreLocalidad: 'Boleta VIP',
                idEvento: '66a2c476991cff088eb80aaf', // Concierto de despedida del 2024
                precio: 80000
            }
        ],
        fecha: ISODate('2024-10-10T14:00:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Carrito'
    },
    {
        _id: ObjectId('66b3a12e8e2100008f40a1a2'),
        idUsuario: '66a2c14dd9219911cd34f2c0', // Rosa Lopez
        itemsCarrito: [
            {
                cantidad: 1,
                nombreLocalidad: 'Gorra Festival',
                idEvento: '66b3f7d4123cff088eb81cc1', // Festival de Música Electrónica 2024
                precio: 25000
            },
            {
                cantidad: 3,
                nombreLocalidad: 'Boleta General',
                idEvento: '66b3f7d4123cff088eb81cc1', // Festival de Música Electrónica 2024
                precio: 50000
            }
        ],
        fecha: ISODate('2024-10-11T16:00:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Carrito'
    },
    {
        _id: ObjectId('66b3a12e8e2100008f40a1a3'),
        idUsuario: '66a3d2424d629f0716d1b111', // Mariana Moreno
        itemsCarrito: [
            {
                cantidad: 1,
                nombreLocalidad: 'Pulsera Exclusiva',
                idEvento: '66b3d476123cff088eb81bb1', // Feria de Artesanías y Gastronomía
                precio: 15000
            },
            {
                cantidad: 2,
                nombreLocalidad: 'Boleta Preferencial',
                idEvento: '66b3d476123cff088eb81bb1', // Feria de Artesanías y Gastronomía
                precio: 60000
            }
        ],
        fecha: ISODate('2024-10-12T18:00:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Carrito'
    },
    {
        _id: ObjectId('66b3a12e8e2100008f40a1a4'),
        idUsuario: '66a3d24a7f421707baf98ab0', // Jorge Ramirez
        itemsCarrito: [
            {
                cantidad: 1,
                nombreLocalidad: 'Boleta Espectadores',
                idEvento: '66b3f7d4123cff088eb81cc2', // Maratón Internacional
                precio: 20000
            }
        ],
        fecha: ISODate('2024-10-12T20:00:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Carrito'
    },
    {
        _id: ObjectId('66b3a12e8e2100008f40a1a5'),
        idUsuario: '66b3c1a28e2100008f40a2d2', // Julian Morales
        itemsCarrito: [
            {
                cantidad: 3,
                nombreLocalidad: 'Camisa Torneo Ajedrez',
                idEvento: '66b3d476123cff088eb81bb2', // Torneo Nacional de Ajedrez
                precio: 10000
            }
        ],
        fecha: ISODate('2024-10-13T14:00:00.000Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Carrito'
    }
]);

db.cupones.insertMany([
    {
        _id: ObjectId('66b4a12f8e2100008f50a1b1'),
        codigo: 'DESCUENTO2024',
        nombre: 'Descuento Año Nuevo',
        porcentajeDescuento: 20.0,
        fechaVencimiento: ISODate('2024-12-31T23:59:59.000Z'),
        fechaApertura: ISODate('2024-11-01T00:00:00.000Z'),
        tipoCupon: 'UNICO',
        estadoCupon: 'DISPONIBLE',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cupon'
    },
    {
        _id: ObjectId('66b4a12f8e2100008f50a1b2'),
        codigo: 'PROMOFERIA',
        nombre: 'Promoción Feria de Artesanías',
        porcentajeDescuento: 15.0,
        fechaVencimiento: ISODate('2024-12-20T23:59:59.000Z'),
        fechaApertura: ISODate('2024-11-15T00:00:00.000Z'),
        tipoCupon: 'UNICO',
        estadoCupon: 'DISPONIBLE',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cupon'
    },
    {
        _id: ObjectId('66b4a12f8e2100008f50a1b3'),
        codigo: 'TORNEO2025',
        nombre: 'Descuento Torneo de Ajedrez',
        porcentajeDescuento: 10.0,
        fechaVencimiento: ISODate('2025-01-05T23:59:59.000Z'),
        fechaApertura: ISODate('2024-12-01T00:00:00.000Z'),
        tipoCupon: 'MULTIPLE',
        estadoCupon: 'NO_DISPONIBLE',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cupon'
    },
    {
        _id: ObjectId('66b4a12f8e2100008f50a1b4'),
        codigo: 'MARATON2024',
        nombre: 'Maratón Internacional',
        porcentajeDescuento: 25.0,
        fechaVencimiento: ISODate('2024-11-20T23:59:59.000Z'),
        fechaApertura: ISODate('2024-10-15T00:00:00.000Z'),
        tipoCupon: 'UNICO',
        estadoCupon: 'DISPONIBLE',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cupon'
    },
    {
        _id: ObjectId('66b4a12f8e2100008f50a1b5'),
        codigo: 'FESTIVALVIP',
        nombre: 'Descuento VIP Festival Música Electrónica',
        porcentajeDescuento: 30.0,
        fechaVencimiento: ISODate('2024-12-31T23:59:59.000Z'),
        fechaApertura: ISODate('2024-11-30T00:00:00.000Z'),
        tipoCupon: 'MULTIPLE',
        estadoCupon: 'DISPONIBLE',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cupon'
    }
]);

db.pqr.insertMany([
    {
        _id: ObjectId('66b4c2a58e2100008f50b1b1'),
        idUsuario: ObjectId('66a2a9aaa8620e3c1c5437be'), // Pepito Perez
        categoriaPQR: 'QUEJA',
        descripcion: 'El evento fue cancelado sin previo aviso.',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.PQR'
    },
    {
        _id: ObjectId('66b4c2a58e2100008f50b1b2'),
        idUsuario: ObjectId('66a2c14dd9219911cd34f2c0'), // Rosa Lopez
        categoriaPQR: 'RECLAMO',
        descripcion: 'No recibí el descuento prometido en el evento.',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.PQR'
    },
    {
        _id: ObjectId('66b4c2a58e2100008f50b1b3'),
        idUsuario: ObjectId('66a3d24a7f421707baf98ab0'), // Jorge Ramirez
        categoriaPQR: 'SUGERENCIA',
        descripcion: 'Sería bueno contar con más opciones de pago.',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.PQR'
    },
    {
        _id: ObjectId('66b4c2a58e2100008f50b1b4'),
        idUsuario: ObjectId('66a3d2424d629f0716d1b111'), // Mariana Moreno
        categoriaPQR: 'PETICION',
        descripcion: 'Solicito más información sobre próximos eventos.',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.PQR'
    },
    {
        _id: ObjectId('66b4c2a58e2100008f50b1b5'),
        idUsuario: ObjectId('66b3c1a28e2100008f40a2d2'), // Julian Morales
        categoriaPQR: 'QUEJA',
        descripcion: 'El sistema no me dejó inscribirme en el evento.',
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.PQR'
    }
]);



