package baseII.finall.controller;

import baseII.finall.entity.Venta;
import baseII.finall.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> getAll() {
        return ventaService.getAll();
    }

    @GetMapping("/{idVenta}")
    public Optional<Venta> getById(@PathVariable("idVenta") Integer id) {
        return ventaService.getById(id);
    }

    @PostMapping
    public Venta save(@RequestBody Venta venta) {
        return ventaService.save(venta);
    }

    @PutMapping("/{idVenta}")
    public ResponseEntity<Venta> updateVenta(@PathVariable("idVenta") Integer id, @RequestBody Venta venta) {
        Optional<Venta> optionalVenta = ventaService.getById(id);
        if (optionalVenta.isPresent()) {
            try {
                Venta vta = optionalVenta.get();
                vta.setNombre(venta.getNombre());
                vta.setPrecio(venta.getPrecio());
                vta.setCantidadTickets(venta.getCantidadTickets());
                Venta updateVenta = ventaService.save(vta);
                return ResponseEntity.ok(updateVenta);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{idVenta}")
    public void delete(@PathVariable("idVenta") Integer id) {
        ventaService.delete(id);
    }
}


