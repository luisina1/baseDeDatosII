package baseII.finall.controller;

import baseII.finall.entity.Producto;
import baseII.finall.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping
    public List<Producto> getAll() {
        return productoService.getAll();
    }

    @GetMapping("/{idProducto}")
    public Optional<Producto> getById(@PathVariable("idProducto") Integer id) {
        return productoService.getById(id);
    }

    @PostMapping
    public Producto save(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<Producto> updateProducto(@PathVariable("idProducto") Integer id, @RequestBody Producto productos) {
        Optional<Producto> productoOptional = productoService.getById(id);

        if (productoOptional.isPresent()) {
            try {
                Producto producto = productoOptional.get();
                producto.setNombre(productos.getNombre());
                producto.setDescripcion(productos.getDescripcion());
                producto.setCategoria(productos.getCategoria());
                Producto updatedProducto = productoService.save(producto);
                return ResponseEntity.ok(updatedProducto);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{idProducto}")
    public void deleteProducto(@PathVariable("idProducto") Integer id) {
        productoService.delete(id);
    }
}
