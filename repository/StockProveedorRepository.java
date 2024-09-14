package baseII.finall.repository;

import baseII.finall.entity.StockProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockProveedorRepository extends JpaRepository<StockProveedor, Integer> {
}
