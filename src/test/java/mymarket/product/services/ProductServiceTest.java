package mymarket.product.services;

import mymarket.product.exceptions.ProductNotFoundException;
import mymarket.product.models.Product;
import mymarket.product.models.enums.Clasifications;
import mymarket.product.repositories.ProductRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static mymarket.product.utils.ParametersDefaultValue.CLASIFICATIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1, product2;

    private List<Product> productList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        product1 = Product.builder().id(1L).name("Lavandina")
                .clasification(Clasifications.Limpieza)
                .description("Lavandina generica 250ml").build();

        product2 = Product.builder().id(1L).name("Lavandina")
                .clasification(Clasifications.Limpieza)
                .description("Lavandina generica 500ml").build();

        productList.add(product1);
        productList.add(product2);
    }

    @Test
    public void save_ExpectedValues_Ok() {
        //given
        given(productRepository.save(any())).willReturn(product1);

        //when
        Product productFromRepo = productService.save(product1);

        //then
        then(productRepository).should().save(product1);
        assertThat(productFromRepo).isNotNull();
        assertThat(productFromRepo).isEqualTo(product1);
    }

    @Test
    public void deleteById_ExpectedValues_Ok() {
        //given
        willDoNothing().given(productRepository).deleteById(anyLong());

        //when
        productService.deleteById(1L);
        productService.deleteById(2L);
        productService.deleteById(3L);

        //then
        then(productRepository).should(times(3)).deleteById(anyLong());
    }

    @Test
    public void getById_ExpectedValues_Ok() {
        //given
        Optional<Product> productOptional = Optional.of(product1);
        given(productRepository.findById(anyLong())).willReturn(productOptional);

        //when
        Product userFromRepository = productService.getById(1L);

        //then
        then(productRepository).should().findById(1L);
        assertThat(userFromRepository).isNotNull();
        assertThat(userFromRepository).isEqualTo(productOptional.get());
    }

    @Test
    public void getById_NonexistentId_ProductNotFoundException() {
        //given
        BDDMockito.willThrow(new ProductNotFoundException("Product with id 1 not found.")).given(productRepository).findById(anyLong());

        //when
        when(() -> productService.getById(1L));

        //then
        BDDAssertions.then(caughtException())
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product with id 1 not found.")
                .hasNoCause();
    }

    @Test
    public void getByClasificationsAndName_ExpectedValues_Ok() {
        //given
        given(productRepository.findByClasificationInAndNameContaining(anyList(), anyString()))
                .willReturn(productList);

        //when
        List<Product> products = productService.getByClasificationsAndName(Arrays.asList(CLASIFICATIONS.split(",")), "Lavandina");

        //then
        then(productRepository).should().findByClasificationInAndNameContaining(Arrays.asList(CLASIFICATIONS.split(",")), "Lavandina");
        assertThat(products).isNotNull();
        assertThat(products).hasSize(2);
        assertThat(products).isEqualTo(productList);
    }
}
