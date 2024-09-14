package baseII.finall.service;

import baseII.finall.Iservice.IgenericService;
import baseII.finall.entity.Venta;
import baseII.finall.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IgenericService<Venta, Integer> {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> getAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> getById(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta save(Venta entity) {
        return ventaRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
      ventaRepository.deleteById(id);
    }
}
