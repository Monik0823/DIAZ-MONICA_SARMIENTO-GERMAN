let entidadActual = "Odontologo";

window.addEventListener("load", () => {
    const url = "http://localhost:8081";

  /* -------------------------------------------------------------------------- */
  /*                 Seccion metodos de Odontologo                              */
  /* -------------------------------------------------------------------------- */

    listarOdontologos();
    ocultarDivs();
    mostrarDivListarEntidadActual();

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

    const botonListar = document.querySelector("#listar-todos-button");

    botonListar.addEventListener("click", function(){
        const tablaOdontologos = document.querySelector("#odontologo-tabla-listar");
        let cantidadFilas = tablaOdontologos.rows.length;
        while(cantidadFilas > 0){
            tablaOdontologos.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
        ocultarDivs();
        mostrarDivListarEntidadActual();
        listarOdontologos();
    });

    const botonConsultarPorId = document.querySelector("#consultar-id-button");

    botonConsultarPorId.addEventListener("click", function(){
        console.log("evento boton")
        const tablaOdontologos = document.querySelector("#odontologo-tabla-id");
        let cantidadFilas = tablaOdontologos.rows.length;
        while(cantidadFilas > 0){
            tablaOdontologos.deleteRow(cantidadFilas - 1);
            cantidadFilas --;
        }
        ocultarDivs();
        mostrarDivConsultarIdEntidadActual();
    });

    function ocultarDivs(){
        const divListarOdontologos = document.querySelector("#odontologo-div-listar");
        const divConsultarOdontologoPorId = document.querySelector("#odontologo-div-consultar");

        divListarOdontologos.toggleAttribute("class", "oculto");
        divConsultarOdontologoPorId.toggleAttribute("class", "oculto");
    }

    function mostrarDivListarEntidadActual(){
        switch (entidadActual){
            case "Odontologo":
                const divListarOdontologos = document.querySelector("#odontologo-div-listar");
                divListarOdontologos.toggleAttribute("class", "oculto");
                break;
        }
    }

    function mostrarDivConsultarIdEntidadActual(){
        switch (entidadActual){
            case "Odontologo":
                const divConsultarOdontologoPorId = document.querySelector("#odontologo-div-consultar");
                divConsultarOdontologoPorId.toggleAttribute("class", "oculto");
                break;
        }
    }
});
