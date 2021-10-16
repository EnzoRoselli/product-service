package mymarket.product.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import mymarket.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mymarket.product.commons.utils.ParametersDefaultValue.CLASIFICATIONS;

@RequestMapping("products")
@RestController
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        productService.deleteById(id);
    }

    @GetMapping("{id}")
    public Product getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @GetMapping
    public List<Product> getByClasificationsAndName(@RequestParam(required = false, defaultValue = CLASIFICATIONS) List<Clasifications> clasifications,
                                                    @RequestParam(required = false, defaultValue = "") String name) {
        return productService.getByClasificationsAndName(clasifications, name);
    }
}
