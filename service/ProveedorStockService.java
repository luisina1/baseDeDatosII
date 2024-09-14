package baseII.finall.service;

import baseII.finall.Iservice.IgenericService;
import baseII.finall.entity.StockProveedor;
import baseII.finall.repository.StockProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorStockService implements IgenericService<StockProveedor, Integer> {

    @Autowired
    private StockProveedorRepository proveedorStockRepository;

    @Override
    public List<StockProveedor> getAll() {
        return proveedorStockRepository.findAll();
    }

    @Override
    public Optional<StockProveedor> getById(Integer id) {
        return proveedorStockRepository.findById(id);
    }

    @Override
    public StockProveedor save(StockProveedor entity) {
        return proveedorStockRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
         proveedorStockRepository.deleteById(id);
    }
}