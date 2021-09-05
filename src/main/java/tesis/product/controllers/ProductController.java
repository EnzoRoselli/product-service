package tesis.product.controllers;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tesis.product.models.Product;
import tesis.product.repositories.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static tesis.product.utils.ParametersDefaultValue.CLASIFICATIONS;

@RequestMapping
@RestController
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repo;

    @GetMapping("name/{name}")
    public Product getByName(@PathVariable("name") String name) {
        return repo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product with given name does not exist."));
    }

    @GetMapping("{id}")
    public Product getById(@PathVariable("id") Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with given id does not exist."));
    }
    @GetMapping("")
    public List<Product> getByFilters(@RequestParam(required = false,defaultValue = CLASIFICATIONS) List<String> clasificaciones,
                                     @RequestParam(required = false,defaultValue = "") String nombre) {
        return repo.findByClasificationInAndNameContaining(clasificaciones, nombre);
    }
    @GetMapping("/ids")
    public List<String> getIdsByFilters(@RequestParam(required = false,defaultValue = CLASIFICATIONS) List<String> clasificaciones,
                                     @RequestParam(required = false,defaultValue = "") String nombre) {
        return repo.findByClasificationInAndNameContaining(clasificaciones,nombre)
                .stream()
                .map(p->String.valueOf(p.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("names")
    public List<String> getAllNames() {
        return repo.findAll().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Product save(@RequestBody @NotNull Product product) {
        return repo.save(product);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        repo.deleteById(id);
    }
}
