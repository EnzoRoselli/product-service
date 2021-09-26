package mymarket.product.repositories;

import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByClasificationInAndNameContaining(List<Clasifications> clasifications, String name);
}
