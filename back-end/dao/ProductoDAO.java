/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dto.ProductoDTO;
import server.Conexion;

/**
 *
 * @author csimes
 */
public class ProductoDAO implements intCRUD<ProductoDTO> {
    public static final Conexion con = Conexion.crearConexion();
    public static final String SQL_INSERT = "insert into producto(codigo,nombre,descripcion,marca,cantidad,precioUnitario,iva) values (?,?,?,?,?,?,?)";
    public static final String SQL_DELETE = "delete from producto where codigo=?";
    public static final String SQL_UPDATE = "update producto set nombre=?,descripcion=?,marca=?,cantidad=?,precioUnitario=?,iva=? where codigo=?";
    public static final String SQL_READ = "select * from producto where codigo=?";
    public static final String SQL_READALL = "select * from producto";

    @Override

    public boolean create(ProductoDTO e) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_INSERT);
         
            ps.setInt(1, e.getCodigo());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getDescripcion());
            ps.setString(4, e.getMarca());
            ps.setInt(5, e.getCantidad());
            ps.setFloat(6, e.getPrecioUnitario());
            ps.setFloat(7, e.getIva());

            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return false;
    }

    @Override
    public boolean delete(Object clave) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_DELETE);
            ps.setInt(1, (int) clave);

            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return false;

    }

    @Override
    public boolean update(ProductoDTO e) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_UPDATE);

            ps.setInt(1, e.getCodigo());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getDescripcion());
            ps.setString(4, e.getMarca());
            ps.setInt(5, e.getCantidad());
            ps.setFloat(6, e.getPrecioUnitario());
            ps.setFloat(7, e.getIva());

            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return false;

    }

    @Override
    public ProductoDTO read(Object clave) {

        ProductoDTO producto = null;
        try {

            ResultSet rs = null;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_READ);
            ps.setInt(1, (int) clave);

            rs = ps.executeQuery();
            if (rs.next()) {
                producto = new ProductoDTO();
                producto.setCodigo(rs.getInt("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setMarca(rs.getString("marca"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setPrecioUnitario(rs.getFloat("precioUnitario"));
                producto.setIva(rs.getFloat("iva"));
                return producto;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return producto;

    }

    @Override
    public List<ProductoDTO> readAll() {
        ProductoDTO producto;
        List<ProductoDTO> lista = new ArrayList<>();

        try {

            ResultSet rs = null;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_READALL);

            rs = ps.executeQuery();

            while (rs.next()) {

                producto = new ProductoDTO();
                producto.setCodigo(rs.getInt("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setMarca(rs.getString("marca"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setPrecioUnitario(rs.getFloat("precioUnitario"));
                producto.setIva(rs.getFloat("iva"));
                lista.add(producto);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        finally {
            con.cerrarConexion();
        }
        return lista;

    }

}
