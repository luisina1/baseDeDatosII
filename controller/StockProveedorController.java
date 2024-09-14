package baseII.finall.controller;

import baseII.finall.entity.StockProveedor;
import baseII.finall.service.ProveedorStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/proveedorStock")
public class StockProveedorController {

    @Autowired
    private ProveedorStockService proveedorStockService;

    @GetMapping
    public List<StockProveedor> getAll() {
        return proveedorStockService.getAll();
    }

    @GetMapping("/{idProveedorStock}")
    public Optional<StockProveedor> getById(@PathVariable("idProveedorStock") Integer id) {
        return proveedorStockService.getById(id);
    }

    @PostMapping
    public StockProveedor save(@RequestBody StockProveedor proveedorStock) {
        return proveedorStockService.save(proveedorStock);
    }

    @PutMapping("/{idProveedorStock}")
    public ResponseEntity<StockProveedor> updateProveedorStock(@PathVariable("idProveedorStock") Integer id, @RequestBody StockProveedor proveedorStock) {
        Optional<StockProveedor> proveedorStockOptional = proveedorStockService.getById(id);
        if (proveedorStockOptional.isPresent()) {
            try {
                StockProveedor ps = proveedorStockOptional.get();
                ps.setEntrada(proveedorStock.getEntrada());
                ps.setSalida(proveedorStock.getSalida());
                ps.setFecha(proveedorStock.getFecha());
                StockProveedor updateProveedorStock = proveedorStockService.save(ps);
                return ResponseEntity.ok(updateProveedorStock);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{idProveedorStock}")
    public void delete(@PathVariable("idProveedorStock") Integer id) {
        proveedorStockService.delete(id);
    }
}
