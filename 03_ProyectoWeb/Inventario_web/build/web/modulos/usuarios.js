new Vue({
    el: "#modulo-usuarios",
    vuetify: new Vuetify(),
    setup() {
        const {
            ref,
            reactive,
            computed,
            onMounted,
        } = VueCompositionAPI;

        // 🧠 Variables reactivas
        const rolUsuarioActual = ref(localStorage.getItem("rol") || "");
        const numeroEmpleado = ref("");
        const nombre = ref("");
        const apellido1 = ref("");
        const apellido2 = ref("");
        const campus = ref("");
        const edificio = ref("");
        const correo = ref("");
        const contrasenia = ref("");
        const rol = ref("");
        const mostrarFormulario = ref(false);
        const dialog = ref(false);
        const flagEditar = ref(false);
        const idEditar = ref(null);
        const idEmpleado = ref(0);
        const idPersona = ref(0);
        const idUsuario = ref(0);
        const usuarioSeleccionado = ref({});
        const search = ref("");
        const showPassword = ref(false);

        const datosTabla = ref([]);

        const formUsuario = ref(null);
        const valid = ref(false);

        const rules = {
            required: v => !!v || "Este campo es obligatorio",
        };

        const headers = [
            {text: "Nº Empleado", value: "numeroEmpleado"},
            {text: "Nombre", value: "nombre", align: "center"},
            {text: "Campus", value: "campus"},

            {text: "Correo", value: "correo"},
            {text: "Rol", value: "rol"},
            {text: "Ver más", value: "ver_mas"},
            {text: "Editar", value: "editar", align: "center"},
            {text: "Eliminar", value: "eliminar"}
        ];

        const headersTabla = computed(() => {
            return rolUsuarioActual.value === "ADMINISTRADOR"
                    ? headers
                    : headers.filter(h => h.value !== "editar" && h.value !== "eliminar");
        });

        // ✅ Funciones

        function validarCampos() {
            const campos = [numeroEmpleado.value, nombre.value, apellido1.value, /* ... */];
            const camposValidos = campos.every(campo => rules.required(campo) === true);
            if (!camposValidos) {
                Swal.fire("Campos incompletos", "Por favor completa todos los campos obligatorios.", "warning");
                return false;
            }
            return true;
        }


        async function fnGuardarEnServidor() {
            if (rolUsuarioActual.value !== "ADMINISTRADOR") {
                Swal.fire("Acceso denegado", "Solo los administradores pueden guardar usuarios.", "error");
                return;
            }

           if (!validarCampos()) return;


            const token = localStorage.getItem("token");
            if (!token) {
                Swal.fire("Error", "No se encontró el token de autenticación.", "error");
                return;
            }

            normalizarCampos();

            const empleado = {
                id: idEmpleado.value || 0,
                numeroEmpleado: numeroEmpleado.value,
                campus: campus.value,
                edificio: edificio.value,
                persona: {
                    id: idPersona.value || 0,
                    nombre: nombre.value,
                    apellido1: apellido1.value,
                    apellido2: apellido2.value,
                    correo: correo.value
                },
                usuario: {
                    id: idUsuario.value || 0,
                    usuario: numeroEmpleado.value,
                    contrasenia: contrasenia.value,
                    rol: rol.value
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
                    fnLimpiar();
                    recargarTablaUsuarios();
                }

            } catch (error) {
                Swal.fire("Error de red", error.message, "error");
            }
        }

        async function recargarTablaUsuarios() {
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
                    datosTabla.value = datos.map(u => ({
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
        }

        function fnCargarDatosEditar(item) {
            idEmpleado.value = item.idEmpleado;
            idPersona.value = item.idPersona;
            idUsuario.value = item.idUsuario;

            numeroEmpleado.value = item.numeroEmpleado;
            nombre.value = item.nombre;
            apellido1.value = item.apellido1;
            apellido2.value = item.apellido2;
            campus.value = item.campus;
            edificio.value = item.edificio;
            correo.value = item.correo;
            contrasenia.value = item.contrasenia;
            rol.value = item.rol;

            flagEditar.value = true;
            mostrarFormulario.value = true;

            Swal.fire({
                icon: 'info',
                title: 'Editando usuario',
                text: `Estás editando al usuario: ${item.nombre || 'Sin nombre'}`,
                showConfirmButton: false,
                timer: 2000,
                position: 'center'
            });
        }

        function fnAlerta(item) {
            Swal.fire({
                title: "¿Deseas dar de baja al usuario?",
                text: item.nombre,
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar"
            }).then(result => {
                if (result.isConfirmed) {
                    fnEliminar(item);
                }
            });
        }

        async function fnEliminar(item) {
            if (rolUsuarioActual.value !== "ADMINISTRADOR") {
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
                    recargarTablaUsuarios();
                }

            } catch (error) {
                Swal.fire("Error", error.message, "error");
            }
        }

        function normalizarCampos() {
            numeroEmpleado.value = numeroEmpleado.value.trim().toUpperCase();
            nombre.value = nombre.value.trim().toUpperCase();
            apellido1.value = apellido1.value.trim().toUpperCase();
            apellido2.value = apellido2.value.trim().toUpperCase();
            campus.value = campus.value.trim().toUpperCase();
            edificio.value = edificio.value.trim().toUpperCase();
            correo.value = correo.value.trim().toUpperCase();
            contrasenia.value = contrasenia.value.trim();
            rol.value = rol.value.trim().toUpperCase();
        }

        function sanitizar(texto) {
            return texto.replace(/[();'"\-*%«»”“$=\/ ]/g, ' ');
        }

        function onBlurNombre() {
            nombre.value = sanitizar(nombre.value);
        }

        function fnLimpiar() {
            numeroEmpleado.value = "";
            nombre.value = "";
            apellido1.value = "";
            apellido2.value = "";
            campus.value = "";
            edificio.value = "";
            correo.value = "";
            contrasenia.value = "";
            rol.value = "";
            flagEditar.value = false;
            mostrarFormulario.value = false;
        }

        function verMas(item) {
            usuarioSeleccionado.value = {...item};
            dialog.value = true;
        }

        // 🔃 Inicialización
        onMounted(() => {
            recargarTablaUsuarios();
        });

        return {
            // Datos reactivos
            numeroEmpleado, nombre, apellido1, apellido2,
            campus, edificio, correo, contrasenia, rol,
            mostrarFormulario, dialog, flagEditar, search,
            showPassword, datosTabla, usuarioSeleccionado,
            headersTabla, rules, rolUsuarioActual, formUsuario, valid, rules,

            // Métodos
            fnGuardarEnServidor, fnCargarDatosEditar, fnEliminar, fnAlerta,
            fnLimpiar, normalizarCampos, onBlurNombre, verMas, sanitizar,
        };


    }
});
