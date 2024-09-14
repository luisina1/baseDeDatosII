package baseII.finall.service;

import baseII.finall.Iservice.IgenericService;
import baseII.finall.entity.Proveedor;
import baseII.finall.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService implements IgenericService<Proveedor, Integer> {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> getAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> getById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Proveedor save(Proveedor entity) {
        return proveedorRepository.save(entity);
    }

    @Override
    public void delete(Integer integer) {
          proveedorRepository.deleteById(integer);
    }
}
