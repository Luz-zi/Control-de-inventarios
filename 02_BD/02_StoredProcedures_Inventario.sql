-- --------------------------------------
-- Insertar empleado
-- --------------------------------------

DROP PROCEDURE IF EXISTS insertarEmpleado;
DELIMITER $$
CREATE PROCEDURE insertarEmpleado(
    IN numero_empleado VARCHAR(7),
    IN e_nombre VARCHAR(45),
    IN e_apellido1 VARCHAR(30),
    IN e_apellido2 VARCHAR(30),
    IN e_campus VARCHAR(45),
    IN e_edificio VARCHAR(45),
    IN e_correo VARCHAR(100),
    IN e_usuario VARCHAR(13),
    IN e_contrasenia_usuario VARCHAR(8),
	IN e_rol VARCHAR(65),
    
    /* Valores de Retorno */
                                    OUT	cve_persona      INT,            
									OUT	cve_usuario      INT,
                                    OUT	cve_empleado      INT
)
BEGIN
    -- Insertar en la tabla persona
    INSERT INTO persona (nombre, apellido1, apellido2, correo)
    VALUES (e_nombre, e_apellido1, e_apellido2, e_correo);
    
    -- Obtenemos el idPersona que se genero:
    SET cve_persona = LAST_INSERT_ID();

    -- Insertar en la tabla usuario
    INSERT INTO usuario (usuario, contrasenia, rol)
    VALUES (e_usuario, e_contrasenia_usuario, e_rol);
    
    -- Obtener el idUsuario que se genero
    SET cve_usuario = LAST_INSERT_ID();

    -- Insertar en la tabla alumno
    INSERT INTO empleado (numero_empleado, campus, edificio, cve_persona, cve_usuario)
    VALUES (numero_empleado, e_campus, e_edificio, cve_persona, cve_usuario);
    
       SET cve_empleado = LAST_INSERT_ID();
END 
$$ DELIMITER ;

-- --------------------------------------
-- Actualizar empleado
-- --------------------------------------
DROP PROCEDURE IF EXISTS actualizarEmpleado;
DELIMITER $$
CREATE PROCEDURE actualizarEmpleado(
    IN e_nombre VARCHAR(45),
    IN e_apellido1 VARCHAR(30),
    IN e_apellido2 VARCHAR(30),
    IN e_correo VARCHAR(100),
    IN e_usuario VARCHAR(13),
    IN e_contrasenia_usuario VARCHAR(8),
    IN e_rol VARCHAR(65),
    IN e_campus VARCHAR(45),
    IN e_edificio VARCHAR(45),

    IN in_cve_persona INT,
    IN in_cve_usuario INT,
    IN in_cve_empleado INT
)
BEGIN
    -- Actualizar la tabla persona
    UPDATE persona
    SET nombre     = e_nombre,
        apellido1  = e_apellido1,
        apellido2  = e_apellido2,
        correo     = e_correo
    WHERE cve_persona = in_cve_persona;

    -- Actualizar la tabla usuario
    UPDATE usuario
    SET usuario      = e_usuario,
        contrasenia  = e_contrasenia_usuario,
        rol          = e_rol
    WHERE cve_usuario = in_cve_usuario;

    -- Actualizar la tabla empleado
    UPDATE empleado
    SET campus      = e_campus,
        edificio    = e_edificio
    WHERE cve_empleado = in_cve_empleado;
END $$
DELIMITER ;


-- --------------------------------------
-- Eliminar empleado
-- --------------------------------------
DROP PROCEDURE IF EXISTS eliminarEmpleado;
DELIMITER $$
CREATE PROCEDURE eliminarEmpleado(
    IN var_cve_empleado INT    -- ID de la sucursal a eliminar
)
BEGIN
    -- Cambiar el estado de la sucursal a inactiva
    UPDATE empleado
    SET activo = 0
    WHERE cve_empleado = var_cve_empleado;
END $$
DELIMITER ;

-- --------------------------------------
-- INVENTARIO
-- --------------------------------------

DROP PROCEDURE IF EXISTS sp_insertar_equipo;

DELIMITER $$
CREATE PROCEDURE sp_insertar_equipo (
    IN p_nombre VARCHAR(65),
     IN p_edificio VARCHAR(65),
      IN p_departamento VARCHAR(65),
    IN p_marca VARCHAR(65),
    IN p_direccion_ip VARCHAR(65),
    IN p_direccion_mac VARCHAR(65),
    IN p_modelo VARCHAR(65),
    IN p_numero_serie VARCHAR(65),
    IN p_numero_utl VARCHAR(65),
    IN p_capacidad VARCHAR(65),
    IN p_ram VARCHAR(65),
     IN p_cpu VARCHAR(65),
    IN p_estado VARCHAR(65),
    IN p_periocidad_mantenimiento VARCHAR(60),
   IN p_descripcionEquipo VARCHAR(200),
    IN p_activo INT,
    IN p_cve_empleado INT,
    OUT p_cve_equipo INT
)
BEGIN
    INSERT INTO Equipo (
        nombre, edificio, departamento, marca, direccion_ip, direccion_mac, modelo, numero_serie,
        numero_utl, capacidad, ram, cpu, estado, periocidad_mantenimiento,descripcionEquipo, activo
    ) VALUES (
        p_nombre, p_marca,p_edificio, p_departamento, p_direccion_ip, p_direccion_mac, p_modelo, p_numero_serie,
        p_numero_utl, p_capacidad, p_ram, p_cpu, p_estado, p_periocidad_mantenimiento, p_descripcionEquipo, p_activo
    );

    SET p_cve_equipo = LAST_INSERT_ID();

    IF p_cve_empleado IS NOT NULL AND p_cve_empleado > 0 THEN
        INSERT INTO AsignacionEquipo (cve_equipo, cve_empleado, activo)
        VALUES (p_cve_equipo, p_cve_empleado, 1);
    END IF;
END$$
DELIMITER ;

-- --------------------------------------
-- Actualizar equipo
-- --------------------------------------

DROP PROCEDURE IF EXISTS sp_actualizar_equipo;
DELIMITER $$

CREATE PROCEDURE sp_actualizar_equipo (
    IN p_nombre VARCHAR(65),
    IN p_edificio VARCHAR(65),
    IN p_departamento VARCHAR(25),
    IN p_marca VARCHAR(65),
    IN p_direccion_ip VARCHAR(65),
    IN p_direccion_mac VARCHAR(65),
    IN p_modelo VARCHAR(65),
    IN p_numero_serie VARCHAR(65),
    IN p_numero_utl VARCHAR(65),
    IN p_capacidad VARCHAR(65),
    IN p_ram VARCHAR(65),
    IN p_cpu VARCHAR(65),
    IN p_estado VARCHAR(30),
    IN p_periocidad_mantenimiento VARCHAR(65),
    IN p_descripcionEquipo VARCHAR(200),
    IN p_activo INT,
    IN p_cve_empleado INT,
    IN p_cve_equipo INT
)
BEGIN
    DECLARE empleadoActual INT;

    -- Actualizar información del equipo
    UPDATE Equipo
    SET nombre = p_nombre,
        edificio = p_edificio,
        departamento = p_departamento,
        marca = p_marca,
        direccion_ip = p_direccion_ip,
        direccion_mac = p_direccion_mac,
        modelo = p_modelo,
        numero_serie = p_numero_serie,
        numero_utl = p_numero_utl,
        capacidad = p_capacidad,
        ram = p_ram,
        cpu = p_cpu,
        estado = p_estado,
        periocidad_mantenimiento = p_periocidad_mantenimiento,
        descripcionEquipo  = p_descripcionEquipo,
        activo = p_activo
    WHERE cve_equipo = p_cve_equipo;

    -- Obtener el empleado activo asignado actualmente
    SELECT cve_empleado INTO empleadoActual
    FROM AsignacionEquipo
    WHERE cve_equipo = p_cve_equipo AND activo = 1
    LIMIT 1;

    -- DEBUG (puedes comentar después)
    SELECT empleadoActual AS empleado_actual, p_cve_empleado AS nuevo_empleado;

    -- Solo actualizar asignación si hay cambio real
    IF p_cve_empleado IS NOT NULL AND (empleadoActual IS NULL OR empleadoActual <> p_cve_empleado) THEN
        UPDATE AsignacionEquipo
        SET activo = 0,
            fecha_fin = NOW()
        WHERE cve_equipo = p_cve_equipo AND activo = 1;

        INSERT INTO AsignacionEquipo (cve_equipo, cve_empleado, fecha_asignacion, activo)
        VALUES (p_cve_equipo, p_cve_empleado, NOW(), 1);
    END IF;

END $$

DELIMITER $$

-- --------------------------------------
-- Insertar empleado en inventario
-- --------------------------------------

DROP PROCEDURE IF EXISTS sp_insertar_empleado;

DELIMITER $$
CREATE PROCEDURE sp_insertar_empleado (
    IN p_nombre VARCHAR(25),
    IN p_apellido1 VARCHAR(25),
    IN p_apellido2 VARCHAR(25),
    IN p_telefono VARCHAR(25),
    IN p_correo VARCHAR(25),
    IN p_activo_persona INT,
    IN p_numero_empleado VARCHAR(7),
    IN p_activo_empleado INT,
    IN p_cve_usuario INT
)
BEGIN
    DECLARE v_cve_persona INT;

    -- Insertar en Persona
    INSERT INTO Persona (
        nombre, apellido1, apellido2, telefono, correo, activo
    ) VALUES (
        p_nombre, p_apellido1, p_apellido2, p_telefono, p_correo, p_activo_persona
    );

    -- Obtener el ID de persona insertado
    SET v_cve_persona = LAST_INSERT_ID();

    -- Insertar en Empleado
    INSERT INTO Empleado (
        cve_persona, numero_empleado, activo, cve_usuario
    ) VALUES (
        v_cve_persona, p_numero_empleado, p_activo_empleado, p_cve_usuario
    );
END $$

DELIMITER ;

-- --------------------------------------
-- Buscar empleado
-- --------------------------------------

DROP PROCEDURE IF EXISTS sp_buscar_empleado_por_numeroEmpleado;
DELIMITER $$
CREATE PROCEDURE sp_buscar_empleado_por_numeroEmpleado (
    IN p_numero_empleado VARCHAR(20)
)
BEGIN
    SELECT 
        e.cve_empleado AS id,
        e.numero_empleado AS numeroEmpleado,
        e.activo,
        p.cve_persona AS idPersona,
        p.nombre,
        p.apellido1 AS apellido1,
        p.apellido2 AS apellido2,
        p.telefono,
        p.correo
    FROM empleado e
    JOIN persona p ON e.cve_persona = p.cve_persona
    WHERE e.numero_empleado = p_numero_empleado;
END$$
DELIMITER ;




    




