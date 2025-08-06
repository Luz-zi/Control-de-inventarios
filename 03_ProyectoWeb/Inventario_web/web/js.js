new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    setup() {

        const {ref, onMounted, nextTick, watch} = VueCompositionAPI;


        // 🧠 Variables reactivas
        const cpu = ref("");
        const ram = ref("");
        const idEmpleadoEditar = ref(0);
        const descripcionEquipo = ref("");
        const capacidad = ref("");
        const ip = ref("");
        const mac = ref("");
        const nombreEquipo = ref("");
        const noSerie = ref("");
        const noUtl = ref("");
        const correo = ref("");
        const noEmp = ref("");
        const departamento = ref("");
        const edificio = ref("");
        const nombre = ref("");
        const marca = ref("");
        const modelo = ref("");
        const dialog = ref(false);
        const search = ref('');
        const mostrarFormulario = ref(false);
        const objetoSeleccionado = ref(null);
        const mantenimiento = ref("");
        const estado = ref("");

        const paso = ref(1); // ✅ Control del paso actual
        const idEquipoEditar = ref(0);

        const datosTabla = ref([]);
        const snackbar = ref(false);
        const snackbarMensaje = ref("");
        const snackbarColor = ref("success"); // "success", "error", "info", etc.
        const snackbarIcon = ref("mdi-check-circle"); // Íconos de Material Design

        const headers = ref([
            {text: "ID", value: "equipoId"},
            {text: "Responsable", value: "nombre"},
            {text: "Marca", value: "marca"},
            {text: "Modelo", value: "modelo"},
            {text: "Ver más", value: "ver_mas"},
            {text: "Editar", value: "editar"},
            {text: "Eliminar", value: "eliminar"}
        ]);
       watch(mac, (val) => {
    if (!val) return;

    // Eliminar caracteres no válidos y convertir a mayúsculas
    let limpio = val.replace(/[^0-9A-Fa-f]/g, "").toUpperCase();

    // Limitar a 12 caracteres
    if (limpio.length > 12) limpio = limpio.slice(0, 12);

    // Agrupar en pares de dos caracteres
    const grupos = limpio.match(/.{1,2}/g);

    // Actualiza la variable mac con el formato correcto
    mac.value = grupos ? grupos.join(":") : limpio;
});



        watch(ip, (val) => {
            if (!val)
                return;

            // Eliminar todo lo que no sea número
            let newVal = val.replace(/[^\d]/g, "");

            // Limitar a 12 caracteres (3 números x 4 bloques)
            if (newVal.length > 12)
                newVal = newVal.slice(0, 12);

            // Insertar puntos cada 3 números
            const grupos = newVal.match(/.{1,3}/g);
            ip.value = grupos ? grupos.join(".") : "";
        });

        const flagEditar = ref(false);
        const idEditar = ref("");

        onMounted(() => {
            obtenerEquipos();
            nextTick(() => {
                observarInputsYAplicarFiltro();
            });
        });
      

        function mostrarAlerta(mensaje, tipo = "success") {
            snackbarMensaje.value = mensaje;
            snackbarColor.value = tipo;
            snackbarIcon.value =
                    tipo === "success"
                    ? "mdi-check-circle"
                    : tipo === "error"
                    ? "mdi-alert-circle"
                    : "mdi-information";
            snackbar.value = true;
        }

        function observarInputsYAplicarFiltro() {
            const observer = new MutationObserver(() => {
                const inputs = document.querySelectorAll("input[type='text'], textarea");

                inputs.forEach(input => {
                    if (!input.dataset.filtrado) {
                        input.dataset.filtrado = "true";

                        // Evento input → convertir a mayúsculas y filtrar caracteres
                        input.addEventListener("input", e => {
                            let val = e.target.value;
                            const nuevoVal = val.toUpperCase().replace(/[^A-Z0-9ÁÉÍÓÚÑÜ .,]/g, "");
                            if (nuevoVal !== val) {
                                e.target.value = nuevoVal;
                                // No disparar event nuevo para evitar loop infinito
                            }
                        });

                        // Bloquea directamente caracteres no permitidos al escribir
                        input.addEventListener("keypress", e => {
                            const char = String.fromCharCode(e.which);
                            const permitido = /[A-Z0-9ÁÉÍÓÚÑÜ .,]/i;
                            if (!permitido.test(char)) {
                                e.preventDefault();
                            }
                        });
                    }
                });
            });

            // Observa todo el formulario
            observer.observe(document.getElementById("app"), {
                childList: true,
                subtree: true
            });
        }


        async function obtenerEquipos() {
            const url = "http://localhost:8080/inventario_web/api/equipo/getAll";
            try {
                const response = await fetch(url);
                if (!response.ok)
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                const data = await response.json();
                datosTabla.value = data.map(item => ({
                        id: item.id,
                        equipoId: item.equipo?.id,
                        nombre: `${item.empleado?.persona?.nombre} ${item.empleado?.persona?.apellido1} ${item.empleado?.persona?.apellido2}`,
                        noEmp: item.empleado?.numeroEmpleado,
                        telefono: item.empleado?.persona?.telefono,
                        correo: item.empleado?.persona?.correo,
                        nombreEquipo: item.equipo?.nombre,
                        edificio: item.equipo?.edificio,
                        departamento: item.departamento?.departamento,
                        marca: item.equipo?.marca,
                        modelo: item.equipo?.modelo,
                        ip: item.equipo?.ip,
                        mac: item.equipo?.mac,
                        noSerie: item.equipo?.numSerie,
                        noUtl: item.equipo?.numUtl,
                        capacidad: item.equipo?.capacidad,
                        ram: item.equipo?.ram,
                        cpu: item.equipo?.cpu,
                        estado: item.equipo?.estado,
                        mantenimiento: item.equipo?.mantenimiento,
                        descripcionEquipo: item.equipo?.descripcionEquipo
                    }));

            } catch (error) {
                console.error("🚨 Error:", error);
                alert("Ocurrió un error al conectar con el backend.");
            }
        }

        async function fnGuardar() {
            // Buscar el empleado
            await buscarEmpleadoPorNumero();

            if (!sanitizarYValidarCampos())
                return;

            if (idEmpleadoEditar.value === 0) {
                Swal.fire({
                    icon: 'error',
                    title: '❌ No se encontró al empleado',
                    text: 'Verifica el número ingresado.',
                    timer: 2500,
                    showConfirmButton: false,
                    position: 'center',
                });
                return;
            }

            const payload = {
                id: 0,
                FechaAsignacion: new Date().toISOString().split("T")[0],
                equipo: {
                    id: 0,
                    nombre: nombreEquipo.value,
                    edificio: edificio.value,
                    departamento: departamento.value,
                    marca: marca.value,
                    modelo: modelo.value,
                    ip: ip.value,
                    mac: mac.value,
                    numSerie: noSerie.value,
                    numUtl: noUtl.value,
                    capacidad: capacidad.value,
                    ram: ram.value,
                    cpu: cpu.value,
                    estado: estado.value || "En uso",
                    mantenimiento: mantenimiento.value,
                    descripcionEquipo: descripcionEquipo.value,
                    activo: 1
                },
                empleado: {
                    id: idEmpleadoEditar.value,
                    numeroEmpleado: noEmp.value,
                    persona: {
                        id: 0,
                        nombre: nombre.value,
                        apellido1: "",
                        apellido2: "",
                        telefono: "",
                        correo: correo.value
                    },
                    activo: 1
                },
                activo: 1
            };

            const params = new URLSearchParams();
            params.append("datosEquipo", JSON.stringify(payload));

            try {
                const response = await fetch("http://localhost:8080/inventario_web/api/equipo/save", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                    body: params.toString()
                });

                const data = await response.json();
                console.log("✅ Respuesta del backend:", data);

                // Aceptar cualquier respuesta válida como éxito
                if (response.ok && data) {
                    // Opcional: agregar el nuevo registro a la tabla si lo necesitas
                    datosTabla.value.push({
                        id: data.id || 0,
                        nombre: nombre.value,
                        noEmp: noEmp.value,
                        correo: correo.value,
                        nombreEquipo: nombreEquipo.value,
                        edificio: edificio.value,
                        departamento: departamento.value,
                        marca: marca.value,
                        modelo: modelo.value,
                        ip: ip.value,
                        mac: mac.value,
                        noSerie: noSerie.value,
                        noUtl: noUtl.value,
                        capacidad: capacidad.value,
                        ram: ram.value,
                        cpu: cpu.value,
                        estado: estado.value,
                        mantenimiento: mantenimiento.value,
                        descripcionEquipo: descripcionEquipo.value
                    });

                    Swal.fire({
                        icon: 'success',
                        title: '¡Registro guardado con éxito!',
                        showConfirmButton: false,
                        timer: 2000,
                        position: 'center',
                    });

                    fnLimpiar();
                    mostrarFormulario.value = false;
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'No se guardó correctamente.',
                        timer: 2500,
                        showConfirmButton: false,
                        position: 'center',
                    });
                }

            } catch (e) {
                console.error("🚨 Error al guardar:", e);
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: e.message,
                    timer: 3000,
                    showConfirmButton: false,
                    position: 'center'
                });
            }
        }

        function fnCargarDatosEditar(item) {
            idEditar.value = item.id;
            idEquipoEditar.value = item.equipoId;
            idEmpleadoEditar.value = item.empleadoId || 0;
            nombre.value = item.nombre;
            edificio.value = item.edificio;
            departamento.value = item.departamento;
            marca.value = item.marca;
            modelo.value = item.modelo;
            ip.value = item.ip;
            mac.value = item.mac;
            noSerie.value = item.noSerie;
            noUtl.value = item.noUtl;
            capacidad.value = item.capacidad;
            ram.value = item.ram;
            cpu.value = item.cpu;
            estado.value = item.estado;
            descripcionEquipo.value = item.descripcionEquipo;
            noEmp.value = item.noEmp;
            correo.value = item.correo;
            flagEditar.value = true;
            mostrarFormulario.value = true;
            paso.value = 1;

            // ✅ Mostrar alerta estilo imagen
            Swal.fire({
                icon: 'info',
                title: 'Editando equipo',
                text: `Estás editando el equipo: ${item.nombreEquipo || 'Sin nombre'}`,
                showConfirmButton: false,
                timer: 2000,
                position: 'center'
            });
        }


        function fnNuevo() {
            fnLimpiar();
            mostrarFormulario.value = true;
            paso.value = 1; // ✅ Iniciar en el paso 1
        }

        async function fnEditar() {
            if (!sanitizarYValidarCampos())
                return;

            const url = "http://localhost:8080/inventario_web/api/equipo/save";

            const payload = {
                id: idEditar.value,
                FechaAsignacion: new Date().toISOString().split("T")[0],
                equipo: {
                    id: idEquipoEditar.value,
                    nombre: nombreEquipo.value,
                    edificio: edificio.value,
                    departamento: departamento.value,
                    marca: marca.value,
                    modelo: modelo.value,
                    ip: ip.value,
                    mac: mac.value,
                    numSerie: noSerie.value,
                    numUtl: noUtl.value,
                    capacidad: capacidad.value,
                    ram: ram.value,
                    cpu: cpu.value,
                    estado: estado.value || "En uso",
                    mantenimiento: mantenimiento.value,
                    descripcionEquipo: descripcionEquipo.value,
                    activo: 1
                },
                empleado: {
                    id: idEmpleadoEditar.value,
                    numeroEmpleado: noEmp.value,
                    persona: {
                        id: 0,
                        nombre: nombre.value,
                        apellido1: "",
                        apellido2: "",
                        telefono: "",
                        correo: correo.value
                    },
                    activo: 1
                },
                activo: 1
            };

            try {
                const params = new URLSearchParams();
                params.append("datosEquipo", JSON.stringify(payload));

                const response = await fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                    },
                    body: params.toString()
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    Swal.fire({
                        icon: 'error',
                        title: 'Error al editar',
                        text: errorText,
                        showConfirmButton: false,
                        timer: 2500,
                        position: 'center'
                    });
                    return;
                }

                const data = await response.json();

                // Actualizar datos en la tabla
                const index = datosTabla.value.findIndex(item => item.id === idEditar.value);
                if (index !== -1) {
                    datosTabla.value[index] = {
                        id: data.id,
                        nombre: `${data.empleado?.persona?.nombre} ${data.empleado?.persona?.apellido1} ${data.empleado?.persona?.apellido2}`,
                        noEmp: data.empleado?.numeroEmpleado,
                        telefono: data.empleado?.persona?.telefono,
                        correo: data.empleado?.persona?.correo,
                        nombreEquipo: data.equipo?.nombre,
                        edificio: data.equipo?.edificio,
                        departamento: data.equipo?.departamento,
                        marca: data.equipo?.marca,
                        modelo: data.equipo?.modelo,
                        ip: data.equipo?.ip,
                        mac: data.equipo?.mac,
                        noSerie: data.equipo?.numSerie,
                        noUtl: data.equipo?.numUtl,
                        capacidad: data.equipo?.capacidad,
                        ram: data.equipo?.ram,
                        cpu: data.equipo?.cpu,
                        estado: data.equipo?.estado,
                        mantenimiento: data.equipo?.mantenimiento,
                        descripcionEquipo: data.equipo?.descripcionEquipo
                    };
                }

                fnLimpiar();
                mostrarFormulario.value = false;

                // ✅ Mostrar alerta SweetAlert2 estilo imagen
                Swal.fire({
                    icon: 'success',
                    title: 'Equipo actualizado',
                    text: 'Los datos fueron modificados correctamente.',
                    showConfirmButton: false,
                    timer: 2000,
                    position: 'center'
                });

            } catch (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexión',
                    text: error.message,
                    showConfirmButton: false,
                    timer: 2500,
                    position: 'center'
                });
            }
        }

        function sanitizarYValidarCampos() {
            // Aquí puedes agregar validaciones reales después
            return true;
        }

        function fnEliminar(item) {
            const i = datosTabla.value.findIndex(x => x.id == item.id);
            datosTabla.value.splice(i, 1);
        }


        function fnLimpiar() {
            nombre.value = "";
            marca.value = "";
            modelo.value = "";
            flagEditar.value = false;
            idEquipoEditar.value = 0;
            idEmpleadoEditar.value = 0;
        }

        function fnVerMas(item) {
            objetoSeleccionado.value = item;
            dialog.value = true;
        }
        async function buscarEmpleadoPorNumero() {
            if (!noEmp.value)
                return;

            const url = `http://localhost:8080/inventario_web/api/empleado?numeroEmpleado=${noEmp.value}`;

            try {
                const response = await fetch(url);
                if (!response.ok)
                    throw new Error("No se pudo obtener el empleado");

                const data = await response.json();

                if (data && data.persona) {
                    idEmpleadoEditar.value = data.id;  // <-- Aquí guardas el ID del empleado correctamente
                    nombre.value = `${data.persona.nombre} ${data.persona.apellido1} ${data.persona.apellido2}`;
                    correo.value = data.persona.correo;
                } else {
                    idEmpleadoEditar.value = 0; // Limpia si no encuentra
                    nombre.value = "";
                    alert("Empleado no encontrado.");
                }
            } catch (error) {
                console.error("❌ Error al buscar el empleado:", error);
                alert("Error al obtener los datos del empleado.");
            }
        }


        return {
            nombre, marca, modelo, datosTabla, fnGuardar, fnCargarDatosEditar, headers, flagEditar,
            fnEditar, fnEliminar, dialog, search, mostrarFormulario, fnNuevo, objetoSeleccionado,
            fnVerMas, edificio, departamento, noEmp, correo, noSerie, noUtl, nombreEquipo, capacidad, mac,
            ip, estado, descripcionEquipo, ram, cpu, paso, buscarEmpleadoPorNumero, mantenimiento, snackbar,
            snackbarMensaje,
            snackbarColor,
            snackbarIcon,
            mostrarAlerta
        };
    }
});