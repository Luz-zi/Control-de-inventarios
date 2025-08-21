new Vue({
    el: "#modulo-bajas",
    vuetify: new Vuetify(),
    setup() {
        const {
            ref,
            reactive,
            computed,
            onMounted,
        } = VueCompositionAPI;
        // 🧠 Variables reactivas
        const rolUsuarioActual = ref("ADMINISTRADOR");
        const empleadoId = ref("");
        const equipoId = ref("");
        const numUtl = ref("");
        const nombreEquipo = ref("");
        const marca = ref("");
        const modelo = ref("");
        const usuario = ref("");
        const descripcion = ref("");
        const fechaBaja = ref("");
        const asignar = ref("");
        const menuFecha = ref(false);
        const mostrarFormulario = ref(false);
        const dialog = ref(false);
        const dialogBuscar = ref(false);
        const flagEditar = ref(false);
        const idEditar = ref(null);
        const numeroBuscar = ref("");
        const usuarioSeleccionado = ref({});

        const datosTabla = ref([]);
        const empleados = ref([
            {numero: "1001", nombreEquipo: "Juan Pérez", usuario: "Jefe de RH", marca: "marca central", modelo: "Recursos Humanos"},
            {numero: "1002", nombreEquipo: "Ana García", usuario: "Coordinadora", marca: "marca 2", modelo: "Psicopedagogía"}
        ]);
        const search = ref("");
        const headers = [
            {text: "No.Utl", value: "numUtl"},
            {text: "Nombre de equipo", value: "nombreEquipo"},
            {text: "Marca", value: "marca"},
            {text: "Modelo", value: "modelo"},
            {text: "Último usuario", value: "usuario"},
            {text: "Fecha de baja", value: "fechaBaja"},
            {text: "Ver más", value: "ver_mas"},
            {text: "Editar", value: "editar"},
            {text: "Asignar usuario", value: "asignar"}
        ];
        // ✅ Funciones

        async function fnGuardar() {


            const baja = {
                id: 0,
                equipo: {
                    id: equipoId.value

                },
                empleado: {
                    id: empleadoId.value

                },
                motivo: descripcion.value,
                observaciones: ""
            };

            const token = localStorage.getItem('token') || '';
            if (!token) {
                Swal.fire("Error", "Falta el token de autenticación.", "error");
                return;
            }

            try {
                const response = await fetch("api/baja/save", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                    },
                    body: new URLSearchParams({
                        datosBaja: JSON.stringify(baja), // ✅ nombreEquipo correcto
                        token
                    })
                });

                const data = await response.json();

                if (data.error) {
                    Swal.fire("Error", data.error, "error");
                } else {
                    Swal.fire("¡Guardado!", "Equipo dado de baja.", "success");

                    datosTabla.value.push({
                        id: data.id || (datosTabla.value.length + 1),
                        numUtl: numUtl.value,
                        nombreEquipo: nombreEquipo.value,
                        marca: marca.value,
                        modelo: modelo.value,
                        usuario: usuario.value,
                        fechaBaja: fechaBaja.value,
                        descripcion: descripcion.value,
                        estatus: true
                    });

                    fnLimpiar();
                    mostrarFormulario.value = false;
                    await recargarTablaBajas();

                }
            } catch (err) {
                console.error("Error en fnGuardar:", err);
                Swal.fire("Error", "Error al conectar con el servidor.", "error");
            }
        }

        async function recargarTablaBajas() {
            const token = localStorage.getItem("token");
            if (!token) {
                Swal.fire("Error", "No se encontró el token de autenticación.", "error");
                return;
            }

            try {
                const resp = await fetch(`api/baja/getAll?token=${encodeURIComponent(token)}`);
                const datos = await resp.json();

                if (datos.error) {
                    Swal.fire("Error", datos.error, "error");
                } else {
                    datosTabla.value = datos.map(b => {
                        console.log("✅ Objeto baja recibido:", b); // <--- Agrega esto

// Si b.fechaBaja existe, convertirla a formato YYYY-MM-DD
                        const fechaFormateada = b.fechaBaja
                                ? new Date(b.fechaBaja).toISOString().split("T")[0]
                                : "";

                        return {
                            numUtl: b.equipo?.numUtl || "Sin número",
                            nombreEquipo: b.equipo?.nombre || "Sin nombre",
                            marca: b.equipo?.marca || b.marca || "Sin marca",
                            modelo: b.equipo?.modelo || b.modelo || "Sin modelo",
                            usuario: `${b.empleado?.persona?.nombre ?? ''} ${b.empleado?.persona?.apellido1 ?? ''}`,
                            fechaBaja: fechaFormateada,
                            descripcion: b.motivo ?? ""
                        };
                    });

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
            nombreEquipo.value = item.nombreEquipo;
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
                text: `Estás editando al usuario: ${item.nombreEquipo || 'Sin nombreEquipo'}`,
                showConfirmButton: false,
                timer: 2000,
                position: 'center'
            });
        }

        function fnAlerta(item) {
            Swal.fire({
                title: "¿Deseas dar de baja al usuario?",
                text: item.nombreEquipo,
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

        function fnCargarDatosEditar(item) {
            flagEditar.value = true;
            idEditar.value = item.id;
            nombreEquipo.value = item.nombreEquipo;
            marca.value = item.marca;
            modelo.value = item.modelo;
            usuario.value = item.usuario;
            fechaBaja.value = item.fechaBaja;
            descripcion.value = item.descripcion;
            mostrarFormulario.value = true;
        }

        function fnEditar() {
            const idx = datosTabla.value.findIndex(x => x.id === idEditar.value);
            if (idx >= 0) {
                Object.assign(datosTabla.value[idx], {
                    nombreEquipo: nombreEquipo.value,
                    marca: marca.value,
                    modelo: modelo.value,
                    usuario: usuario.value,
                    fechaBaja: fechaBaja.value,
                    descripcion: descripcion.value
                });
            }
            fnLimpiar();
            Swal.fire("¡Editado!", "Datos actualizados.", "success");
            mostrarFormulario.value = false;

        }

        async function fnAsignarUsuario(item) {
            // 1️⃣ Mostrar alerta con input numérico
            const {value: numeroEmpleado} = await Swal.fire({
                title: "Asignar usuario",
                input: "number",
                inputLabel: "Número de empleado",
                inputPlaceholder: "Ingresa el número del empleado",
                inputAttributes: {
                    min: 1
                },
                showCancelButton: true,
                confirmButtonText: "Buscar empleado",
                cancelButtonText: "Cancelar"
            });

            if (!numeroEmpleado)
                return;

            try {
                // 2️⃣ Buscar al empleado en el backend
                const empleadoResp = await fetch(`http://localhost:8080/inventario_web/api/empleado?numeroEmpleado=${numeroEmpleado}`);
                if (!empleadoResp.ok)
                    throw new Error("No se pudo obtener el empleado");

                const empleadoData = await empleadoResp.json();

                if (empleadoData && empleadoData.persona) {
                    const nombreCompleto = `${empleadoData.persona.nombre} ${empleadoData.persona.apellido1} ${empleadoData.persona.apellido2}`;
                    const empleadoId = empleadoData.id;
                    const equipoId = empleadoData.id;


                    // 3️⃣ Crear objeto de asignación (formato requerido por tu backend)
                    const asignacion = {
                        id: 0,
                        equipo: {id: equipoId},
                        empleado: {id: empleadoId},
                        fechaAsignacion: null,
                        fechaFin: null,
                        activo: 1
                    };

                    // 4️⃣ Obtener el token
                    const token = localStorage.getItem('token') || '';
                    if (!token) {
                        Swal.fire("Error", "Falta el token de autenticación.", "error");
                        return;
                    }

                    // 5️⃣ Enviar la asignación al backend
                    const saveResp = await fetch("http://localhost:8080/inventario_web/api/asignacion/save", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                        },
                        body: new URLSearchParams({
                            datosAsignacion: JSON.stringify(asignacion),
                            token: token
                        })
                    });

                    const saveData = await saveResp.json();

                    // 6️⃣ Mostrar resultado
                    if (saveData.error) {
                        Swal.fire("Error", saveData.error, "error");
                    } else {


                        Swal.fire({
                            icon: "success",
                            title: "Empleado asignado",
                            text: `El equipo fue asignado a: ${nombreCompleto}`,
                            timer: 2000,
                            showConfirmButton: false
                        });

                        recargarTablaBajas();
                    }

                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Empleado no encontrado",
                        text: "El número ingresado no corresponde a ningún empleado"
                    });
                }

            } catch (error) {
                console.error("❌ Error en la asignación:", error);
                Swal.fire({
                    icon: "error",
                    title: "Error al asignar",
                    text: "Ocurrió un error al conectar con el servidor"
                });
            }
        }



        function fnAlerta(item) {
            Swal.fire({
                title: "¿Dar de baja este registro?",
                text: item.nombreEquipo,
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

        function fnEliminar(item) {
            datosTabla.value = datosTabla.value.filter(x => x.id !== item.id);
            Swal.fire("Eliminado", "Registro eliminado.", "success");
        }

        function verMas(item) {
            usuarioSeleccionado.value = {...item};
            dialog.value = true;
        }

        function fnBuscarEmpleado() {
            const e = empleados.value.find(x => x.numero === numeroBuscar.value);
            if (e) {
                usuario.value = e.nombre;
                dialogBuscar.value = false;
                Swal.fire("Empleado encontrado", "", "success");
            } else {
                Swal.fire("No encontrado", "Número incorrecto", "error");
            }
        }

        function fnLimpiar() {
            nombreEquipo.value = "";
            marca.value = "";
            modelo.value = "";
            usuario.value = "";
            descripcion.value = "";
            fechaBaja.value = "";
            numeroBuscar.value = "";
            flagEditar.value = false;
            idEditar.value = null;
        }


        onMounted(() => {
            recargarTablaBajas();
            const equipoBaja = localStorage.getItem('equipoParaBaja');
            if (equipoBaja) {
                const equipo = JSON.parse(equipoBaja);
                empleadoId.value = equipo.empleadoId || '';
                equipoId.value = equipo.equipoId || '';
                numUtl.value = numUtl.numUtl || '';
                nombreEquipo.value = equipo.nombreEquipoEquipo || equipo.nombreEquipo || '';
                marca.value = equipo.marca || '';
                modelo.value = equipo.modelo || '';
                usuario.value = equipo.nombre || '';
                mostrarFormulario.value = true;

                localStorage.removeItem('equipoParaBaja'); // <-- AÑADE ESTO

            }

            // 👂 También puedes agregar un listener para mostrar el formulario si viene desde Inventario
            window.addEventListener("mostrarFormulario", () => {
                const equipoBaja = localStorage.getItem('equipoParaBaja');
                if (equipoBaja) {
                    const equipo = JSON.parse(equipoBaja);
                    console.log("📦 JSON recibido para baja:", equipo);

                    nombreEquipo.value = equipo.nombreEquipo || '';
                    marca.value = equipo.marca || '';
                    modelo.value = equipo.modelo || '';
                    usuario.value = equipo.nombre || '';
                    mostrarFormulario.value = true;

                    // Limpiar localStorage
                    // localStorage.removeItem('equipoParaBaja');
                }
            });
        });

        return {
            // Datos reactivos
            nombreEquipo, marca, modelo, usuario, descripcion, fechaBaja, menuFecha,
            mostrarFormulario, dialog, dialogBuscar, flagEditar, idEditar, numeroBuscar, numUtl,
            usuarioSeleccionado, datosTabla, empleados, search, headers, rolUsuarioActual, empleadoId, asignar,
            // Métodos
            fnGuardar, fnCargarDatosEditar, fnEditar, fnAlerta, fnEliminar,
            verMas, fnBuscarEmpleado, fnLimpiar, fnAsignarUsuario
        };
    }
});
