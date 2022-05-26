# web2_parcial
Desarrolle una aplicación Web Responsive utilizando Bootstrap y Javascript.
Los Datos registrados estarán alojados en localStorage y no se podrán registrar registros duplicados.
Posteriormente se utilizará el WebServer IW2 y una base de datos MySql la reemplazará el alojamiento local por almacenamiento remoto.
Se deberá modificar el Webserver para que procese solicitudes Get y Post e implemente las funcionalidades que inicialmente se realizan en el localStorage, esto es Alta, Baja, Modificación y Listado de las entidades que sean necesarias para hacer funcionar la webapp.
Construir una aplicación que permita cargar una serie de productos.
Estos tendrán un Código, Descripción, Marca, Cantidad y Precio Unitario e Iva (puede ser solo 10 % o 21 %).
De esta manera se ira creando una tabla con los distintos productos registrados (El stock de nuestro negocio)
A su vez la aplicación irá calculando a medida que ingresemos nuevos productos el valor total del stock, el que será igual a la Suma de los subtotales de cada producto, siendo que el subtotal se calculará como:
Subtotal=(Precio * Cantidad) + (Precio * Cantidad)*IVA/100
La tabla se deberá mostrar usando estilos condicionales, indicando en diferentes colores los registros dependiendo del IVA aplicado al producto (por ejemplo iva 10% puede ser verde e iva 21% puede ser naranja)
Se realizará la siguiente validación:
El código del Producto deberá tener al menos tres cifras y será único (No puede repetirse).
El nombre del producto deberá tener al menos tres caracteres.
Si se intenta registrar un producto cuyo código ya está registrado se avisará al usuario que el producto ya está y en vez de duplicar el registro, se efectuará la edición del mismo actualizando las cantidades.
Por ejemplo si ya estaba registrado el producto de código 094 que es Yerba y hay en stock 10 kilos a precio $150 y el usuario quiere ingresar nuevamente un producto 094 Yerba con cantidad 15 kilos y precio 180, el sistema avisará que el producto ya existe y de continuar se efectuará el incremento de la cantidad en stock agregando a los 10 kilos existentes los 15 kilos que estoy agregando, el precio se ajustará al nuevo valor ingresado y se calculará nuevamente el total para el producto con el nuevo precio .
Una Interfaz podría ser como:
Cada Registro tendrá además un botón Eliminar para poder Borrar un producto del stock y un botón Editar que permita modificar los valores registrados en el precio, la cantidad o el iva del producto.
Se contará con una función Ordenar, la que se ejecutará al hacer click en el título de una columna, la que ordenará la misma según ese criterio, por ejemplo si se hace clic en la columna Descripción, se ordenará la tabla en forma creciente según ese criterio.
