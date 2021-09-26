package mymarket.product.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import mymarket.product.exceptions.ProductNotFoundException;
import mymarket.product.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1, product2;

    private List<Product> productList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ExceptionController())
                .build();

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
    public void save_ExpectedValues_Ok() throws Exception {
        //given
        given(productService.save(any())).willReturn(product1);

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product1))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(productService).should().save(product1);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(product1));
    }

    @Test
    public void save_MissingValues_DataIntegrityViolationException() throws Exception {
        //given
        given(productController.save(any())).willThrow(new DataIntegrityViolationException(""));

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product1))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(productService).should().save(product1);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteById_ExpectedValues_Ok() throws Exception {
        //given
        willDoNothing().given(productService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/products/4")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(productService).should().deleteById(4L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void deleteById_NonexistentId_EmptyResultDataAccessException() throws Exception {
        //given
        willThrow(new EmptyResultDataAccessException(0)).given(productService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/products/150")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(productService).should().deleteById(150L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getById_ExpectedValues_Ok() throws Exception {
        //given
        given(productService.getById(anyLong())).willReturn(product1);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/products/4")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(productService).should().getById(4L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(product1));
    }

    @Test
    public void getById_NonexistentId_ProductNotFoundException() throws Exception {
        //given
        BDDMockito.willThrow(new ProductNotFoundException("")).given(productService).getById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/products/150")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(productService).should().getById(150L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getByClasificationsAndName_ExpectedValues_Ok() throws Exception {
        //given
        given(productService.getByClasificationsAndName(anyList(), anyString())).willReturn(productList);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/products")
                .param("clasifications", "Almacen,Limpieza")
                .param("name", "Lavandina")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        then(productService).should().getByClasificationsAndName(
                Arrays.asList(Clasifications.Almacen, Clasifications.Limpieza), "Lavandina");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(productList));
    }

    @Test
    public void getByClasificationsAndName_EmptyValues_Ok() throws Exception {
        //given
        given(productService.getByClasificationsAndName(anyList(), anyString())).willReturn(productList);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        then(productService).should().getByClasificationsAndName(Arrays.asList(Clasifications.values()), "");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(productList));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
