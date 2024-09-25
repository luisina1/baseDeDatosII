package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class DetalleEmpleado {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idDetalleEmpleado;

        private String direccion;
        private String telefono;

        @OneToOne
        @JoinColumn(name = "id_empleado")
        private Empleado empleado;
    }
