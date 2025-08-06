DROP VIEW IF EXISTS v_empleado1;

CREATE VIEW v_empleado1 AS
SELECT 
    E.cve_empleado,
    E.numero_empleado,
	E.activo AS empleadoActivo, 
    P.cve_persona,
    P.nombre,
    P.apellido1,
    P.apellido2,
    P.correo,
    U.usuario AS nombreUsuario,
    U.cve_usuario,
    U.contrasenia,
    U.rol,
    U.activo
FROM 
    empleado E
INNER JOIN persona P ON E.cve_persona = P.cve_persona
INNER JOIN usuario U ON E.cve_usuario = U.cve_usuario;

DROP VIEW IF EXISTS v_empleado;

CREATE VIEW v_empleado AS
SELECT 
    E.cve_empleado,
    E.numero_empleado,
    E.campus,
    E.edificio,
    E.activo AS empleadoActivo, 
    P.cve_persona,
    P.nombre,
    P.apellido1,
    P.apellido2,
    P.correo,
    U.usuario AS nombreUsuario,
    U.cve_usuario,
    U.contrasenia,
    U.rol,
    U.activo
FROM 
    empleado E
INNER JOIN persona P ON E.cve_persona = P.cve_persona
INNER JOIN usuario U ON E.cve_usuario = U.cve_usuario
WHERE 
    U.activo = 1;

DROP VIEW IF EXISTS vista_equipo_completo;

CREATE VIEW vista_equipo_completo AS
SELECT 
    e.cve_equipo,
    e.nombre AS nombre_equipo,
    e.edificio,
    e.departamento,
    e.marca,
    e.direccion_ip,
    e.direccion_mac,
    e.modelo,
    e.numero_serie,
    e.numero_utl,
    e.capacidad,
    e.ram,
    e.cpu,
    e.estado,
    e.periocidad_mantenimiento,
    e.descripcionEquipo,
    e.activo AS equipo_activo,

    ae.cve_asignacion,
    ae.fecha_asignacion,
    ae.fecha_fin,
    ae.activo AS asignacion_activa,

    emp.cve_empleado,
    emp.numero_empleado,
    p.nombre AS nombre_empleado,
    p.apellido1,
    p.apellido2,
    p.correo,
    p.telefono

FROM Equipo e
LEFT JOIN AsignacionEquipo ae
    ON ae.cve_asignacion = (
        SELECT cve_asignacion
        FROM AsignacionEquipo
        WHERE cve_equipo = e.cve_equipo AND activo = 1
        ORDER BY fecha_asignacion DESC
        LIMIT 1
    )
LEFT JOIN Empleado emp ON ae.cve_empleado = emp.cve_empleado
LEFT JOIN Persona p ON emp.cve_persona = p.cve_persona;

SELECT * FROM vista_equipo_completo;
