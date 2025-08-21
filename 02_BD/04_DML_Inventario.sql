
-- Insertar en persona
INSERT INTO persona (nombre, apellido1, apellido2, correo)
VALUES
( 'Juan', 'Pérez', 'Gómez', 'juan.perez@example.com'),
('Ana', 'López', 'Martínez', 'ana.lopez@example.com');

-- Insertar en usuario
INSERT INTO usuario ( usuario, contrasenia, rol, activo)
VALUES
('001', '1234', 'ADMINISTRADOR', 1);


-- Insertar en empleado
INSERT INTO empleado ( numero_empleado, campus, edificio,  activo, cve_persona, cve_usuario)
VALUES
( '001',"Campus 2", "F", 1, 1, 1);


CALL sp_insertar_empleado(
    'Ana', 'López', 'García',
    '4771234567', 'ana.lopez@utl.edu.mx',
    1, -- Persona activa
    '2300134', -- Número de empleado
    1,  -- Empleado activo
	1
);


SET @idEquipo := 0;

-- Llamar al procedimiento con los parámetros correctos
CALL sp_insertar_equipo(
    'Laptop Dell',          -- p_nombre
     'F',                 -- p_marca
	'Tecnologia',                 -- p_marca
    'Dell',                 -- p_marca
    '192.168.1.100',        -- p_direccion_ip
    '00:1A:2B:3C:4D:5E',    -- p_direccion_mac
    'Inspiron 15',          -- p_modelo
    'SN123456789',          -- p_numero_serie
    'UTL456',               -- p_numero_utl
    '512GB',                -- p_capacidad
    '16',                     -- p_ram (INT, sin comillas)
      'intel',                 -- p_marca
    'Operativo',            -- p_estado
    'Trimestral',           -- p_periocidad_mantenimiento
      'Buen estado',           -- p_periocidad_mantenimiento
    1,                      -- p_activo
    1,                      -- p_cve_empleado (usa un ID válido de empleado)
    @idEquipo               -- OUT: p_cve_equipo
);


SELECT * FROM usuario;
SELECT * FROM v_empleado;
SELECT * FROM equipo;
SELECT * FROM persona;

SELECT cve_empleado, numero_empleado FROM empleado;

