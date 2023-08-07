package telran.spring.service;

import java.util.List;

import telran.spring.model.ProductModel;

public interface ProductService {
	ProductModel addAnouncement(ProductModel product);
	ProductModel getById(int id);
	List<ProductModel> getAll();
	List<ProductModel> getByCategory(String category);
	ProductModel deleteAnouncement(int id);
	ProductModel updateAnouncement(int id, ProductModel updatedProduct);
	List<ProductModel> getProductsAbovePrice(float price);
	void save(String path);
	void restore(String path);
}
