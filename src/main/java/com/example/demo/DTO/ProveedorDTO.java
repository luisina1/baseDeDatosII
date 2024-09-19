package com.example.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProveedorDTO {
    private Integer id;
    private String nombreCompleto;
    private String contacto;
    private String direccion;
    private List<ProductoDTO> productos;
    private StockProveedorDTO stockProveedor;
}
