package com.rapidapp.l2redis2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SpringBootApplication
public class L2Redis2Application {

	public static void main(String[] args) {
		SpringApplication.run(L2Redis2Application.class, args);
	}

}
@RestController
@RequestMapping("/products")
class ProdutController{
	private final ProductRepository productRepository;

	public ProdutController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	@GetMapping
	public Iterable<Product> getProducts(){
		return productRepository.findAll();
	}
	@GetMapping("/{id}")
	public Product getProducts(@PathVariable Long id){
		Optional<Product> product = productRepository.findById(id);

		return product.get();
	}
	@PostMapping
	public Product createProduct(@RequestBody CreateProductRequest request){
		Product product = Product.builder()
				.name(request.getName())
				.description(request.getDescription())
				.price(request.getPrice())
				.build();
		return productRepository.save(product);
	}
}


@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cache(region = "products", usage = CacheConcurrencyStrategy.READ_WRITE)
class Product{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private String price;


}
@Data
class CreateProductRequest {
	private String name;
	private String description;
	private String price;
}
interface ProductRepository extends JpaRepository<Product,Long>{}