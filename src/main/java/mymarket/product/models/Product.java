package mymarket.product.models;

import lombok.*;
import mymarket.product.models.enums.Clasifications;

import javax.persistence.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    private String image;
    @Column(name = "clasification")
    private Clasifications clasification;
    @Column(name = "description")
    private String description;
}
