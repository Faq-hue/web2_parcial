if (localStorage.getItem("productos") !== null) {
  dibujarTabla();
}

/*Sona de Test */
/*async function test () {
  let producto = await fetch("http://127.0.0.1:8866/api")
  let productos = await producto.json()
  console.log(productos)
  
}
test();*/
//test de post un producto


//---------------------

function registar() {
  let list = JSON.parse(localStorage.getItem("productos"));
  console.log("ACA TOY");

  if (list === null) {
    list = [];
  }

  if (verificar(list)) {
    preventDefault();
    return;
  }

  //subtotal  = precio * cantidad + precio * cantidad * iva
  const precio = Number(document.getElementById("precio").value);
  const cantidad = Number(document.getElementById("cantidad").value);
  const iva = Number(document.getElementById("iva").value);
  const subtotal = precio * cantidad + precio * cantidad * iva;

  const pruducto = {
    codigo: document.getElementById("codigo").value,
    nombre: document.getElementById("nombre").value,
    descripcion: document.getElementById("descripcion").value,
    marca: document.getElementById("marca").value,
    precio: precio,
    cantidad: cantidad,
    iva: iva,
    subtotal: subtotal,
  };

  list.push(pruducto);

  localStorage.setItem("productos", JSON.stringify(list));

  dibujarTabla();
}

function borrar(codigo) {
  let list = JSON.parse(localStorage.getItem("productos"));

  list.forEach((producto, index) => {
    if (producto.codigo == codigo) {
      list.splice(index, 1);
    }
  });
  localStorage.setItem("productos", JSON.stringify(list));
  dibujarTabla();
}

function editar(codigo) {
  let form = document.getElementById("editForm");
  form.innerHTML = `<form>
                <h1>Editar Producto</h1>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" name="nombre" id="Enombre"
                        placeholder="ingrese el nombre">
                </div>
            
                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripcion</label>
                    <input type="text" class="form-control" name="descripcion" id="Edescripcion"
                        placeholder="ingrese el descripcion">
                </div>
                
                <div class="mb-3">
                    <label for="marca" class="form-label">Marca</label>
                    <input type="text" class="form-control" name="marca" id="Emarca"
                        placeholder="ingrese la marca">
                </div>

                <div class="mb-3">
                    <label for="precio" class="form-label">Precio</label>
                    <input type="text" class="form-control" name="precio" id="Eprecio"
                        placeholder="ingrese la precio">
                </div>

                <div class="mb-3">
                    <label for="cantidad" class="form-label">Cantidad</label>
                    <input type="text" class="form-control" name="cantidad" id="Ecantidad"
                        placeholder="ingrese la cantidad">
                </div>

                <div class="mb-3">
                    <label for="iva">IVA</label>
                    <select class="form-select" id="Eiva">
                        <option selected value="0.1">10%</option>
                        <option value="0.21">21%</option>
                    </select>
                </div>

                <div class="mb-3">
                    <input type="button" class="btn btn-success" value="Actualizar producto" onclick="registarEdicion(${codigo})">
                </div>
            </form>`;
}

function registarEdicion(codigo) {
  let list = JSON.parse(localStorage.getItem("productos"));
  let producto;
  list.forEach((e) => {
    if (e.codigo == codigo) {
      producto = e;
    }
  });

  producto.nombre = document.getElementById("Enombre").value;
  producto.descripcion = document.getElementById("Edescripcion").value;
  producto.marca = document.getElementById("Emarca").value;
  producto.precio = Number(document.getElementById("Eprecio").value);
  producto.cantidad = Number(document.getElementById("Ecantidad").value);
  producto.iva = Number(document.getElementById("Eiva").value);
  producto.subtotal =
    producto.precio * producto.cantidad +
    producto.precio * producto.cantidad * producto.iva;
  console.log(producto);
  list.forEach((e) => {
    if (e.codigo == codigo) {
      e = producto;
    }
  });

  localStorage.setItem("productos", JSON.stringify(list));
  dibujarTabla();
  document.getElementById("editForm").innerHTML = "";
}

function dibujarTabla() {
  let tbody = document.getElementById("tableBody");
  let totalStock = document.getElementById("totalStock");
  let list = JSON.parse(localStorage.getItem("productos"));
  let total = 0;
  let template = "";

  tbody.innerHTML = "";

  list.forEach((producto) => {
    if (producto.iva == 0.1) {
      template += `<tr class='table-primary'>`;
    } else {
      template += `<tr class='table-warning'>`;
    }

    tbody.innerHTML +=
      template +
      `<td>${producto.codigo}</td>
                                <td>${producto.nombre}</td>
                                <td>${producto.descripcion}</td>
                                <td>${producto.marca}</td>
                                <td>${producto.precio}</td>
                                <td>${producto.cantidad}</td>
                                <td>${Number(producto.iva) * 100}%</td>
                                <td>${producto.subtotal}</td><td>
                                <input type="button" class="btn btn-danger" value="Borrar" onclick="borrar(${
                                  producto.codigo
                                })">
                                <input type="button" class="btn btn-warning" value="Editar" onclick="editar(${
                                  producto.codigo
                                })">
                                </td></tr>`;
    // TODO:ARREGLAR
    total += producto.cantidad;
  });

  totalStock.innerHTML = total;
}

function verificar(list) {
  const codigo = document.getElementById("codigo").value;
  const nombre = document.getElementById("nombre").value;
  const cantidad = Number(document.getElementById("cantidad").value);
  const marca = document.getElementById("marca").value;
  const precio = Number(document.getElementById("precio").value);

  let aux = false;

  if (!longitud(3, codigo)) {
    alert("El codigo debe tener al menos 3 caracteres");
    return true;
  }

  list.forEach((producto) => {
    if (producto.codigo == codigo) {
      alert("El codigo ya existe");
      aux = true;
    }
  });

  if (aux == true) {
    return true;
  }

  if (!longitud(3, nombre)) {
    alert("El nombre debe tener al menos 3 caracteres");
    return true;
  }

  if (!longitud(3, marca)) {
    alert("La marca debe tener al menos 3 caracteres");
    return true;
  }

  if (cantidad == 0) {
    alert("La cantidad debe ser mayor que 0");
    return true;
  }

  if (precio == 0) {
    alert("El precio debe ser mayor que 0");
    return true;
  }

  return false;
}

function longitud(longitud, cadena) {
  if (cadena.length < longitud) {
    return false;
  }
  return true;
}
