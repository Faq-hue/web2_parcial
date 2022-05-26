if (localStorage.getItem("alumnos") !== null) {
    dibujarTabla()
}

function registar() {

    let list = JSON.parse(localStorage.getItem("alumnos"))

    if (list === null) {
        list = []
    }


    if (verificar()) {
        return
    }

    const peso = document.getElementById("peso").value
    const altura = document.getElementById("altura").value
    const inc = calcularINC(Number(peso), Number(altura))

    // Masculino -> 1, Femenino -> 2
    const alumno = {
        apellido: document.getElementById("apellido").value,
        legajo: document.getElementById("legajo").value,
        sexo: document.getElementById("sexo").value,
        peso: peso,
        altura: altura,
        inc: inc
    }

    list.push(alumno)

    localStorage.setItem("alumnos", JSON.stringify(list))

    dibujarTabla();
}

function borrar(legajo) {
    let list = JSON.parse(localStorage.getItem("alumnos"))

    list.forEach((alumno, index) => {
        if (alumno.legajo == legajo) {
            list.splice(index, 1)
        }
    })
    localStorage.setItem("alumnos", JSON.stringify(list))
    dibujarTabla()
}

function editar(legajo) {
    let form = document.getElementById("editForm")
    form.innerHTML = `<form>
                <h1>Editar Alumno</h1>
                <div class="mb-3">
                    <label for="apellido" class="form-label">Apellido</label>
                    <input type="text" class="form-control" name="apellido" id="Eapellido"
                        placeholder="ingrese el Apellido">
                </div>
                <div class="mb-3">
                    <label for="sexo">Sexo:</label>
                    <select class="form-select" id="Esexo">
                        <option selected value="1">Masculino</option>
                        <option value="2">Femenino</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="peso" class="form-label">Peso en Kg</label>
                    <input type="number" class="form-control" name="peso" id="Epeso" placeholder="ingrese el Peso en Kg">
                </div>

                <div class="mb-3">
                    <label for="altura" class="form-label">Altura en CM</label>
                    <input type="number" class="form-control" name="altura" id="Ealtura"
                        placeholder="ingrese la Altura en CM">
                </div>

                <div class="mb-3">
                    <input type="button" class="btn btn-success" value="Actualizar alumno" onclick="registarEdicion(${legajo})">
                </div>
            </form>`
}

function registarEdicion(legajo) {
    let list = JSON.parse(localStorage.getItem("alumnos"))
    let alumno
    list.forEach(e => { if (e.legajo == legajo) { alumno = e } })
    delete (legajo)
    alumno.apellido = document.getElementById("Eapellido").value
    alumno.peso = document.getElementById("Epeso").value
    alumno.sexo = document.getElementById("Esexo").value
    alumno.altura = document.getElementById("Ealtura").value

    if(alumno.apellido.length < 3){
        alert("El apellido debe tener al menos 3 caracteres")
        document.getElementById("editForm").innerHTML = ""
        return
    }
    if(Number(alumno.peso) == 0 || Number(alumno.altura) == 0){
        alert("El peso y la altura deben ser mayores que 0")
        document.getElementById("editForm").innerHTML = ""
        return
    }


    localStorage.setItem("alumnos",JSON.stringify(list))
    dibujarTabla()
    document.getElementById("editForm").innerHTML = ""
}

function dibujarTabla() {
    let tbody = document.getElementById("tableBody")
    let pesoTotal = document.getElementById("calPesoTotal")
    let list = JSON.parse(localStorage.getItem("alumnos"))
    let counter = 0
    let template = ""
    tbody.innerHTML = ""
    list.forEach(alumno => {
        if (alumno.inc <= 25) { template += "<tr class='table-primary'>" }
        if (alumno.inc > 25 && alumno.inc <= 30) {
            template += "<tr class='table-warning'>"
            counter++
        }
        if (alumno.inc > 30) {
            template += "<tr class='table-danger'>"
            counter++
        }
        tbody.innerHTML += template + `<td>${alumno.apellido}</td>
                                <td>${alumno.legajo}</td>
                                <td>${alumno.peso}</td>
                                <td>${alumno.altura}</td>
                                <td>${alumno.sexo == 1 ? "Masculino" : "femenino"}</td>
                                <td>${alumno.inc}</td><td>
                                <input type="button" class="btn btn-danger" value="Borrar" onclick="borrar(${alumno.legajo})">
                                <input type="button" class="btn btn-warning" value="Editar" onclick="editar(${alumno.legajo})">
                                </td></tr>`
    })

    pesoTotal.innerHTML = counter

}

function calcularINC(peso, altura) {
    const alturaM = altura / 100
    return peso / (alturaM * alturaM)
}

function verificar() {
    const apellido = document.getElementById("apellido").value
    const legajo = document.getElementById("legajo").value
    const peso = document.getElementById("peso").value
    const altura = document.getElementById("altura").value
    list = JSON.parse(localStorage.getItem("alumnos"))
    let aux = false


    //Si sobra arreglar

    if (apellido.length < 3) {
        alert("El apellido debe tener como minimo 3 caracteres")
        aux = true
    }

    if (list !== null) {
        list.forEach(alumno => {
            if (alumno.legajo == legajo) {
                alert("El legajo ya esta registrado")
                aux = true
            }
        })
    }

    if (Number(peso) == 0 || Number(altura) == 0) {
        alert("El peso y la altura deben ser mayores a 0")
        aux = true
    }

    return aux
}