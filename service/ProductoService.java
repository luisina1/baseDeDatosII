package baseII.finall.service;

import baseII.finall.Iservice.IgenericService;
import baseII.finall.entity.Producto;
import baseII.finall.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IgenericService<Producto, Integer> {

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getById(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto entity) {
        return productoRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
}
