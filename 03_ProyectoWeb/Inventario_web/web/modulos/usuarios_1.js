new Vue({
    el: "#modulo-usuarios",
    vuetify: new Vuetify(),
    data() {
        return {
            rolUsuarioActual: "", // 👈 rol del usuario actual
            rol: "", // 👈 solo para el formulario
            numeroEmpleado: "",
            nombre: "",
            apellido1: "",
            apellido2: "",
            campus: "",
            edificio: "",
            correo: "",
            contrasenia: "",
            descripcion: "",
            mostrarFormulario: false,
            dialog: false,
            flagEditar: false,
            idEditar: null,
            search: "",
            showPassword: false,
            rules: {
                required: v => !!v || 'Este campo es obligatorio',
            },
            usuarioSeleccionado: {},
            datosTabla: [],
            headers: [
                {text: "Nº Empleado", value: "numeroEmpleado"},
                {text: "Nombre", value: "nombre", align: "center"},
                {text: "Campus", value: "campus"},
                {text: "Edificio", value: "edificio"},
                {text: "Correo", value: "correo"},
                {text: "Rol", value: "rol"},
                {text: "Ver más", value: "ver_mas"},
                {text: "Editar", value: "editar", align: "center"},
                {text: "Eliminar", value: "eliminar"}
            ]
        };
    },
    computed: {
        headersTabla() {
            if (this.rolUsuarioActual === "ADMINISTRADOR") {
                return this.headers; // mostrar todas las columnas
            } else {
                // ocultar columnas editar y eliminar para otros roles
                return this.headers.filter(
                        h => h.value !== "editar" && h.value !== "eliminar"
                );
            }
        }
    },
    mounted() {
        this.rolUsuarioActual = localStorage.getItem("rol") || "";
        this.recargarTablaUsuarios();
    },
    methods: {
        async fnGuardarEnServidor() {
            if (this.rolUsuarioActual !== "ADMINISTRADOR") {
                Swal.fire("Acceso denegado", "Solo los administradores pueden guardar usuarios.", "error");
                return;
            }

            const token = localStorage.getItem("token");
            if (!token) {
                Swal.fire("Error", "No se encontró el token de autenticación.", "error");
                return;
            }

            this.normalizarCampos();
            const empleado = {
                id: this.idEmpleado || 0,
                numeroEmpleado: this.numeroEmpleado,
                campus: this.campus,
                edificio: this.edificio,
                persona: {
                    id: this.idPersona || 0,
                    nombre: this.nombre,
                    apellido1: this.apellido1,
                    apellido2: this.apellido2,
                    correo: this.correo
                },
                usuario: {
                    id: this.idUsuario || 0,
                    usuario: this.numeroEmpleado,
                    contrasenia: this.contrasenia,
                    rol: this.rol
                }
            };

            try {
                const resp = await fetch("api/usuario/save", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                    body: new URLSearchParams({datosUsuario: JSON.stringify(empleado), token})
                });
                const data = await resp.json();
                if (data.error) {
                    Swal.fire("Error", data.error, "error");
                } else {
                    Swal.fire("Éxito", "Usuario guardado con éxito", "success");
                    this.fnLimpiar();
                    this.recargarTablaUsuarios();
                }
            } catch (error) {
                Swal.fire("Error de red", error.message, "error");
            }
        },

        async recargarTablaUsuarios() {
            const token = localStorage.getItem("token");
            if (!token) {
                Swal.fire("Error", "No se encontró el token de autenticación.", "error");
                return;
            }

            try {
                const resp = await fetch(`api/usuario/getAll?token=${encodeURIComponent(token)}`);
                const datos = await resp.json();
                if (datos.error) {
                    Swal.fire("Error", datos.error, "error");
                } else {
                    this.datosTabla = datos.map(u => ({
                            numeroEmpleado: u.numeroEmpleado ?? "",
                            nombre: u.persona?.nombre ?? "Sin nombre",
                            apellido1: u.persona?.apellido1 ?? "",
                            apellido2: u.persona?.apellido2 ?? "",
                            correo: u.persona?.correo ?? "",
                            contrasenia: u.usuario?.contrasenia ?? "",
                            campus: u.campus ?? "",
                            edificio: u.edificio ?? "",
                            rol: u.usuario?.rol ?? "",
                            idEmpleado: u.id ?? 0,
                            idPersona: u.persona?.id ?? 0,
                            idUsuario: u.usuario?.id ?? 0
                        }));
                }
            } catch (error) {
                Swal.fire("Error", error.message, "error");
            }
        },

        fnCargarDatosEditar(item) {
            this.idEmpleado = item.idEmpleado;
            Object.assign(this, item);
            this.flagEditar = true;
            this.mostrarFormulario = true;
        },

        fnAlerta(item) {
            Swal.fire({
                title: "¿Deseas dar de baja al usuario?",
                text: item.nombre,
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar"
            }).then(result => {
                if (result.isConfirmed) {
                    this.fnEliminar(item);
                }
            });
        },

        async fnEliminar(item) {
            if (this.rolUsuarioActual !== "ADMINISTRADOR") {
                Swal.fire("Acceso denegado", "Solo los administradores pueden eliminar usuarios.", "error");
                return;
            }

            const token = localStorage.getItem("token");
            if (!token) {
                Swal.fire("Error", "No se encontró el token de autenticación.", "error");
                return;
            }

            try {
                const resp = await fetch("api/usuario/delete", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                    body: new URLSearchParams({cve_usuario: item.idUsuario, token})
                });
                const data = await resp.json();
                if (data.error) {
                    Swal.fire("Error", data.error, "error");
                } else {
                    Swal.fire("Eliminado", "Usuario eliminado con éxito", "success");
                    this.recargarTablaUsuarios();
                }
            } catch (error) {
                Swal.fire("Error", error.message, "error");
            }
        },

        normalizarCampos() {
            this.numeroEmpleado = this.numeroEmpleado.trim().toUpperCase();
            this.nombre = this.nombre.trim().toUpperCase();
            this.apellido1 = this.apellido1.trim().toUpperCase();
            this.apellido2 = this.apellido2.trim().toUpperCase();
            this.campus = this.campus.trim().toUpperCase();
            this.edificio = this.edificio.trim().toUpperCase();
            this.correo = this.correo.trim().toUpperCase();
            this.contrasenia = this.contrasenia.trim();
            this.rol = this.rol.trim().toUpperCase();
        },

        sanitizar(texto) {
            return texto.replace(/[();'"\-*%«»”“$=\/ ]/g, ' ');
        },

        onBlurNombre() {
            this.nombre = this.sanitizar(this.nombre);
        },

        fnLimpiar() {
            this.numeroEmpleado = "";
            this.nombre = "";
            this.apellido1 = "";
            this.apellido2 = "";
            this.campus = "";
            this.edificio = "";
            this.correo = "";
            this.contrasenia = "";
            this.rol = "";
            this.flagEditar = false;
            this.mostrarFormulario = false;
        },

        verMas(item) {
            this.usuarioSeleccionado = {...item};
            this.dialog = true;
        }
    }
});
