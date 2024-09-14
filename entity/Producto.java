package baseII.finall.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    private String nombre;
    private String descripcion;
    private String categoria;

    @OneToMany
    private List<MovimientoInventario> movimientosInventarios;

    @ManyToMany(mappedBy = "productos")
    private List<Venta> ventas;
}
