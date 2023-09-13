const ODONTOLOGO = "Odontologo";
const PACIENTE = "Paciente";
const TURNO = "Turno";

let entidadActual = ODONTOLOGO;

window.addEventListener("load", () => {
    const url = "http://localhost:8081";

  /* -------------------------------------------------------------------------- */
  /*                 Listener de botones de opciones sobre entidades            */
  /* -------------------------------------------------------------------------- */
  const botonListar = document.querySelector("#listar-todos-button");

  botonListar.addEventListener("click", function(){
      limpiarTablaListarEntidadActual();
      ocultarDivs();
      mostrarDivListarEntidadActual();
      listarEntidadActual();
  });

  const botonConsultarPorId = document.querySelector("#consultar-id-button");

    botonConsultarPorId.addEventListener("click", function(){
        resetFormConsultarPorIdEntidadActual();
        limpiarTablaConsultarPorIdEntidadActual();
        ocultarDivs();
        mostrarDivConsultarIdEntidadActual();
    });

    const botonRegistrar = document.querySelector("#registrar-button");

    botonRegistrar.addEventListener("click", function(){
        resetFormRegistrarEntidadActual();
        ocultarDivs();
        mostrarDivRegistrarEntidadActual();
    });

    const botonActualizar = document.querySelector("#actualizar-button");

    botonActualizar.addEventListener("click", function(){
        resetFormActualizarEntidadActual();
        ocultarDivs();
        mostrarDivActualizarEntidadActual();
    });

    const botonEliminar = document.querySelector("#eliminar-button");

    botonEliminar.addEventListener("click", function(){
        resetFormEliminarEntidadActual();
        ocultarDivs();
        mostrarDivEliminarEntidadActual();
    });

    const botonOdontologoEntidad = document.querySelector("#odontologo-entidad");

    botonOdontologoEntidad.addEventListener("click", function(){
        entidadActual = ODONTOLOGO;
        document.querySelector("#entidad-actual-label").innerHTML = entidadActual;
        limpiarTablaListarEntidadActual();
        ocultarDivs();
        mostrarDivListarEntidadActual();
        listarEntidadActual();
    });

    const botonPacienteEntidad = document.querySelector("#paciente-entidad");

    botonPacienteEntidad.addEventListener("click", function(){
        entidadActual = PACIENTE;
        document.querySelector("#entidad-actual-label").innerHTML = entidadActual;
        limpiarTablaListarEntidadActual();
        ocultarDivs();
        mostrarDivListarEntidadActual();
        listarEntidadActual();
    });

    const botonTurnoEntidad = document.querySelector("#turno-entidad");

    botonTurnoEntidad.addEventListener("click", function(){
        entidadActual = TURNO;
        document.querySelector("#entidad-actual-label").innerHTML = entidadActual;
        limpiarTablaListarEntidadActual();
        ocultarDivs();
        mostrarDivListarEntidadActual();
        listarEntidadActual();
    });

  /* -------------------------------------------------------------------------- */
  /*                 Seccion forms CRUD de Odontologo                           */
  /* -------------------------------------------------------------------------- */

    const formConsultarOdontologoPorId = document.querySelector("#odontologo-form-consultar-id");

    formConsultarOdontologoPorId.addEventListener("submit", function(event){
        event.preventDefault();
        let id = Math.trunc(document.getElementById("odontologo-form-consultar-id-id").value);

        const tablaOdontologos = document.querySelector("#odontologo-tabla-id");
        let cantidadFilas = tablaOdontologos.rows.length;
        while(cantidadFilas > 0){
            tablaOdontologos.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
        
        let hayError = false;

        const requestOptions = {
            method: "GET"
        }
    
        fetch(`${url}/odontologos/${id}`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                if(!hayError){
                    renderizarOdontologoPorId(data);
                }
                else{
                    if(data.hasOwnProperty("message")){
                        alert(data.message);
                    }
                    else{
                        alert("No se pudo generar resultado para el ID:" + id);
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formRegistrarOdontologo = document.querySelector("#odontologo-form-registrar");

    formRegistrarOdontologo.addEventListener("submit", function(event){
        event.preventDefault();

        let numeroMatricula = document.getElementById("odontologo-form-registrar-matricula").value;
        let nombre = document.getElementById("odontologo-form-registrar-nombre").value;
        let apellido = document.getElementById("odontologo-form-registrar-apellido").value;

        let elHeader = new Headers();
        elHeader.append("Content-Type", "application/json");

        let payLoad = JSON.stringify({
            "matricula": numeroMatricula,
            "nombre": nombre,
            "apellido": apellido
        });

        const requestOptions = {
            method: "POST",
            headers:elHeader,
            body: payLoad
        }
        
        let hayError = false;
        fetch(`${url}/odontologos/registrar`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#odontologo-span-registrar");
                if(!hayError){
                    spanResultado.innerHTML = "Se ha creado el Odontologo con ID:" + data.id;
                    document.querySelector("#odontologo-form-registrar").reset();
                }
                else{
                    if(data.hasOwnProperty("message")){
                        spanResultado.innerHTML = data.message;
                    }
                    else{
                        spanResultado.innerHTML = "Se produjo un error que no permitió hacer el registro";
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formActualizarOdontologo = document.querySelector("#odontologo-form-actualizar");

    formActualizarOdontologo.addEventListener("submit", function(event){
        event.preventDefault();

        let id = document.getElementById("odontologo-form-actualizar-id").value;
        let numeroMatricula = document.getElementById("odontologo-form-actualizar-matricula").value;
        let nombre = document.getElementById("odontologo-form-actualizar-nombre").value;
        let apellido = document.getElementById("odontologo-form-actualizar-apellido").value;

        let elHeader = new Headers();
        elHeader.append("Content-Type", "application/json");

        let payLoad = JSON.stringify({
            "id": id,
            "matricula": numeroMatricula,
            "nombre": nombre,
            "apellido": apellido
        });

        const requestOptions = {
            method: "PUT",
            headers:elHeader,
            body: payLoad
        }
        
        let hayError = false;
        fetch(`${url}/odontologos/actualizar`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#odontologo-span-actualizar");
                if(!hayError){
                    spanResultado.innerHTML = "Se ha modificado el Odontologo con ID:" + data.id;
                    document.querySelector("#odontologo-form-actualizar").reset();
                }
                else{
                    if(data.hasOwnProperty("message")){
                        spanResultado.innerHTML = data.message;
                    }
                    else{
                        spanResultado.innerHTML = "Se produjo un error que no permitió hacer la actualización";
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formEliminarOdontologo = document.querySelector("#odontologo-form-eliminar");

    formEliminarOdontologo.addEventListener("submit", function(event){
        event.preventDefault();

        let id = document.getElementById("odontologo-form-eliminar-id").value;

        const requestOptions = {
            method: "DELETE",
            body: {},
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Z-Key',
                'Access-Control-Allow-Methods': 'GET, HEAD, POST, PUT, DELETE, OPTIONS'
            }
        }
        
        let hayError = false;
        fetch(`${url}/odontologos/eliminar/${id}`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#odontologo-span-eliminar");

                if(data.hasOwnProperty("message")){
                    spanResultado.innerHTML = data.message;
                }
                else{
                    spanResultado.innerHTML = "Se produjo un error que no permitió hacer la actualización";
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    /* -------------------------------------------------------------------------- */
    /*                 Seccion forms CRUD de Paciente                             */
    /* -------------------------------------------------------------------------- */

    const formConsultarPacientePorId = document.querySelector("#paciente-form-consultar-id");

    formConsultarPacientePorId.addEventListener("submit", function(event){
        event.preventDefault();
        let id = Math.trunc(document.getElementById("paciente-form-consultar-id-id").value);

        const tablaPacientes = document.querySelector("#paciente-tabla-id");
        let cantidadFilas = tablaPacientes.rows.length;
        while(cantidadFilas > 0){
            tablaPacientes.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
        
        let hayError = false;

        const requestOptions = {
            method: "GET"
        }
    
        fetch(`${url}/pacientes/${id}`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                if(!hayError){
                    renderizarPacientePorId(data);
                }
                else{
                    if(data.hasOwnProperty("message")){
                        alert(data.message);
                    }
                    else{
                        alert("No se pudo generar resultado para el ID:" + id);
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formRegistrarPaciente = document.querySelector("#paciente-form-registrar");

    formRegistrarPaciente.addEventListener("submit", function(event){
        event.preventDefault();

        let nombre = document.getElementById("paciente-form-registrar-nombre").value;
        let apellido = document.getElementById("paciente-form-registrar-apellido").value;
        let dni = document.getElementById("paciente-form-registrar-dni").value;
        let fechaIngreso = document.getElementById("paciente-form-registrar-fecha").value;
        let calle = document.getElementById("paciente-form-registrar-calle").value;
        let numero = document.getElementById("paciente-form-registrar-numero").value;
        let localidad = document.getElementById("paciente-form-registrar-localidad").value;
        let provincia = document.getElementById("paciente-form-registrar-provincia").value;

        let elHeader = new Headers();
        elHeader.append("Content-Type", "application/json");

        let payLoad = JSON.stringify({
            "nombre": nombre,
            "apellido": apellido,
            "dni": dni,
            "fechaIngreso": fechaIngreso,
            "domicilio":{
                "calle": calle,
                "numero": numero,
                "localidad": localidad,
                "provincia": provincia
            }
        });

        const requestOptions = {
            method: "POST",
            headers:elHeader,
            body: payLoad
        }
        
        let hayError = false;
        fetch(`${url}/pacientes/registrar`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#paciente-span-registrar");
                if(!hayError){
                    spanResultado.innerHTML = "Se ha creado el Paciente con ID:" + data.id;
                    document.querySelector("#paciente-form-registrar").reset();
                }
                else{
                    if(data.hasOwnProperty("message")){
                        spanResultado.innerHTML = data.message;
                    }
                    else{
                        spanResultado.innerHTML = "Se produjo un error que no permitió hacer el registro";
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formActualizarPaciente = document.querySelector("#paciente-form-actualizar");

    formActualizarPaciente.addEventListener("submit", function(event){
        event.preventDefault();

        let id = document.getElementById("paciente-form-actualizar-id").value;
        let nombre = document.getElementById("paciente-form-actualizar-nombre").value;
        let apellido = document.getElementById("paciente-form-actualizar-apellido").value;
        let dni = document.getElementById("paciente-form-actualizar-dni").value;
        let fechaIngreso = document.getElementById("paciente-form-actualizar-fecha").value;
        let idd = document.getElementById("paciente-form-actualizar-idd").value;
        let calle = document.getElementById("paciente-form-actualizar-calle").value;
        let numero = document.getElementById("paciente-form-actualizar-numero").value;
        let localidad = document.getElementById("paciente-form-actualizar-localidad").value;
        let provincia = document.getElementById("paciente-form-actualizar-provincia").value;

        let elHeader = new Headers();
        elHeader.append("Content-Type", "application/json");

        let payLoad = JSON.stringify({
            "id": id,
            "nombre": nombre,
            "apellido": apellido,
            "dni": dni,
            "fechaIngreso": fechaIngreso,
            "domicilio":{
                "id": idd,
                "calle": calle,
                "numero": numero,
                "localidad": localidad,
                "provincia": provincia
            }
        });

        const requestOptions = {
            method: "PUT",
            headers:elHeader,
            body: payLoad
        }
        
        let hayError = false;
        fetch(`${url}/pacientes/actualizar`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#paciente-span-actualizar");
                if(!hayError){
                    spanResultado.innerHTML = "Se ha modificado el Paciente con ID:" + data.id;
                    document.querySelector("#paciente-form-actualizar").reset();
                }
                else{
                    if(data.hasOwnProperty("message")){
                        spanResultado.innerHTML = data.message;
                    }
                    else{
                        spanResultado.innerHTML = "Se produjo un error que no permitió hacer la actualización";
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formEliminarPaciente = document.querySelector("#paciente-form-eliminar");

    formEliminarPaciente.addEventListener("submit", function(event){
        event.preventDefault();

        let id = document.getElementById("paciente-form-eliminar-id").value;

        const requestOptions = {
            method: "DELETE",
            body: {},
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Z-Key',
                'Access-Control-Allow-Methods': 'GET, HEAD, POST, PUT, DELETE, OPTIONS'
            }
        }
        
        let hayError = false;
        fetch(`${url}/pacientes/eliminar/${id}`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#paciente-span-eliminar");

                if(data.hasOwnProperty("message")){
                    spanResultado.innerHTML = data.message;
                }
                else{
                    spanResultado.innerHTML = "Se produjo un error que no permitió hacer la eliminación";
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    /* -------------------------------------------------------------------------- */
    /*                 Seccion forms CRUD de Turno                                */
    /* -------------------------------------------------------------------------- */

    const formConsultarTurnoPorId = document.querySelector("#turno-form-consultar-id");

    formConsultarTurnoPorId.addEventListener("submit", function(event){
        event.preventDefault();
        let id = Math.trunc(document.getElementById("turno-form-consultar-id-id").value);

        const tablaTurnos = document.querySelector("#turno-tabla-id");
        let cantidadFilas = tablaTurnos.rows.length;
        while(cantidadFilas > 0){
            tablaTurnos.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
        
        let hayError = false;

        const requestOptions = {
            method: "GET"
        }
    
        fetch(`${url}/turnos/${id}`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                if(!hayError){
                    renderizarTurnoPorId(data);
                }
                else{
                    if(data.hasOwnProperty("message")){
                        alert(data.message);
                    }
                    else{
                        alert("No se pudo generar resultado para el ID:" + id);
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formRegistrarTurno = document.querySelector("#turno-form-registrar");

    formRegistrarTurno.addEventListener("submit", function(event){
        event.preventDefault();

        let idPaciente = document.getElementById("turno-form-registrar-id-paciente").value;
        let idOdontologo = document.getElementById("turno-form-registrar-id-odontologo").value;
        let fechaYHora = document.getElementById("turno-form-registrar-fecha-hora").value.replace("T", " ");

        let elHeader = new Headers();
        elHeader.append("Content-Type", "application/json");

        let payLoad = JSON.stringify({
            "pacienteId": idPaciente,
            "odontologoId": idOdontologo,
            "fechaYHora": fechaYHora,
        });

        const requestOptions = {
            method: "POST",
            headers:elHeader,
            body: payLoad
        }
        
        let hayError = false;
        fetch(`${url}/turnos/registrar`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#turno-span-registrar");
                if(!hayError){
                    spanResultado.innerHTML = "Se ha creado el turno con ID:" + data.id;
                    document.querySelector("#turno-form-registrar").reset();
                }
                else{
                    if(data.hasOwnProperty("message")){
                        spanResultado.innerHTML = data.message;
                    }
                    else{
                        spanResultado.innerHTML = "Se produjo un error que no permitió hacer el registro";
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formActualizarTurno = document.querySelector("#turno-form-actualizar");

    formActualizarTurno.addEventListener("submit", function(event){
        event.preventDefault();

        let idTurno = document.getElementById("turno-form-actualizar-id-turno").value;
        let idPaciente = document.getElementById("turno-form-actualizar-id-paciente").value;
        let idOdontologo = document.getElementById("turno-form-actualizar-id-odontologo").value;
        let fechaYHora = document.getElementById("turno-form-actualizar-fecha-hora").value.replace("T", " ");

        let elHeader = new Headers();
        elHeader.append("Content-Type", "application/json");

        let payLoad = JSON.stringify({
            "id": idTurno,
            "pacienteId": idPaciente,
            "odontologoId": idOdontologo,
            "fechaYHora": fechaYHora
        });

        const requestOptions = {
            method: "PUT",
            headers:elHeader,
            body: payLoad
        }
        
        let hayError = false;
        fetch(`${url}/turnos/actualizar`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#turno-span-actualizar");
                if(!hayError){
                    spanResultado.innerHTML = "Se ha modificado el turno con ID:" + data.id;
                    document.querySelector("#turno-form-actualizar").reset();
                }
                else{
                    if(data.hasOwnProperty("message")){
                        spanResultado.innerHTML = data.message;
                    }
                    else{
                        spanResultado.innerHTML = "Se produjo un error que no permitió hacer la actualización";
                    }
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    const formEliminarTurno = document.querySelector("#turno-form-eliminar");

    formEliminarTurno.addEventListener("submit", function(event){
        event.preventDefault();

        let id = document.getElementById("turno-form-eliminar-id-turno").value;

        const requestOptions = {
            method: "DELETE",
            body: {},
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Z-Key',
                'Access-Control-Allow-Methods': 'GET, HEAD, POST, PUT, DELETE, OPTIONS'
            }
        }
        
        let hayError = false;
        fetch(`${url}/turnos/eliminar/${id}`,requestOptions)
            .then(response => {
                if(response.ok){
                    return response.json()
                }
                else{
                    hayError = true;
                    try{
                        return response.json();
                    }
                    catch(e){
                        return undefined
                    }
                }
            }
            )
            .then(data => {
                console.log(data);
                const spanResultado = document.querySelector("#turno-span-eliminar");

                if(data.hasOwnProperty("message")){
                    spanResultado.innerHTML = data.message;
                }
                else{
                    spanResultado.innerHTML = "Se produjo un error que no permitió hacer la eliminación";
                }
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    });

    /* -------------------------------------------------------------------------- */
    /*                 Funciones varias                                           */
    /* -------------------------------------------------------------------------- */

    function listarOdontologos(){
        const requestOptions = {
            method: "GET"
        }
    
        fetch(`${url}/odontologos/`,requestOptions)
            .then(response => response.json())
            .then(data => {
                renderizarListadoOdontologos(data)
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    }

    function listarPacientes(){
        const requestOptions = {
            method: "GET"
        }
    
        fetch(`${url}/pacientes/`,requestOptions)
            .then(response => response.json())
            .then(data => {
                renderizarListadoPacientes(data)
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    }

    function listarTurnos(){
        const requestOptions = {
            method: "GET"
        }
    
        fetch(`${url}/turnos/`,requestOptions)
            .then(response => response.json())
            .then(data => {
                renderizarListadoTurnos(data)
            })
            .catch(error => {
                console.log("pendeji-error",error)
            })
    }

    function renderizarListadoOdontologos(data){
        const tablaOdontologos = document.querySelector("#odontologo-tabla-listar");

        data.forEach(element => {
            tablaOdontologos.insertRow(-1).innerHTML =
                `
                <td>${element.id}</td>
                <td>${element.matricula}</td>
                <td>${element.nombre}</td>
                <td>${element.apellido}</td>
                `
            ;
        });
    }

    function renderizarListadoPacientes(data){
        const tablaPacientes = document.querySelector("#paciente-tabla-listar");

        data.forEach(element => {
            tablaPacientes.insertRow(-1).innerHTML =
                `
                <td>${element.id}</td>
                <td>${element.nombre}</td>
                <td>${element.apellido}</td>
                <td>${element.dni}</td>
                <td>${element.fechaIngreso}</td>
                <td>${element.domicilio.calle}</td>
                <td>${element.domicilio.numero}</td>
                <td>${element.domicilio.localidad}</td>
                <td>${element.domicilio.provincia}</td>
                `
            ;
        });
    }

    function renderizarListadoTurnos(data){
        const tablaTurnos = document.querySelector("#turno-tabla-listar");

        data.forEach(element => {
            tablaTurnos.insertRow(-1).innerHTML =
                `
                <td>${element.id}</td>
                <td>${element.pacienteTurnoSalidaDto.id}</td>
                <td>${element.pacienteTurnoSalidaDto.nombre}</td>
                <td>${element.pacienteTurnoSalidaDto.apellido}</td>
                <td>${element.odontologoTurnoSalidaDto.id}</td>
                <td>${element.odontologoTurnoSalidaDto.nombre}</td>
                <td>${element.odontologoTurnoSalidaDto.apellido}</td>
                <td>${element.fechaYHora}</td>
                `
            ;
        });
    }

    function renderizarOdontologoPorId(data){
        const tablaOdontologos = document.querySelector("#odontologo-tabla-id");

        tablaOdontologos.insertRow(-1).innerHTML =
                `
                <td>${data.id}</td>
                <td>${data.matricula}</td>
                <td>${data.nombre}</td>
                <td>${data.apellido}</td>
                `
        ;
    }

    function renderizarPacientePorId(data){
        const tablaOdontologos = document.querySelector("#paciente-tabla-id");

        tablaOdontologos.insertRow(-1).innerHTML =
                `
                <td>${data.id}</td>
                <td>${data.nombre}</td>
                <td>${data.apellido}</td>
                <td>${data.dni}</td>
                <td>${data.fechaIngreso}</td>
                <td>${data.domicilio.calle}</td>
                <td>${data.domicilio.numero}</td>
                <td>${data.domicilio.localidad}</td>
                <td>${data.domicilio.provincia}</td>
                `
        ;
    }

    function renderizarTurnoPorId(data){
        const tablaTurnos = document.querySelector("#turno-tabla-id");

        tablaTurnos.insertRow(-1).innerHTML =
                `
                <td>${data.id}</td>
                <td>${data.pacienteTurnoSalidaDto.id}</td>
                <td>${data.pacienteTurnoSalidaDto.nombre}</td>
                <td>${data.pacienteTurnoSalidaDto.apellido}</td>
                <td>${data.odontologoTurnoSalidaDto.id}</td>
                <td>${data.odontologoTurnoSalidaDto.nombre}</td>
                <td>${data.odontologoTurnoSalidaDto.apellido}</td>
                <td>${data.fechaYHora}</td>
                `
        ;
    }

    function ocultarDivs(){
        const tarjetas = document.getElementsByClassName("contenidos");
        
        Array.prototype.forEach.call(tarjetas, function(divs){
            if (!divs.classList.toggle("oculto")){
                divs.classList.toggle("oculto")
            }
        }
        );
    }

    function limpiarTablaListarEntidadActual(){
        let tabla = undefined;
        switch (entidadActual){
            case ODONTOLOGO:
                tabla = document.querySelector("#odontologo-tabla-listar");
                break;
            case PACIENTE:
                tabla = document.querySelector("#paciente-tabla-listar");
                break;
            case TURNO:
                tabla = document.querySelector("#turno-tabla-listar");
                break;
        }

        let cantidadFilas = tabla.rows.length;
        while(cantidadFilas > 0){
            tabla.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
    }

    function limpiarTablaConsultarPorIdEntidadActual(){
        let tabla = undefined;
        switch (entidadActual){
            case ODONTOLOGO:
                tabla = document.querySelector("#odontologo-tabla-id");
                break;
            case PACIENTE:
                tabla = document.querySelector("#paciente-tabla-id");
                break;
            case TURNO:
                tabla = document.querySelector("#turno-tabla-id");
                break;
        }

        let cantidadFilas = tabla.rows.length;
        while(cantidadFilas > 0){
            tabla.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
    }

    function listarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                listarOdontologos();
                break;
            case PACIENTE:
                listarPacientes();
                break;
            case TURNO:
                listarTurnos();
                break;
        }
    }

    function mostrarDivListarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                const divListarOdontologos = document.querySelector("#odontologo-div-listar");
                divListarOdontologos.classList.toggle("oculto");
                break;
            case PACIENTE:
                const divListarPacientes = document.querySelector("#paciente-div-listar");
                divListarPacientes.classList.toggle("oculto");
                break;
            case TURNO:
                const divListarTurnos = document.querySelector("#turno-div-listar");
                divListarTurnos.classList.toggle("oculto");
                break;
        }
    }

    function mostrarDivConsultarIdEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                const divConsultarOdontologoPorId = document.querySelector("#odontologo-div-consultar");
                divConsultarOdontologoPorId.classList.toggle("oculto");
                break;
            case PACIENTE:
                const divConsultarPacientePorId = document.querySelector("#paciente-div-consultar");
                divConsultarPacientePorId.classList.toggle("oculto");
                break;
            case TURNO:
                const divConsultarTurnoPorId = document.querySelector("#turno-div-consultar");
                divConsultarTurnoPorId.classList.toggle("oculto");
                break;
        }
    }

    function mostrarDivRegistrarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                const divRegistrarOdontologo = document.querySelector("#odontologo-div-registrar");
                divRegistrarOdontologo.classList.toggle("oculto");
                break;
            case PACIENTE:
                const divRegistrarPaciente = document.querySelector("#paciente-div-registrar");
                divRegistrarPaciente.classList.toggle("oculto");
                break;
            case TURNO:
                const divRegistrarTurno = document.querySelector("#turno-div-registrar");
                divRegistrarTurno.classList.toggle("oculto");
                break;
        }
    }

    function mostrarDivActualizarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                const divActualizarOdontologo = document.querySelector("#odontologo-div-actualizar");
                divActualizarOdontologo.classList.toggle("oculto");
                break;
            case PACIENTE:
                const divActualizarPaciente = document.querySelector("#paciente-div-actualizar");
                divActualizarPaciente.classList.toggle("oculto");
                break;
            case TURNO:
                const divActualizarTurno = document.querySelector("#turno-div-actualizar");
                divActualizarTurno.classList.toggle("oculto");
                break;
        }
    }

    function mostrarDivEliminarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                const divEliminarOdontologo = document.querySelector("#odontologo-div-eliminar");
                divEliminarOdontologo.classList.toggle("oculto");
                break;
            case PACIENTE:
                const divEliminarPaciente = document.querySelector("#paciente-div-eliminar");
                divEliminarPaciente.classList.toggle("oculto");
                break;
            case TURNO:
                const divEliminarTurno = document.querySelector("#turno-div-eliminar");
                divEliminarTurno.classList.toggle("oculto");
                break;
        }
    }

    function resetFormConsultarPorIdEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                document.querySelector("#odontologo-form-consultar-id").reset();
                break;
            case PACIENTE:
                document.querySelector("#paciente-form-consultar-id").reset();
                break;
            case TURNO:
                document.querySelector("#turno-form-consultar-id").reset();
                break;
        }
    }

    function resetFormRegistrarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                document.querySelector("#odontologo-span-registrar").innerHTML = "";
                document.querySelector("#odontologo-form-registrar").reset();
                break;
            case PACIENTE:
                document.querySelector("#paciente-span-registrar").innerHTML = "";
                document.querySelector("#paciente-form-registrar").reset();
                break;
            case TURNO:
                document.querySelector("#turno-span-registrar").innerHTML = "";
                document.querySelector("#turno-form-registrar").reset();
                break;
        }
    }

    function resetFormActualizarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                document.querySelector("#odontologo-span-actualizar").innerHTML = "";
                document.querySelector("#odontologo-form-actualizar").reset();
                break;
            case PACIENTE:
                document.querySelector("#paciente-span-actualizar").innerHTML = "";
                document.querySelector("#paciente-form-actualizar").reset();
                break;
            case TURNO:
                document.querySelector("#turno-span-actualizar").innerHTML = "";
                document.querySelector("#turno-form-actualizar").reset();
                break;
        }
    }

    function resetFormEliminarEntidadActual(){
        switch (entidadActual){
            case ODONTOLOGO:
                document.querySelector("#odontologo-span-eliminar").innerHTML = "";
                document.querySelector("#odontologo-form-eliminar").reset();
                break;
            case PACIENTE:
                document.querySelector("#paciente-span-eliminar").innerHTML = "";
                document.querySelector("#paciente-form-eliminar").reset();
                break;
            case TURNO:
                document.querySelector("#turno-span-eliminar").innerHTML = "";
                document.querySelector("#turno-form-eliminar").reset();
                break;
        }
    }

    /* -------------------------------------------------------------------------- */
    /*                 Ejecución inicial por defecto                              */
    /* -------------------------------------------------------------------------- */
    ocultarDivs();
    mostrarDivListarEntidadActual();
    listarEntidadActual();
});
