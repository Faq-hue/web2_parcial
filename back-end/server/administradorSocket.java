/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

//import com.google.gson.Gson;
//import static com.iw2.core.Util. System.out.println;
import dao.ProductoDAO;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import dto.ProductoDTO;

/**
 * @author CARLOMAGNO
 */
public class administradorSocket extends Thread {
  private final Socket conector;
  private String metodoPedido;
  private String httpPedido;
  private final String NEW_LINE = "\r\n";
  private StringBuffer sb;
  private final String APP_NAME = "api";
  private String URL;

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
      metodoPedido = tokenizer.nextToken();
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

      // AHORA PARA VER SI TODO ESTA OK VAMOS A GENERAR UN ECHO AL WEB BROWSER
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

          if (hacer.equalsIgnoreCase("Listar")) {
            ProductoDTO lib = new ProductoDTO();
            ProductoDAO ldao = new ProductoDAO();
            List<ProductoDTO> listadoLibros;
            listadoLibros = ldao.readAll();
            ProductoDTO libro = new ProductoDTO();
            // TODO agregar GSON
            Gson gson = new Gson();
            String listadoJSON = "[";

            for (int t = 0; t < listadoLibros.size(); t++) {
              libro = (ProductoDTO) listadoLibros.get(t);

              listadoJSON += gson.toJson(libro) + ",";
            }
            listadoJSON = listadoJSON.substring(0, listadoJSON.length() - 1);
            listadoJSON += "]";
            // resp.enviarRespuestaDatos(200, resp.getInitPage("Hola Mundo !!!"));
            System.out.println(gson.toJson((ProductoDTO) lib));
            PaginaInicio = resp.getInitPage(gson.toJson((ProductoDTO) lib));
            resp.imprimirSalida(resp.getHeader());
            resp.imprimirSalida(listadoJSON);
          } else { // no piden ninguna accion enviamos un archivo, por defecto es index.html
            if (req.getAccion().equals(" ")) // no pidieron nada enviamos pagina principal
            {
              PaginaInicio = resp.getHeader();
              PaginaInicio += resp.getInitPage("hola desde el servidor IW2");
              resp.imprimirSalida(PaginaInicio);
            } else { // pidieron un archivo, lo enviamos
              req.enviarArchivo("", ps);
            }
          }
        }
      }
      // EMPEZAMOS EL ANALISIS DE POST

      if (req.getMetodo().trim().equalsIgnoreCase("POST")) {
        System.out.println("estoy en Post");
        if (req.getAccion() != null) {
          String hacer = req.getAccion();

          // ANALIZAMOS LAS ACCIONES

          if (hacer.equalsIgnoreCase("Listar")) {
            System.out.println("estoy en Listar del Post");
            ProductoDTO lib = new ProductoDTO();
            ProductoDAO ldao = new ProductoDAO();
            List<ProductoDTO> listadoLibros;
            listadoLibros = ldao.readAll();
            ProductoDTO libro = new ProductoDTO();
            Gson gson = new Gson();
            String listadoJSON = "[";

            for (int t = 0; t < listadoLibros.size(); t++) {
              libro = (ProductoDTO) listadoLibros.get(t);

              listadoJSON += gson.toJson(libro) + ",";
            }
            listadoJSON = listadoJSON.substring(0, listadoJSON.length() - 1);
            listadoJSON += "]";
            // resp.enviarRespuestaDatos(200, resp.getInitPage("Hola Mundo !!!"));
            System.out.println(gson.toJson((ProductoDTO) lib));
            PaginaInicio = resp.getInitPage(gson.toJson((ProductoDTO) lib));
            resp.imprimirSalida(resp.getHeader());
            resp.imprimirSalida(listadoJSON);
          } // no piden ninguna accion enviamos un archivo, por defecto es index.html

          if (hacer.trim().equalsIgnoreCase("Buscar")) {
            System.out.println("estoy en Buscar del Post");
            ProductoDTO lib = new ProductoDTO();
            ProductoDAO ldao = new ProductoDAO();
            List<ProductoDTO> listadoLibros;
            Gson gson = new Gson();
            String pp = req.getParametrosPost();
            if (pp != null) {
            }
            // listadoLibros = ldao.read(300);
            ProductoDTO libro = new ProductoDTO();
            libro = ldao.read(300);
            String listadoJSON = "[";

            listadoJSON += gson.toJson(libro) + ",";

            listadoJSON = listadoJSON.substring(0, listadoJSON.length() - 1);
            listadoJSON += "]";

            // resp.enviarRespuestaDatos(200, resp.getInitPage("Hola Mundo !!!"));
            System.out.println(gson.toJson((ProductoDTO) lib));
            PaginaInicio = resp.getInitPage(gson.toJson((ProductoDTO) lib));
            resp.imprimirSalida(resp.getHeader());
            resp.imprimirSalida(listadoJSON);
          } // no piden ninguna accion enviamos un archivo, por defecto es index.html

          if (req.getAccion().equals(" ")) // no pidieron nada enviamos pagina principal
          {
            PaginaInicio = resp.getHeader();
            PaginaInicio += resp.getInitPage("hola desde el servidor IW2");
            resp.imprimirSalida(PaginaInicio);
          }

        }

      }

      resp.cerrar();
      conector.close();

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

  }
}
