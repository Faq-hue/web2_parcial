/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import dao.ProductoDAO;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

import dto.ProductoDTO;

/**
 * @author CARLOMAGNO
 */
public class administradorSocket extends Thread {
  private final Socket conector;
  private String httpPedido;

  administradorSocket(Socket insocket) {
    conector = insocket;
  }

  public void procesarSocket() {
    this.start();
  }

  @Override // sobreescribimos el método run del Thread para procesar el socket

  public void run() { // proceso principal del server
    try {
      // capturamos el flujo de entrada
      // creamos un objeto para manipular el pedido. Necesitamos que tenga acceso al
      // socket
      // se lo inyectamos en el constructor

      InputStream flujoentrada = conector.getInputStream();
      BufferedReader buffer = new BufferedReader(new InputStreamReader(flujoentrada));
      // necesitamos un printStream para enviar archivos
      PrintStream ps = new PrintStream(new BufferedOutputStream(conector.getOutputStream()));
      // procesamos El Stream y se lo pasamos como String a HttpRequest
      String linea = buffer.readLine();
      String header = linea;
      /** Si la cabecera es nula salimos !!! */
      if (header == null)
        return;

      StringTokenizer tokenizer = new StringTokenizer(header);
      tokenizer.nextToken();
      httpPedido = tokenizer.nextToken();
      httpPedido = header + "\r\n";

      while (buffer.ready()) {
        /** Leemos todo el pedido HTTP hasta el final.... */

        httpPedido += buffer.readLine() + "\r\n";
      }
      System.out.println(httpPedido);
      /*
       * System.out.println("HTTP-METHOD: " + metodoPedido);
       * System.out.println(httpPedido);
       */

      HttpRequest req = new HttpRequest(httpPedido);
      HttpResponse resp = new HttpResponse(conector);

      // AHORA PARA VER SI TOD,O ESTA OK VAMOS A GENERAR UN ECHO AL WEB BROWSER
      // ANTES QUE NADA SE ENVIA UN ENCABEZADO DE ESTADO Y AUTORIZACION PARA CORS SINO
      // NO FUNCIONA
      // DESDE OTROS SITIOS EXTERNOS, POR EJEMPLO ALGO QUE HAGAMOS CON ANGULARJS Y
      // QUERRAMOS CONSUMIR
      // EXTERNAMENTE
      // capturamos los parametros enviados

      String PaginaInicio;
      System.out.println("Accion: " + req.getAccion());
      System.out.println(req.getMetodo());

      // EMPEZAMOS EL ANALISIS DE GET
      if (req.getMetodo().trim().equalsIgnoreCase("GET")) {

        if (req.getAccion() != null) {
          String hacer = req.getAccion();

          // ANALIZAMOS LAS ACCIONES
          if (hacer.equals("api")) {
            ProductoDTO pDTO = new ProductoDTO();
            ProductoDAO pDAO = new ProductoDAO();
            List<ProductoDTO> listaProductos = pDAO.readAll();
            ProductoDTO producto = new ProductoDTO();
            Gson gson = new Gson();
            String listadoJson = "[";

            for (int i = 0; i < listaProductos.size(); i++) {
              producto = (ProductoDTO) listaProductos.get(i);
              listadoJson += gson.toJson(producto) + ",";
            }

            listadoJson = listadoJson.substring(0, listadoJson.length() - 1);
            listadoJson += "]";

            // resp.enviarRespuesta
            System.out.println(gson.toJson((ProductoDTO) pDTO));
            PaginaInicio = resp.getInitPage(gson.toJson((ProductoDTO) pDTO));
            resp.imprimirSalida(resp.getHeader());
            resp.imprimirSalida(listadoJson);

          } else {
            if (req.getAccion().equals(" ")) {
              PaginaInicio = resp.getHeader();
              PaginaInicio = resp.getInitPage("Servidor Del Segundo parcial de web II");
              resp.imprimirSalida(PaginaInicio);
            } else
              req.enviarArchivo("", ps);
          }
        }
      }

      // EMPEZAMOS EL ANALISIS DE POST
      if (req.getMetodo().trim().equalsIgnoreCase("POST")) {
        System.out.println("estoy en Post");
        if (req.getAccion() != null) {
          String hacer = req.getAccion();

          // ANALIZAMOS LAS ACCIONES

          if (hacer.equalsIgnoreCase("api")) {
            System.out.println("estoy en Listar del Post");
            ProductoDAO pDAO = new ProductoDAO();
            String params = req.getParametrosPost();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            
            // extraer el body del params
            String test = params.substring(params.indexOf("form-data; name=") + 11, params.length());
            System.out.println("Test:" + test);

            String test2[] = test.split("name=\"");

            for (int i = 1; i < test2.length; i++) {
              // quitar el primer " que encuentre
              test2[i] = test2[i].substring(test2[i].indexOf("\"") + 1);
              // quitar el guion del final
              test2[i] = test2[i].substring(0, test2[i].indexOf("-"));
            }

            ProductoDTO pd = new ProductoDTO();

            pd.setCodigo(Integer.parseInt(test2[1]));
            pd.setNombre(test2[2]);
            pd.setDescripcion(test2[3]);
            pd.setMarca(test2[4]);
            pd.setPrecioUnitario(Float.parseFloat(test2[5]));
            pd.setCantidad(Integer.parseInt(test2[6]));
            pd.setIva(Float.parseFloat(test2[7]));

            //System.out.println("ACA ESTOY PELOTUDO"+test2[8]);

            System.out.println(pd.toString());

            int accion = Integer.parseInt(test2[8]);

            switch (accion) {
              case 2:
                pDAO.create(pd);
                break;
              case 1:
                pDAO.delete(pd.getCodigo());
                break;
              case 0:
                pDAO.update(pd);
                break;
            }

          }

        }

      }

      resp.cerrar();
      conector.close();

    } catch (

    Exception ex) {
      System.out.println(ex.getMessage());
    }

  }
}
