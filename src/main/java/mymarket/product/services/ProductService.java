package mymarket.product.services;

import lombok.RequiredArgsConstructor;
import mymarket.product.exceptions.ProductNotFoundException;
import mymarket.product.models.Product;
import mymarket.product.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
    }

    public List<Product> getByClasificationsAndName(List<String> clasifications, String name) {
        return productRepository.findByClasificationInAndNameContaining(clasifications, name);
    }
}
