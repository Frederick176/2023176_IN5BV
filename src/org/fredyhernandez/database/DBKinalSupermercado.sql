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
call sp_EliminarClientes(2);
call sp_ListarClientes();


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
call sp_EliminarProveedores(13);
call sp_ListarProveedores();


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
call sp_EliminarCompras(4);
call sp_ListarCompras();


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
	create procedure sp_EliminarCargoEmpleados(in codCargoE int)
		begin 
			Delete from CargoEmpleados
				where codigoCargoEmpleado = codCargoE;
                
		End $$
        
Delimiter ;
call sp_EliminarCargoEmpleados(9);
call sp_ListarCargoEmpleados();


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
call sp_EliminarTipoProducto(18);
call sp_ListarTipoProducto();


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



set global time_zone = '-6:00'