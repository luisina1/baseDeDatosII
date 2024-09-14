package baseII.finall.controller;

import baseII.finall.entity.Proveedor;
import baseII.finall.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> getAll() {
        return proveedorService.getAll();
    }

    @GetMapping("/{idProveedor}")
    public Optional<Proveedor> getById(@PathVariable("idProveedor") Integer id) {
        return proveedorService.getById(id);
    }

    @PostMapping
    public Proveedor save(@RequestBody Proveedor proveedor) {
        return proveedorService.save(proveedor);
    }

    @PutMapping("/{idProveedor}")
    public ResponseEntity<Proveedor> updateProveedor(@PathVariable("idProveedor") Integer id, @RequestBody Proveedor proveedor) {
        Optional<Proveedor> proveedorOptional = proveedorService.getById(id);

        if (proveedorOptional.isPresent()) {
            try {
                Proveedor proveedores = proveedorOptional.get();
                proveedor.setNombreCompleto(proveedores.getNombreCompleto());
                proveedor.setContacto(proveedores.getContacto());
                proveedor.setDireccion(proveedores.getDireccion());
                Proveedor updatedProveedor = proveedorService.save(proveedor);
                return ResponseEntity.ok(updatedProveedor);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("{idProveedor}")
    public void delete(@PathVariable("idProveedor") Integer id) {
        proveedorService.delete(id);
    }
}
