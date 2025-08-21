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
LEFT JOIN Persona p ON emp.cve_persona = p.cve_persona
WHERE e.activo = 1;
SELECT * FROM vista_equipo_completo;

DROP VIEW IF EXISTS v_bajaHISTO;

CREATE VIEW v_bajaHISTO AS
SELECT 
    b.cve_baja,
    b.fecha_baja,
    b.motivo,
    b.observaciones,

    e.cve_equipo,
    e.nombre AS nombre_equipo,
    e.marca,
    e.modelo,
    e.numero_serie,
    e.numero_utl,

    emp.cve_empleado,
    emp.numero_empleado,

    p.cve_persona,
    p.nombre,
    p.apellido1,
    p.apellido2,
    p.correo,
    p.telefono

FROM Baja b
INNER JOIN Equipo e ON b.cve_equipo = e.cve_equipo
INNER JOIN Empleado emp ON b.cve_empleado = emp.cve_empleado
INNER JOIN Persona p ON emp.cve_persona = p.cve_persona
WHERE e.activo = 0;


DROP VIEW IF EXISTS v_baja;

CREATE VIEW v_baja AS
WITH UltimaBaja AS (
    SELECT 
        b.*,
        ROW_NUMBER() OVER (PARTITION BY b.cve_equipo ORDER BY b.fecha_baja DESC) AS rn
    FROM Baja b
)

SELECT 
    ub.cve_baja,
    ub.fecha_baja,
    ub.motivo,
    ub.observaciones,

    e.cve_equipo,
    e.nombre AS nombre_equipo,
    e.marca,
    e.modelo,
    e.numero_serie,
    e.numero_utl,

    emp.cve_empleado,
    emp.numero_empleado,

    p.cve_persona,
    p.nombre,
    p.apellido1,
    p.apellido2,
    p.correo,
    p.telefono

FROM UltimaBaja ub
INNER JOIN Equipo e ON ub.cve_equipo = e.cve_equipo
INNER JOIN Empleado emp ON ub.cve_empleado = emp.cve_empleado
INNER JOIN Persona p ON emp.cve_persona = p.cve_persona
WHERE ub.rn = 1
AND e.activo = 0;

SELECT * FROM baja;
SELECT * FROM v_baja;
SELECT * FROM AsignacionEquipo;
SELECT * FROM Equipo;
SELECT * FROM empleado;