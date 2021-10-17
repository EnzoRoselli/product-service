package mymarket.product.service;

import mymarket.exception.commons.exception.NotFoundException;
import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import mymarket.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id " + id + " not found."));
    }

    public List<Product> getByClasificationsAndName(List<Clasifications> clasifications, String name) {
        return productRepository.findByClasificationInAndNameContaining(clasifications, name);
    }
}
