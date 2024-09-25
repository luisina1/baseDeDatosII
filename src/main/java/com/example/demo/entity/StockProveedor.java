package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer entrada;
    private Integer salida;
    private LocalDate fecha;
    private Double cantidadDisponible;
    private Double stockInicial;

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    public Double calcularCantidadDisponible() {
        return stockInicial - entrada + salida;
    }
}
