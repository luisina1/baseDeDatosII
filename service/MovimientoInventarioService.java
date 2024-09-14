package baseII.finall.service;

import baseII.finall.Iservice.IgenericService;
import baseII.finall.entity.MovimientoInventario;
import baseII.finall.repository.MovimientoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoInventarioService implements IgenericService<MovimientoInventario, Integer> {

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    public List<MovimientoInventario> getAll() {
        return movimientoInventarioRepository.findAll();
    }

    @Override
    public Optional<MovimientoInventario> getById(Integer id) {
        return movimientoInventarioRepository.findById(id);
    }

    @Override
    public MovimientoInventario save(MovimientoInventario entity) {
        return movimientoInventarioRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        movimientoInventarioRepository.deleteById(id);
    }
}
