package baseII.finall.controller;

import baseII.finall.entity.MovimientoInventario;
import baseII.finall.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventario")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @GetMapping
    public List<MovimientoInventario> getAll() {
        return movimientoInventarioService.getAll();
    }

    @GetMapping("/{idStock}")
    public Optional<MovimientoInventario> getById(@PathVariable("idStock") int idStock) {
        return movimientoInventarioService.getById(idStock);
    }

    @PostMapping
    public MovimientoInventario save(@RequestBody MovimientoInventario stock) {
        return movimientoInventarioService.save(stock);
    }

    @PutMapping("/{idStock}")
    public ResponseEntity<MovimientoInventario> updateStock(@PathVariable("idStock") Integer id, @RequestBody MovimientoInventario stock) {
        Optional<MovimientoInventario> stockOptional = movimientoInventarioService.getById(id);
        if (stockOptional.isPresent()) {
            try {
                MovimientoInventario st = stockOptional.get();
                st.setEntrada(stock.getEntrada());
                st.setSalida(stock.getSalida());
                st.setFecha(stock.getFecha());
                MovimientoInventario updateStock = movimientoInventarioService.save(st);
                return ResponseEntity.ok(updateStock);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{idStock}")
    public void deleteStock(@PathVariable("idStock") Integer idStock) {
        movimientoInventarioService.delete(idStock);
    }
}