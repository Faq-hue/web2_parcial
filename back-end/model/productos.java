package model;

public class productos {
    private int codigo;
    private String descripcion;
    private String marca;
    private int cantidad;
    private float precioUnitario;
    private float iva;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    @Override
    public String toString() {
        return "productos [cantidad=" + cantidad + ", codigo=" + codigo + ", descripcion=" + descripcion + ", iva="
                + iva + ", marca=" + marca + ", precioUnitario=" + precioUnitario + "]";
    }
    
}
