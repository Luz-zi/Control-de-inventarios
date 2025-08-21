DROP DATABASE IF EXISTS Inventario;
CREATE DATABASE Inventario;
USE Inventario;

CREATE TABLE Persona(
	cve_persona INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(25) NOT NULL,
	apellido1 VARCHAR(25) NOT NULL,
	apellido2 VARCHAR(25) NOT NULL,
	correo VARCHAR(25) NOT NULL,
    telefono VARCHAR(25)  NULL,
    activo      INT NOT NULL DEFAULT 1
);

CREATE TABLE Usuario(
	cve_usuario INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(65) NOT NULL,
	rol VARCHAR(65) NOT NULL,
    contrasenia VARCHAR(65) NULL,
	activo      INT NOT NULL DEFAULT 1,
    token longText 
    );

CREATE TABLE Empleado(
	cve_empleado INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cve_persona INT NOT NULL,
    cve_usuario INT NOT NULL,
	numero_empleado VARCHAR(7) NOT NULL UNIQUE,
    campus VARCHAR(45) NULL,
	edificio VARCHAR(45) NULL,
    activo      INT NOT NULL DEFAULT 1,
    CONSTRAINT cve_persona FOREIGN KEY (cve_persona) REFERENCES persona(cve_persona),
    CONSTRAINT cve_usuario FOREIGN KEY (cve_usuario) REFERENCES usuario(cve_usuario)
    
);

CREATE TABLE Campus(
	cve_campus INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(30) NOT NULL,
    activo INT NOT NULL
);

CREATE TABLE Edificio(
	cve_edificio INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cve_campus INT NOT NULL,
    nombre VARCHAR(65) NOT NULL,
    activo INT NOT NULL,
    CONSTRAINT cve_campus FOREIGN KEY (cve_campus) REFERENCES campus(cve_campus)
);

	CREATE TABLE Departamento(
	cve_departamento INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cve_edificio INT NOT NULL,
    nombre VARCHAR(65) NOT NULL,
    activo INT NOT NULL,
    CONSTRAINT cve_edificio FOREIGN KEY (cve_edificio) REFERENCES edificio(cve_edificio)
    );
    
CREATE TABLE Puesto(
	cve_puesto INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cve_departamento INT NOT NULL,
    cve_empleado INT NOT NULL,
    nombre VARCHAR(65) NOT NULL,
    activo INT NOT NULL,
    CONSTRAINT cve_departamento FOREIGN KEY (cve_departamento) REFERENCES departamento(cve_departamento),
    CONSTRAINT cve_empleado FOREIGN KEY (cve_empleado) REFERENCES empleado(cve_empleado)
    );
    
CREATE TABLE Equipo (
    cve_equipo INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(65) NOT NULL,
    edificio VARCHAR(65) NOT NULL,
    departamento VARCHAR(65) NOT NULL,
    marca VARCHAR(65) NOT NULL,
    direccion_ip VARCHAR(65) NOT NULL,
    direccion_mac VARCHAR(65) NOT NULL,
    modelo VARCHAR(65) NOT NULL,
    numero_serie VARCHAR(65) NOT NULL,
    numero_utl VARCHAR(65) NOT NULL,
    capacidad VARCHAR(65) NOT NULL, 
    ram VARCHAR(65) NOT NULL,
    cpu   VARCHAR(65)  NOT NULL,
    estado VARCHAR(30) NOT NULL,
    periocidad_mantenimiento VARCHAR(30) NOT NULL,
    descripcionEquipo VARCHAR(200) NOT NULL,
    activo INT NOT NULL
);
CREATE TABLE AsignacionEquipo (
    cve_asignacion INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cve_equipo INT NOT NULL,
    cve_empleado INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin DATETIME DEFAULT NULL, -- fecha en que se terminó la asignación
    activo INT NOT NULL DEFAULT 1, -- 1: activa, 0: inactiva
    FOREIGN KEY (cve_equipo) REFERENCES Equipo(cve_equipo),
    FOREIGN KEY (cve_empleado) REFERENCES Empleado(cve_empleado)
);

CREATE TABLE Baja (
    cve_baja INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cve_equipo INT NOT NULL,
    cve_empleado INT NOT NULL,
    fecha_baja DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(255) DEFAULT 'Fin de asignación',
    observaciones TEXT,
	activo INT NOT NULL DEFAULT 1, -- 1: activa, 0: inactiva
    FOREIGN KEY (cve_equipo) REFERENCES Equipo(cve_equipo),
    FOREIGN KEY (cve_empleado) REFERENCES Empleado(cve_empleado)
);
	





