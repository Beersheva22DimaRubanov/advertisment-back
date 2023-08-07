package telran.spring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.exceptions.NotFoundException;
import telran.spring.model.ProductModel;
import telran.spring.service.ProductService;
import telran.spring.service.ProductServiceImpl;

@RestController
@Slf4j
@RequestMapping("products")
@RequiredArgsConstructor
@CrossOrigin
public class ServiceController {
	final ProductService service;
	private static  final String DATA_FILE = "data";
	
	@PostMapping
	ProductModel addAnouncement(@RequestBody ProductModel product) {
		log.debug("Add product to maps");
		return service.addAnouncement(product);
	}
	@GetMapping
	List<ProductModel> getAll() {
		log.debug("Get all products from idMap");
		return service.getAll();
	}
	
	@GetMapping("category/{categoryName}")
	List<ProductModel> getByCategory(@PathVariable(name= "categoryName") @NotEmpty String category){
		log.debug("Get all products with category: {}", category);
		return service.getByCategory(category);
	}
	
	@GetMapping("id/{idNum}")
	ProductModel getbyId(@PathVariable(name = "idNum") @NotEmpty int id ) {
		ProductModel product = service.getById(id);
		ProductModel res = null;
		log.debug("Get product with id {}", id);
		return service.getById(id);
		
	}
	
	
	@DeleteMapping("id/{idNum}")
	ProductModel deletebyId(@PathVariable(name = "idNum") @NotEmpty int id ) {
		log.debug("Delete product with id {}", id );
		return service.deleteAnouncement(id);
	}
	
	@PutMapping("id/{idNum}")
	ProductModel updateById(@PathVariable(name = "idNum") @NotEmpty int id, @RequestBody ProductModel product) {
		log.debug("Update product with id {}", id);
		return service.updateAnouncement(id, product);
	}
	
	@GetMapping("price/{price}")
	List<ProductModel> getOverPrice(@PathVariable(name = "price") @NotEmpty float price){
		log.debug("Get all products with price above: {}", price);
		return service.getProductsAbovePrice(price);
	}
	
	
	@PostConstruct
	void init() {
		service.restore(DATA_FILE);
		log.info("Restored data from file {}", DATA_FILE);
	}
	
	@PreDestroy
	void safe() {
		service.save(DATA_FILE);
		log.info("Saved data to file {}", DATA_FILE);
	}
	
}
