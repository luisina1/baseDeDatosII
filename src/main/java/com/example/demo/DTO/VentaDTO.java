package com.example.demo.DTO;
import lombok.Data;
import java.util.List;

@Data
public class VentaDTO {
    private Integer idVenta;
    private String nombre;
    private double precio;
    private int cantidadTickets;
    private Double total;
    private List<ProductoDTO> productos;

    //public void calcularTotal() {
        //this.total = precio * cantidadTickets;
    //}
}
