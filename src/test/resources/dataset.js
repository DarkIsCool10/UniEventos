db = connect( 'mongodb://localhost:27017/UniEventos' );

db.cuentas.insertMany([
    {
        _id: ObjectId('66a2a9aaa8620e3c1c5437be'),
        rol: 'CLIENTE',
        estado: 'INACTIVO',
        email: 'pepeperez@email.com',
        password: 'password',
        usuario: {
            cedula: '1213444',
            nombre: 'Pepito perez',
            telefono: '3012223333',
            direccion: 'Calle 12 # 12-12',
        },
        fechaRegistro: ISODate('2024-07-25T21:41:57.849Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66a2c14dd9219911cd34f2c0'),
        rol: 'CLIENTE',
        estado: 'ACTIVO',
        email: 'rosalopez@email.com',
        password: 'password',
        usuario: {
            cedula: '1213445',
            nombre: 'Rosa Lopez',
            telefono: '3128889191',
            direccion: 'Calle ABC # 12-12',
        },
        fechaRegistro: ISODate('2024-08-02T21:41:57.849Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
    {
        _id: ObjectId('66a2c1517f3b340441ffdeb0'),
        rol: 'ADMINISTRADOR',
        estado: 'ACTIVO',
        email: 'camiloalbran2018@gmail.com',
        password: 'admin10',
        usuario: {
            nombre: 'Admin 1'
        },
        fechaRegistro: ISODate('2024-10-09T21:41:57.849Z'),
        _class: 'co.edu.uniquindio.proyecto.modelo.documents.Cuenta'
    },
]);


db.eventos.insertMany([
    {
        _id: ObjectId('66a2c476991cff088eb80aaf'),
        nombre: 'Concierto de despedida del 2024',
        descripcion: 'Concierto con los mejores artistas del 2024 para despedir el año',
        fecha: ISODate('2024-11-11T01:00:00.000Z'),
        tipo: 'FESTIVAL',
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
        imagenLocalidades: 'Url de la imagen de la distribución de las localidades',
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Evento'
    }
]);


db.ordenes.insertMany([
    {
        _id: ObjectId('66a2c6a55773597d73593fff'),
        detalle: [
            {
                codigoEvento: ObjectId('66a2c476991cff088eb80aaf'),
                nombreLocalidad: 'PLATEA',
                precio: 50000,
                cantidad: 2
            }
        ],
        codigoCliente: ObjectId('66a2a9aaa8620e3c1c5437be'),
        total: 100000,
        fecha: ISODate('2024-07-25T21:41:57.849Z'),
        codigoPasarela: 'CODIGO_PASARELA',
        pago: {
            codigoAutorizacion: '48dc3dd9-bde1-45ae-b23f-27ee7a261f00',
            fecha: ISODate('2024-07-25T21:41:57.849Z'),
            totalPagado: 100000,
            estado: 'APROBADA',
            metodoPago: 'TARJETA DE CRÉDITO'
        },
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Orden'
    }
]);
