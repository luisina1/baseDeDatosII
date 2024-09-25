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
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStock;

    private Double entrada;
    private Double salida;
    private LocalDate fecha;
    private Double stockInicial;
    private Double cantidadDisponible;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public double calcularCantidadDisponible() {
        return stockInicial - entrada + salida;
    }
}
