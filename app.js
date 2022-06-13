if (localStorage.getItem("productos") !== null) {
  dibujarTabla();
}

async function registar() {
  let list = pedir();
  var validate = true;
  await list.then((data) => {
    console.log("ESTOY ACA");
    let aux = true
    const codigo = document.getElementById("codigo").value;
    const nombre = document.getElementById("nombre").value;
    const cantidad = Number(document.getElementById("cantidad").value);
    const marca = document.getElementById("marca").value;
    const precio = Number(document.getElementById("precio").value);
    const ignorar = Number(document.getElementById("accion").value)

    if (!longitud(3, codigo)) {
      alert("El codigo debe tener al menos 3 caracteres");
      aux = false;
    }

    data.forEach((producto) => {
      if (producto.codigo == codigo && ignorar == 3) {
        alert("El codigo ya existe");
        aux = false;
      }
    });

    if (aux == false) {
      aux = false;
    }

    if (!longitud(3, nombre)) {
      alert("El nombre debe tener al menos 3 caracteres");
      aux = false;
    }

    if (!longitud(3, marca)) {
      alert("La marca debe tener al menos 3 caracteres");
      aux = false;
    }

    if (cantidad == 0) {
      alert("La cantidad debe ser mayor que 0");
      aux = false;
    }

    if (precio == 0) {
      alert("El precio debe ser mayor que 0");
      aux = false;
    }

    validate = aux;
    console.log("ESTOY EN LA LAMBDA:" + validate);
  });
  dibujarTabla();
  console.log("ESTOY FUERA DE LA LAMBDA:" + validate);
  return validate
}

function dibujarTabla() {
  let tbody = document.getElementById("tableBody");
  let totalStock = document.getElementById("totalStock");
  let total = 0;
  let template = "";
  let list = pedir();
  list.then((data) => {
    data.forEach((producto) => {
      if (producto.iva == 0.1) {
        template += `<tr class='table-primary'>`;
      } else {
        template += `<tr class='table-warning'>`;
      }
      let subtotal =
        producto.precioUnitario * producto.cantidad +
        producto.precioUnitario * producto.cantidad * producto.iva;
      total += subtotal;
      template += `
      <td>${producto.codigo}</td>
      <td>${producto.nombre}</td>
      <td>${producto.descripcion}</td>
      <td>${producto.marca}</td>
      <td>${producto.precioUnitario}</td>
      <td>${producto.cantidad}</td>
      <td>${producto.iva}</td>
      <td>${subtotal}</td>
      <td><button class='btn btn-danger' onclick='eliminar(${producto.codigo})'>Eliminar</button></td>
      </tr>
      `;
    });
    tbody.innerHTML = template;
    totalStock.innerHTML = total;
  });
}

function longitud(longitud, cadena) {
  if (cadena.length < longitud) {
    return false;
  }
  return true;
}

async function pedir() {
  let list = [];
  let req = await fetch("http://127.0.0.1:8866/api");
  list = await req.json();
  return list;
}
