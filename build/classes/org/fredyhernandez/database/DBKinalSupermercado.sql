drop database if exists DBKinalSupermercado;
create database DBKinalSupermercado;

use DBKinalSupermercado;

create table Clientes(
	codigoCliente int not null,
    nitCliente varchar (10) not null,
    nombreCliente varchar (50) not null,
    apellidoCliente varchar (50) not null,
	direccionCliente varchar (150),
    telefonoCliente varchar (12),
    correoCliente varchar (100), 
    primary key PK_codigoCliente (codigoCliente)
);

create table Proveedores(
	codigoProveedor int not null,
	nitProveedor varchar (10)not null,
    nombreProveedor varchar (60),
    apellidoProveedor varchar (60),
    direccionProveedor varchar (150),
    razonSocial varchar (60),
    contactoPrincipal varchar (100),
    paginaWeb varchar (50),
    primary key PK_codigoProveedor (codigoProveedor)
);

create table Compras(
	numeroDocumento int not null,
    fechaDocumento Date,
    descripcion varchar (90),
    totalDocumento Decimal (10,2),
    primary key PK_numeroDocumento (numeroDocumento)
);

create table CargoEmpleados(
	codigoCargoEmpleado int not null,
    nombreCargo varchar (50),
    descripcionCargo varchar (90),
    primary key PK_codigoCargoEmpleado (codigoCargoEmpleado)
);

create table TipoProducto(
	codigoTipoProducto int not null,
    descripcion varchar (90),
    primary key PK_codigoTipoProducto (codigoTipoProducto)
    
);

create table TelefonoProveedor(
	codigoTelefonoProveedor int not null,
    numeroPrincipal varchar (8),
    numeroSecundario varchar (8),
    observaciones varchar (45),
    codigoProveedor int not null,
    primary key PK_codigoTelefonoProveedor (codigoTelefonoProveedor),
    constraint FK_TelefonoProveedor_Proveedores foreign key TelefonoProveedor(codigoProveedor)
		references Proveedores(codigoProveedor) on delete cascade on update cascade
);

create table Productos(
	codigoProducto varchar (15),
    descripcionProducto varchar (100),
    precioUnitario decimal (10,2),
    precioDocena decimal (10,2),
    precioMayor decimal (10,2),
    existencia int not null,
    codigoTipoProducto int not null,
    codigoProveedor int not null,
    primary key PK_codigoProducto (codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(codigoTipoProducto)
		references TipoProducto (codigoTipoProducto) on delete cascade on update cascade,
	constraint FK_Productos_Proveedores foreign key Productos(codigoProveedor)
		references Proveedores (codigoProveedor) on delete cascade on update cascade
);

create table Empleados(
	codigoEmpleado int not null,
    nombreEmpleado varchar (50),
    apellidoEmpleado varchar (50),
    sueldo decimal(10,2),
    direccion varchar(150),
    turno varchar(15),
    codigoCargoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_CargoEmpleados foreign key Empleados (codigoCargoEmpleado)
		references CargoEmpleados (codigoCargoEmpleado) on delete cascade on update cascade
);

create table Facturas(
	numeroFactura int not null,
    estado varchar(50),
    totalFactura decimal(10,2),
    fechaFactura varchar(45),
    codigoCliente int not null,
    codigoEmpleado int not null,
    primary key PK_numeroFactura (numeroFactura),
    constraint FK_Facturas_Clientes foreign key Facturas(codigoCliente)
		references Clientes(codigoCliente) on delete cascade on update cascade,
	constraint FK_Facturas_Empleados foreign key Facturas(codigoEmpleado)
		references Empleados(codigoEmpleado) on delete cascade on update cascade
);

create table DetalleFactura(
	codigoDetalleFactura int not null,
    precioUnitario decimal(10,2),
    cantidad int,
    numeroFactura int not null,
    codigoProducto varchar(15) not null,
    primary key PK_codigoDetalleFactura (codigoDetalleFactura),
    constraint FK_DetalleFactura_Facturas foreign key (numeroFactura)
		references Facturas(numeroFactura) on delete cascade on update cascade,
	constraint FK_DetalleFactura_Productos foreign key (codigoProducto)
		references Productos(codigoProducto) on delete cascade on update cascade
);

create table DetalleCompra(
	codigoDetalleCompra int not null,
    costoUnitario decimal(10,2),
    cantidad int,
    codigoProducto varchar(15),
    numeroDocumento int not null,
    primary key PK_codigoDetalleCompra (codigoDetalleCompra),
    constraint FK_DetalleCompra_Productos foreign key (codigoProducto)
		references Productos (codigoProducto) on delete cascade on update cascade,
	constraint FK_DetalleCompra_Compras foreign key (numeroDocumento)
		references Compras (numeroDocumento) on delete cascade on update cascade
);

create table EmailProveedor(
	codigoEmailProveedor int not null,
    emailProveedor varchar(50),
    descripcion varchar(100),
    codigoProveedor int not null,
    primary key PK_codigoEmailProveedor (codigoEmailProveedor),
    constraint FK_EmailProveedor_Proveedores foreign key (codigoProveedor)
		references Proveedores (codigoProveedor) on delete cascade on update cascade
);


-- ---------------------- Procedimientos Almacenados ----------------------
-- ------------------- Clientes --------------------
-- ---- Agregar Clientes ----  
 Delimiter $$
	 create procedure sp_AgregarClientes(in codigoCliente int, in nitCliente varchar (10), in nombreCliente varchar (50), in apellidoCliente varchar (50),
     in direccionCliente varchar (150), in telefonoCliente varchar (12), in correoCliente varchar (100))  
		begin
			insert into Clientes (codigoCliente, nitCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
				values (codigoCliente, nitCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
                
		End $$

 Delimiter ;
 call sp_AgregarClientes (01, '125302145', 'Lizandro', 'Hernandez', 'El Gallito', '89258922', 'lizandrohernan@gmail.com');
 call sp_AgregarClientes (02, '563200210', 'Luis', 'Garcia', 'Villa Nueva', '45782055', 'luisGarcia0755@gmail.com');
 call sp_AgregarClientes (03, '101213456', 'Guillermo', 'Florian', 'San Cristobal', '59210388', 'guillermoFrorian@gmail.com');
 call sp_AgregarClientes (04, '878620234', 'Danilo', 'Gonzales', 'La Petapa', '56897422', 'daniloGonzales@gmail.com');
 
 
 -- ---- Listar Clientes ----
 Delimiter $$
	create procedure sp_ListarClientes()
		begin
			select
            C.codigoCliente,
            C.nitCliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C; 
            
		End $$
        
Delimiter ;
call sp_ListarClientes(); 


-- ---- Buscar Clientes ----
Delimiter $$
	create procedure sp_BuscarClientes (in codCli int)
		begin
			select 
            C.codigoCliente,
            C.nitCliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente 
            from Clientes C
            where codigoCliente = codCli;
            
		End $$
        
Delimiter ;
call sp_BuscarClientes (1);


-- ---- Eliminar Clientes ----
Delimiter $$
	create procedure sp_EliminarClientes (in codCli int)
		begin
			Delete from Clientes
				where codigoCliente = codCli;
                
		End $$
        
Delimiter ;
/*call sp_EliminarClientes(2);
call sp_ListarClientes();*/


-- ---- Editar Clientes ----
Delimiter $$
	create procedure sp_EditarClientes (in codCli int, in nCliente varchar (10), in nomCliente varchar (50), in apCliente varchar (50),
    in direcCliente varchar (150), in telCliente varchar (12), in corrCliente varchar (100))
		begin
			update Clientes C
				set 
				C.nitCliente = nCliente,
				C.nombreCliente = nomCliente,
				C.apellidoCliente = apCliente,
				C.direccionCliente = direcCliente,
				C.telefonoCliente = telCliente,
				C.correoCliente = corrCliente
                Where codigoCliente = codCli;
                
		End $$
        
Delimiter ;
call sp_EditarClientes(05, '875302148', 'Daniel', 'Hernandez', 'Zona 14', '56897400', 'danielhernandez@gmail.com');


-- ---------------------- Proveedores ----------------------
-- ---- Agregar Proveedores -----
Delimiter $$
	create procedure sp_AgregarProveedores(in codigoProveedor int, in nitProveedor varchar (10), in nombreProveedor varchar (60), in apellidoProveedor varchar (60),
    in direccionProveedor varchar (150), in razonSocial varchar (60), in contactoPrincipal varchar (100), in paginaWeb varchar (50))
		begin
			insert into Proveedores (codigoProveedor, nitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, 
									contactoPrincipal, paginaWeb)
			values (codigoProveedor, nitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
            
		End $$
        
Delimiter ;
call sp_AgregarProveedores(10, '895645210', 'Miguel', 'Fernandez', 'Colonia Maya', 'Mayita', '89751200', 'Mayita.com');
call sp_AgregarProveedores(11, '782406698', 'Pedro' , 'Gonzales', 'Chiquimullilla', 'COBAN', '69870211', 'COBANStore.com');
call sp_AgregarProveedores(12, '982026575', 'Rodrigo', 'Hernandez', 'Villa Nueva', 'La Torre', '35642100', 'La Torre.com');
call sp_AgregarProveedores(13, '895100021', 'Thomas', 'Velazques', 'Zona 1', 'Tienda La Moderna', '78950233', 'LaModernita.com');


-- ---- Listar Proveedores ----
Delimiter $$
	create procedure sp_ListarProveedores()
		begin
			select
            P.codigoProveedor,
            P.nitProveedor,
            P.nombreProveedor,
            P.apellidoProveedor,
            P.direccionProveedor,
            P.razonSocial,
            P.contactoPrincipal,
            P.paginaWeb
            from Proveedores P;
            
		End $$
        
Delimiter ;
call sp_ListarProveedores();


-- -- Buscar Proveedores ----
Delimiter $$
	create procedure sp_BuscarProveedores(in codPro int)
		begin
			select
            P.codigoProveedor,
            P.nitProveedor,
            P.nombreProveedor,
            P.apellidoProveedor,
            P.direccionProveedor,
            P.razonSocial,
            P.contactoPrincipal,
            P.paginaWeb
            from Proveedores P
            where codigoProveedor = codPro;
            
		End $$
        
Delimiter ;
call sp_BuscarProveedores(10);


-- ---- Eliminar Proveedores ----
Delimiter $$
	create procedure sp_EliminarProveedores(in codPro int)
		begin
			Delete from Proveedores
				where codigoProveedor = codPro;
                
		End $$
        
Delimiter ;
/*call sp_EliminarProveedores(13);
call sp_ListarProveedores();*/


-- ---- Editar Proveedores ----
Delimiter $$
	create procedure sp_EditarProveedores(in codPro int, in nProveedor varchar (10), in nomProveedor varchar (60), in apProveedor varchar (60),
    in direcProveedor varchar (150), in razSocial varchar (60), in conPrincipal varchar (100), in pagWeb varchar (50))
		begin
			update Proveedores P
				set
				P.nitProveedor = nProveedor,
				P.nombreProveedor = nomProveedor,
				P.apellidoProveedor = apProveedor,
				P.direccionProveedor = direcProveedor,
				P.razonSocial = razSocial,
				P.contactoPrincipal = conPrincipal,
				P.paginaWeb = pagWeb
                where codigoProveedor = codPro;
                
		End $$
        
Delimiter ;
call sp_EditarProveedores(10, '875031469', 'Gustavo', 'Basquez', 'Juana De Arco', 'Despensa Familiar', '57891022', 'despensaFamiliar.com');


-- ---------------------- Compras ----------------------
-- ---- Agregar Compras -----
Delimiter $$
	create procedure sp_AgregarCompras(in numeroDocumento int, in fechaDocumneto Date, descripcion varchar (90), in totalDocumento Decimal (10,2))
		begin
			insert into Compras (numeroDocumento , fechaDocumento , descripcion , totalDocumento)
				values (numeroDocumento , fechaDocumneto , descripcion , totalDocumento);
                
		End $$
        
Delimiter ;
call sp_AgregarCompras(1, '2023-02-15', 'Pasta Colgate', '500');
call sp_AgregarCompras(15, '2024-01-14', 'Peluche para la novia', '1000');
call sp_AgregarCompras(4, '2020-10-20', 'Harina para panqueques', '100');
call sp_AgregarCompras(10, '2020-10-20', 'Frituras de maiz sabor a queso', '100');


-- ---- Listar Compras -----
Delimiter $$
	create procedure sp_ListarCompras()
		begin
			select
            C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C;
            
		End $$
        
Delimiter ;
call sp_ListarCompras();


-- ---- Buscar Compras -----
Delimiter $$
	create procedure sp_BuscarCompras(in numDoc int)
		begin
			select
            C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C
            where numeroDocumento = numDoc;
            
		End $$
        
Delimiter ;
call sp_BuscarCompras(1);


-- ---- Eliminar Compras -----
Delimiter $$
	create procedure sp_EliminarCompras(in numDoc int)
		begin
			Delete from Compras
				where numeroDocumento = numDoc;
                
		End $$
        
Delimiter ;
/*call sp_EliminarCompras(4);
call sp_ListarCompras();*/


-- ---- Editar Compras -----
Delimiter $$
	create procedure sp_EditarCompras(in numDoc int, in fechaDoc Date, descrip varchar (90), in totalDoc Decimal (10,2))
		begin 
			update Compras C 
				set
                C.fechaDocumento = fechaDoc,
                C.descripcion = descrip,
                C.totalDocumento = totalDoc
                where numeroDocumento = numDoc;
                
		End $$
        
Delimiter ;
call sp_EditarCompras(1, '2023-11-22', 'Jabon en Liquido', '125');
    

-- ---------------------- CargoEmpleado ----------------------
-- ---- Agregar CargoEmpleados -----
Delimiter $$
	create procedure sp_AgregarCargoEmpleados(in codigoCargoEmpleado int, in nombreCargo varchar (50), descripcionCargo varchar (90))
		begin
			insert into CargoEmpleados (codigoCargoEmpleado, nombreCargo, descripcionCargo)
				values (codigoCargoEmpleado, nombreCargo, descripcionCargo);
                
		End $$
        
Delimiter ;
call sp_AgregarCargoEmpleados(1, 'Coordinador de Personal', 'Supervisa las actividades relacionadas');
call sp_AgregarCargoEmpleados(9, 'Asistente de recursos humanos', 'Brindar apoyo administrativo');
call sp_AgregarCargoEmpleados(11, 'Analista de Nomina', 'Gestiona de maneta precisa y eficiente el procedimiento');
call sp_AgregarCargoEmpleados(12, 'Analista de Nomina', 'Gestiona de manera eficiente');


-- ---- Listar CargoEmpleados -----
Delimiter $$
	create procedure sp_ListarCargoEmpleados()
		begin
			select
            CE.codigoCargoEmpleado,
            CE.nombreCargo,
            CE.descripcionCargo
            from CargoEmpleados CE;
            
		End $$

Delimiter ;
call sp_ListarCargoEmpleados();


-- ---- Buscar CargoEmpleados -----
Delimiter $$
	create procedure sp_BuscarCargoEmpleados(in codCargoE int)
		begin
			select
            CE.codigoCargoEmpleado,
            CE.nombreCargo,
            CE.descripcionCargo
            from CargoEmpleados CE
            where codigoCargoEmpleado = codCargoE;
            
		End $$

Delimiter ;
call sp_BuscarCargoEmpleados(11);


-- ---- Eliminar CargoEmpleados -----
Delimiter $$
	create procedure sp_EliminarCargoEmpleados(in codCargoEmp int)
		begin 
			Delete from CargoEmpleados
				where codigoCargoEmpleado = codCargoEmp;
                
		End $$
        
Delimiter ;
-- call sp_EliminarCargoEmpleados(9);
-- call sp_ListarCargoEmpleados();*/


-- ---- Editar CargoEmpleados -----
Delimiter $$
	create procedure sp_EditarCargoEmpleados(in codigoCE int, in nombreC varchar (50), descripcionC varchar (90))
		begin
			update CargoEmpleados CE
				set
				CE.nombreCargo = nombreC,
				CE.descripcionCargo = descripcionC
                where codigoCargoEmpleado = codigoCE;
                
		End $$
        
Delimiter ;
call sp_EditarCargoEmpleados(11, 'Repartidor de productos', 'Reparte los productos de los cleintes');



-- ---------------------- TipoProducto ----------------------
-- ---- Agregar TipoProducto -----
Delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int, in descripcion varchar(90))
		begin
			insert into TipoProducto (codigoTipoProducto, descripcion)
				values (codigoTipoProducto, descripcion);
                
		End $$
        
Delimiter ;
call sp_AgregarTipoProducto(15, 'Estuche para lapiceros y crayones');
call sp_AgregarTipoProducto(16, 'Aceite para carros automaticos');  
call sp_AgregarTipoProducto(17, 'Juego de carros con pista armable');   
call sp_AgregarTipoProducto(18, 'Una PlayStation 5 con dos controles');  


-- ---- Listar TipoProducto -----
Delimiter $$
		create procedure sp_ListarTipoProducto()
			begin
				select
                TP.codigoTipoProducto,
                TP.descripcion
                from TipoProducto TP;
                
			End $$
            
Delimiter ;
call sp_ListarTipoProducto();


-- ---- Buscar TipoProducto -----
Delimiter $$
	Create procedure sp_BuscarTipoProducto(in codigoTP int)
		begin
			select
            TP.codigoTipoProducto,
            TP.descripcion
            from TipoProducto TP
            where codigoTipoProducto = codigoTP;
            
		End $$
        
Delimiter ;
call sp_BuscarTipoProducto(17);


-- ---- Eliminar TipoProducto -----
Delimiter $$
	create procedure sp_EliminarTipoProducto(in codigoTP int)
		begin
			Delete from TipoProducto
				where codigoTipoProducto = codigoTP;
                
		End $$
        
Delimiter ;
/*call sp_EliminarTipoProducto(18);
call sp_ListarTipoProducto();*/


-- ---- Editar TipoProducto -----
Delimiter $$
	create procedure sp_EditarTipoProducto(in codigoTP int, in descripcionTP varchar(90))
		begin
			Update TipoProducto TP
				set
                TP.descripcion = descripcionTP
                where codigoTipoProducto = codigoTP;
                
		End $$
        
Delimiter $$
call sp_EditarTipoproducto(17, 'Un collar para decoracion de hogar');


-- ------------------- Telefono Proveedor --------------------
-- ---- Agregar TelefonoPrveedor ----  
Delimiter $$
	create procedure sp_AgregarTelefonoProveedor(in codigoTelefonoProveedor int, in numeroPrincipal varchar(8), in numeroSecundario varchar(8),
    in observaciones varchar(45), in codigoProveedor int)
		begin
			insert into TelefonoProveedor (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
				values (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor );
                
		End $$
        
Delimiter ;
call sp_AgregarTelefonoProveedor(20, '23101847', '20235678', 'Lo siento si no contesto', 10);
call sp_AgregarTelefonoProveedor(21, '20245789', '12030000', 'Estoy en el trabajo', 11);
call sp_AgregarTelefonoProveedor(22, '17214506', '20201547', 'No estoy en la casa jajajaja', 12);
call sp_AgregarTelefonoProveedor(23, '68936574', '17542532', 'En el estudio que triste', 13);


-- ---- Listar TelefonoProveedor ----  
Delimiter $$
	create procedure sp_ListarTelefonoProveedor()
		begin
			select
            TP.codigoTelefonoProveedor,
            TP.numeroPrincipal,
            TP.numeroSecundario,
            TP.observaciones,
            TP.codigoProveedor 
            from TelefonoProveedor TP;
            
		End $$
        
Delimiter ;
call sp_ListarTelefonoProveedor();


-- ---- Buscar TelefonoProveedor ----  
Delimiter $$
	create procedure sp_BuscarTelefonoProveedor(in codigoTP int)
		begin
			select
			TP.codigoTelefonoProveedor,
            TP.numeroPrincipal,
            TP.numeroSecundario,
            TP.observaciones,
            TP.codigoProveedor 
            from TelefonoProveedor TP
            where codigoTelefonoProveedor = codigoTP;
            
		End $$
        
Delimiter ;
call sp_BuscarTelefonoProveedor(20);


-- ---- Eliminar TelefonoProveedor ---- 
Delimiter $$
	create procedure sp_EliminarTelefonoProveedor(in codigoTP int)
		begin
			Delete from TelefonoProveedor
				where codigoTelefonoProveedor = codigoTP;
                
		End $$
        
Delimier ;
/*call sp_EliminarTelefonoProveedor(10);
call sp_ListarTelefonoProveedor();*/


-- ---- Editar TelefonoProveedor ---- 
Delimiter $$
	create procedure sp_EditarTelefonoProveedor(in codigoTP int, in numPrincipal varchar(8), in numSecundario varchar(8), 
	in observaciones varchar(45), in codigoProveedor int)
		begin
			update TelefonoProveedor TP
				set
                TP.numeroPrincipal = numPrincipal,
                TP.numeroSecundario = numSecundario,
                TP.observaciones = observaciones,
                TP.codigoProveedor = codigoProveedor 
                where codigoTelefonoProveedor = codigoTP;
                
		End $$
        
Delimiter ;
call sp_EditarTelefonoProveedor(21, '56489700', '63201459', 'Jugando Mario Bros', 11);


-- ------------------- Prodcutos --------------------
-- ---- Agregar Prodcutos ----  
Delimiter $$
	create procedure sp_AgregarProductos (in codigoProducto varchar(15), in descripcionProducto varchar(100), in precioUnitario decimal(10,2), 
    in precioDocena decimal(10,2), in precioMayor decimal(10,2), in existencia int, in codigoTipoProducto int, in codigoProveedor int)
		Begin
			insert into Productos(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, 
            codigoTipoProducto, codigoProveedor) 
				values(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto,
						codigoProveedor);
                        
		End $$
        
Delimiter ;
call sp_AgregarProductos('45', 'Estuche de metal', 100.20, 15.00, 125.50, 200, 15, 10);
call sp_AgregarProductos('32', 'Carros Automaticos', 130.00, 10.00, 150.40, 500, 16, 11);
call sp_AgregarProductos('21', '15 piezas armables', 120.10, 65.00, 115.00, 600, 17, 12);
call sp_AgregarProductos('13', '5 Juegos incluidos', 100.45, 25.00, 500.00, 20, 18, 13);


-- ---- Listar Prodcutos ----
Delimiter $$
	create procedure sp_ListarProductos()
		begin
			select
            P.codigoProducto, 
            P.descripcionProducto,
            P.precioUnitario,
            P.precioDocena ,
            P.precioMayor,
			P.existencia, 
            P.codigoTipoProducto,
            P.codigoProveedor
            from Productos P;
            
		End $$
        
Delimiter ;
call sp_ListarProductos();


-- ---- Buscar Prodcutos ----
Delimiter $$
	create procedure sp_BuscarProductos(in codigoP int)
		begin
			select
            P.codigoProducto, 
            P.descripcionProducto,
            P.precioUnitario,
            P.precioDocena ,
            P.precioMayor,
			P.existencia, 
            P.codigoTipoProducto,
            P.codigoProveedor
            from Productos P
            where codigoProducto = codigoP;
            
		End $$
        
Delimiter ;
call sp_BuscarProductos(21);


-- ---- Eliminar Prodcutos ----
Delimiter $$
	create procedure sp_EliminarProductos(in codigoP varchar(15))
		begin
			Delete from Productos
				where codigoProducto = codigoP;
                
		End $$
        
Delimiter ;
/*call sp_EliminarProductos(45);
call sp_ListarProductos();*/


-- ---- Editar Prodcutos ----
Delimiter $$
	create procedure sp_EditarProductos(in codigoP varchar(15), in descripcionP varchar(100), in precioUP decimal(10,2), 
    in precioDP decimal(10,2), in precioMP decimal(10,2), in existencia int, in codigoTipoProducto int, in codigoProveedor int)
		begin 
			Update Productos P
				set
                P.descripcionProducto = descripcionP,
				P.precioUnitario = precioUP,
				P.precioDocena =  precioDP,
				P.precioMayor = precioMP,
				P.existencia = existencia,
				p.codigoTipoProducto = codigoTipoProducto,
				p.codigoProveedor = codigoProveedor 
                where codigoProducto = codigoP;
                
		End $$
        
Delimiter ;
call sp_EditarProductos(32, 'Carros incluidos', 100.10, 25, 1010.50, 25, 16, 11);


-- ------------------- Empleados --------------------
-- ---- Agregar Empleados ----  
Delimiter $$
	create procedure sp_AgregarEmpleados(in codigoEmpleado int, in nombreEmpleado varchar(50), in apellidoEmpleado varchar(50),
    in sueldo decimal(10,2), in direccion varchar(150), in turno varchar(15), in codigoCargoEmpleado int)
		begin	
			insert into Empleados(codigoEmpleado, nombreEmpleado, apellidoEmpleado, sueldo, direccion, turno, codigoCargoEmpleado) 
				values (codigoEmpleado, nombreEmpleado, apellidoEmpleado, sueldo, direccion, turno, codigoCargoEmpleado);
                
		End $$
        
Delimiter ;
call sp_AgregarEmpleados(1, 'Daniel', 'Hernandez', '3500', 'Mixco', 'Mañana', 1);
call sp_AgregarEmpleados(5, 'Fredy', 'Gomez', '6000', 'Juana de Arco', 'Noche', 9);
call sp_AgregarEmpleados(3, 'Josue', 'Perez', '4500', 'Zona 18', 'Mañana', 11);
call sp_AgregarEmpleados(4, 'Fernando', 'Perez', '7000', 'Zona 14', 'Tarde', 12);


-- ---- Listar Empleados ----  
Delimiter $$
	create procedure sp_ListarEmpleados()
		begin
			select
            E.codigoEmpleado,
            E.nombreEmpleado,
            E.apellidoEmpleado,
            E.sueldo,
            E.direccion,
            E.turno,
            E.codigoCargoEmpleado
            from Empleados E;
            
		End $$
        
Delimiter ;
call sp_ListarEmpleados();


-- ---- Buscar Empleados ----
Delimiter $$
	Create procedure sp_BuscarEmpleados(in codigoE int)
		begin
			select
            E.codigoEmpleado,
            E.nombreEmpleado,
            E.apellidoEmpleado,
            E.sueldo,
            E.direccion,
            E.turno,
            E.codigoCargoEmpleado
            from Empleados E
            where codigoEmpleado = codigoE;
            
		End $$
        
Delimiter ;
call sp_BuscarEmpleados(2);


-- ---- Eliminar Empleados ----
Delimiter $$
	create procedure sp_EliminarEmpleados(in codigoE int)
		begin
			Delete from Empleados
				where codigoEmpleado = codigoE;
                
		End $$
        
Delimiter ;
/*call sp_EliminarEmpleados(1);
call sp_ListarEmpleados();*/


-- ---- Editar Empleados ----
Delimiter $$
	create procedure sp_EditarEmpleados(in codigoE int, in nombreE varchar(50), in apellidoE varchar(50),
    in sueldo decimal(10,2), in direccion varchar(150), in turno varchar(15), in codigoCargoEmpleado int)
		begin
			update Empleados E
            set
            E.nombreEmpleado = nombreE,
            E.apellidoEmpleado = apellidoE,
            E.sueldo = sueldo,
            E.direccion = direccion,
            E.turno = turno,
            E.codigoCargoEmpleado = codigoCargoEmpleado
            where codigoEmpleado = codigoE;
            
		End $$
        
Delimiter ;
call sp_EditarEmpleados(1, 'Anthony', 'Davis', '15500', 'Zacapa', 'Dia', 1);


-- ------------------- Facturas --------------------
-- ---- Agregar Facturas ----  
Delimiter $$
	create procedure sp_AgregarFacturas(in numeroFactura int, in estado varchar(50), in totalFactura decimal(10,2), 
	in fechaFactura varchar(45), in codigoCliente int, in codigoEmpleado int)
		Begin
			insert into Facturas(numeroFactura, estado, totalFactura, fechaFactura, codigoCliente, codigoEmpleado) 
            values (numeroFactura, estado, totalFactura, fechaFactura, codigoCliente, codigoEmpleado);
            
		End $$
Delimiter ;

call sp_AgregarFacturas(30, 'Guatemala', '1025.60', '2023-06-15', 01, 1);
call sp_AgregarFacturas(31, 'Zacapa', '2023.00', '2020-05-12', 02, 5);
call sp_AgregarFacturas(32, 'Guatemala', '600.00', '2025-08-20', 03, 3);
call sp_AgregarFacturas(33, 'Guatemala', '2010.00', '2026-10-20', 04, 4);


-- ---- Listar Facturas ----  
Delimiter $$
	create procedure sp_ListarFacturas()
		begin
			select
            F.numeroFactura,
            F.estado,
            F.totalFactura,
            F.fechaFactura,
            F.codigoCliente,
            F.codigoEmpleado
            from Facturas F;
            
		End $$
        
Delimiter ;
call  sp_ListarFacturas();


-- ---- Buscar Facturas ----   
Delimiter $$
	create procedure sp_BuscarFacturas(in numeroFac int)
		begin 
			select
            F.numeroFactura,
            F.estado,
            F.totalFactura,
            F.fechaFactura,
            F.codigoCliente,
            F.codigoEmpleado
            from Facturas F
            where numeroFactura = numeroFac;
            
		End $$
        
Delimiter ;
call sp_BuscarFacturas(33);


-- ---- Eliminar Facturas ----   
Delimiter $$
	create procedure sp_EliminarFacturas(in numeroFac int)
		begin
			Delete from Facturas 
				where numeroFactura = numeroFac;
                
		End $$
        
Delimiter ;
/*call sp_EliminarFacturas(32);
call sp_ListarFacturas();*/


-- ---- Editar Facturas ----   
Delimiter $$
	create procedure sp_EditarFacturas(in numeroFac int, in estado varchar(50), in totalFac decimal(10,2), 
		in fechaFac varchar(45), in codigoCliente int, in codigoEmpleado int)
			begin
				update Facturas F
                set
                F.estado  = estado,
			    F.totalFactura  = totalFac,
				F.fechaFactura  =  fechaFac,
				F.codigoCliente  = codigoCliente,
				F.codigoEmpleado = codigoEmpleado 
				where numeroFactura = numeroFac;
                
			End $$
            
Delimiter ;
call sp_EditarFacturas(30, 'Zacapa', '1600.00', '2024-05-19', 01, 1);


-- ------------------- Detalle Factura --------------------
-- ---- Agregar DetalleFactura ----
Delimiter $$
	create procedure sp_AgregarDetalleFactura(in codigoDetalleFactura int, in precioUnitario decimal(10,2), in cantidad int, 
    in numeroFactura int, in codigoProducto varchar(15))
		begin
			insert into DetalleFactura(codigoDetalleFactura, precioUnitario, cantidad, numeroFactura, codigoProducto)
				values (codigoDetalleFactura, precioUnitario, cantidad, numeroFactura, codigoProducto);
                
		End $$
        
Delimiter ;
call sp_AgregarDetalleFactura(50, '250.35', '50', 30, 45);
call sp_AgregarDetalleFactura(51, '650.45', '100', 31, 32);
call sp_AgregarDetalleFactura(52, '150.10', '80', 32, 21);
call sp_AgregarDetalleFactura(53, '95.45', '10', 33, 13);


-- ---- Listar DetalleFactura ----
Delimiter $$
	create procedure sp_ListarDetalleFactura()
		begin
			select
            DF.codigoDetalleFactura,
            DF.precioUnitario,
            DF.cantidad,
            DF.numeroFactura,
            DF.codigoProducto
            from DetalleFactura DF;
            
		End $$
        
Delimiter ;
call sp_ListarDetalleFactura();


-- ---- Buscar DetalleFactura ----
Delimiter $$
	create procedure sp_BuscarDetalleFactura(in codigoDF int)
		begin
			select
			DF.codigoDetalleFactura,
            DF.precioUnitario,
            DF.cantidad,
            DF.numeroFactura,
            DF.codigoProducto
            from DetalleFactura DF
            where codigoDetalleFactura = codigoDF;
            
		End $$
        
Delimiter ;
call sp_BuscarDetalleFactura(50);


-- ---- Eliminar DetalleFactura ----
Delimiter $$
	create procedure sp_EliminarDetalleFactura(in codigoDF int)
		begin
			Delete from DetalleFactura 
				where codigoDetalleFactura = codigoDF;
                
		End $$
        
Delimiter ;
/*call sp_EliminarDetalleFactura(53);
call sp_ListarDetalleFactura();*/


-- ---- Editar DetalleFactura ----
Delimiter $$
	create procedure sp_EditarDetalleFactura(in codigoDF int, in precioUnitario decimal(10,2), in cantidad int, in numeroFactura int,
    in codigoProducto varchar(15))
		begin 
			update DetalleFactura DF
            set
            DF.precioUnitario = precioUnitario,
            DF.cantidad = cantidad,
            DF.numeroFactura = numeroFactura,
            DF.codigoProducto = codigoProducto
            where codigoDetalleFactura = codigoDF;
		
        End $$
        
Delimiter ;
call sp_EditarDetalleFactura(51, '1025.20', '49', 31, 32);


-- ------------------- Detalle Compra --------------------
-- ---- Agregar DetalleCompra ----
Delimiter $$
	create procedure sp_AgregarDetalleCompra(in codigoDetalleCompra int, in costoUnitario decimal(10,2), in cantidad int, in codigoProducto varchar(15),
    in numeroDocumento int)
		begin 
			insert into DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
				values (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento);
                
		End $$
        
Delimiter ;
call sp_AgregarDetalleCompra(70, '100.50', '90', 45, 1);
call sp_AgregarDetalleCompra(71, '60.10', '50', 32, 15);
call sp_AgregarDetalleCompra(72, '45.21', '20', 21, 4);
call sp_AgregarDetalleCompra(73, '80.00', '60', 13, 10);


-- ---- Listar DetalleCompra ----
Delimiter $$
	create procedure sp_ListarDetalleCompra()
		begin
			select
            DC.codigoDetalleCompra,
            DC.costoUnitario,
            DC.cantidad,
            DC.codigoProducto,
            DC.numeroDocumento 
            from DetalleCompra DC;
            
		End $$
        
Delimiter ;
call sp_ListarDetalleCompra();


-- ---- Buscar DetalleCompra ----
Delimiter $$
	Create procedure sp_BuscarDetalleCompra(in codigoDC  int)
		begin
			select
            DC.codigoDetalleCompra,
            DC.costoUnitario,
            DC.cantidad,
            DC.codigoProducto,
            DC.numeroDocumento
            from DetalleCompra DC
            where codigoDetalleCompra = codigoDC;
            
		End $$
        
Delimiter ;
call sp_BuscarDetalleCompra(73);


-- ---- Eliminar DetalleCompra ----
Delimiter $$
	create procedure sp_EliminarDetalleCompra(in codigoDC int)
		begin 
			Delete from DetalleCompra
				where codigoDetalleCompra = codigoDC;
                
		End $$
        
Delimiter ;
/*call sp_EliminarDetalleCompra(72);
call sp_ListarDetalleCompra();*/


-- ---- Editar DetalleCompra ----
Delimiter $$
	create procedure sp_EditarDetalleCompra(in codigoDC int, in costoUni decimal(10,2), in cantidad int, in codigoProducto varchar(15),
    in numeroDocumento int)
		begin
			update DetalleCompra DC
            set
            DC.costoUnitario  = costoUni,
            DC.cantidad = cantidad,
            DC.codigoProducto =  codigoProducto,
            DC.numeroDocumento = numeroDocumento
            where codigoDetalleCompra  = codigoDC;
            
		End $$
        
Delimiter ;
call sp_EditarDetalleCompra(70, '250.10', '180', 45, 1);


-- ------------------- Email Proveedor --------------------
-- ---- Agregar EmailProveedor ----
Delimiter $$
	create procedure sp_AgregarEmailProveedor(in codigoEmailProveedor int, in emailProveedor varchar(50), in descripcion varchar(100), 
    in codigoProveedor int)
		begin 
			insert into EmailProveedor (codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
				values (codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor);
                
		End $$
        
Delimiter ;
call sp_AgregarEmailProveedor(90, 'ceferino@gamil.com', 'correo para contactar', 10);
call sp_AgregarEmailProveedor(91, 'aron@gmail.com', 'correo para contactar', 11);
call sp_AgregarEmailProveedor(92, 'danilo@gmail.com', 'correo para contactar', 12);
call sp_AgregarEmailProveedor(93, 'fredy@gamil.com', 'correo para contactar', 13);

    
-- ---- Listar EmailProveedor ----
Delimiter $$
	create procedure sp_ListarEmailProveedor()
		begin
			select 
			EP.codigoEmailProveedor,
			EP.emailProveedor,
			EP.descripcion,
			EP.codigoProveedor
			from EmailProveedor EP;
            
		End $$
        
Delimiter ;
call sp_ListarEmailProveedor();


-- ---- Buscar EmailProveedor ----
Delimiter $$
	Create procedure sp_BuscarEmailProveedor(in codigoEP int)
		begin
			select
			EP.codigoEmailProveedor,
            EP.emailProveedor,
            EP.descripcion,
            EP.codigoProveedor
            from EmailProveedor EP
            where codigoEmailProveedor = codigoEP;
            
		End $$
        
Delimiter ;
call sp_BuscarEmailProveedor(93);


-- ---- Eliminar EmailProveedor ----
Delimiter $$
	create procedure sp_EliminarEmailProveedor(in codigoEP int)
		begin
			Delete from EmailProveedor 
				where codigoEmailProveedor = codigoEP;
                
		End $$
        
Delimiter ;
/*call sp_EliminarEmailProveedor(91);
call sp_ListarEmailProveedor();*/


-- ---- Editar EmailProveedor ----
Delimiter $$
	create procedure sp_EditarEmailProveedor(in codigoEP int, in emailProveedor varchar(50), in descripcion varchar(100), in codigoProveedor int)
		begin
			update EmailProveedor EP
            set
            EP.emailProveedor = emailProveedor,
            EP.descripcion = descripcion,
            EP.codigoProveedor = codigoProveedor
            where codigoEmailProveedor = codigoEP;
            
		End $$
        
Delimiter ;;
call sp_EditarEmailProveedor(90, 'ulises22@gmail.com', 'Oficial', 10);



-- ------------------- Joins --------------------
select * from DetalleFactura
	join Facturas on DetalleFactura.numeroFactura = Facturas.numeroFactura
	join Clientes on Facturas.codigoCliente = Clientes.codigoCliente
    join Productos on DetalleFactura.codigoProducto = Productos.codigoProducto
    
    where Facturas.numeroFactura = 30;



-- ------------------- triggers --------------------
-- -------------------Triger_After_ Insert de DetalleFactura con nombre PrecioProductos -------------------
delimiter $$
create trigger tr_PrecioProductos_After_Insert
after insert on DetalleCompra
for each row 
begin
	declare total decimal(10,2);
    declare cantidad int;
    set total = new.costoUnitario * new.cantidad;
 
	update Productos
	set precioUnitario = total * 0.40,
		precioDocena  = total * 0.35 * 12,
        precioMayor = total * 0.25
    where Productos.codigoProducto = new.codigoProducto;
	update Productos
        set Productos.existencia = Productos.existencia - new.cantidad
	where Productos.codigoProducto = new.codigoProducto;
 
end $$

delimiter ;


-- ------------------- Triger_After_ Insert de DetalleFactura con nombre TotalDocumento -------------------
delimiter $$
create trigger tr_TotalDocumento_After_Insert
after insert on DetalleCompra
for each row
begin
    declare total decimal(10,2);
    select sum(costoUnitario * cantidad) into total from DetalleCompra 
    where numeroDocumento = NEW.numeroDocumento;
    update Compras 
		set totalDocumento = total 
	where numeroDocumento = NEW.numeroDocumento;
end $$
delimiter ;


-- ------------------- Triger_After_ Insert de DetalleFactura con nombre PrecioUnitario -------------------
delimiter $$
create trigger tr_PrecioUnitario_After_Upd
after insert on DetalleCompra
for each row
begin
 
	declare precio decimal(10,2);
    set precio = (select precioUnitario from Productos where codigoProducto = new.codigoProducto);
    update DetalleFactura
    set DetalleFactura.precioUnitario = precioP
    where DetalleFactura.codigoProducto = NEW.codigoProducto;
end $$
delimiter ;


-- ------------------- Triger_After_ Update de DetalleFactura con nombre TotalFactura -------------------
delimiter $$
create trigger tr_TotalFactura_Aftr_U
after update on DetalleFactura
for each row
begin
	declare totalFactura decimal(10,2);
    select sum(precioUnitario * cantidad) into totalFactura from DetalleFactura
    where numeroFactura = new.numeroFactura;
    update Factura
		set Factura.totalFactura = totalFactura
	where Factura.numeroFactura = new.numeroFactura;
end $$
delimiter ;




set global time_zone = '-6:00'	