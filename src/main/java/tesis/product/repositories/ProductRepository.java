package tesis.product.repositories;

import net.bytebuddy.build.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tesis.product.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    Optional<Product> findByName(String name);
    List<Product> findByClasificationInAndNameContaining(List<String> clasifications,String name);

}
