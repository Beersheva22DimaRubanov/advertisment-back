package telran.spring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import telran.spring.exceptions.NotFoundException;
import telran.spring.model.ProductModel;

@Service
@Slf4j
public class ProductServiceImpl implements Serializable, ProductService {
	
	private static final long serialVersionUID = 1L;
	private static final int MIN = 1000000; 
	private static final int MAX = 10000000; 
	
	private HashMap<Integer, ProductModel> idMap;
	private TreeMap<Float, HashSet<ProductModel>> priceMap;
	private HashMap<String, HashSet<ProductModel>> categoryMap;
	
	public ProductServiceImpl() {
		this.idMap = new HashMap<Integer, ProductModel>();
		this.categoryMap = new HashMap<String, HashSet<ProductModel>>();
		this.priceMap = new TreeMap<Float, HashSet<ProductModel>>();
	}
	@Override
	public ProductModel addAnouncement(ProductModel product) {
		synchronized (idMap) {
			int id = getId();
			if(!idMap.isEmpty()) {
				while(idMap.containsKey(id)) {
					id = getId();
				};
			}
			
			product.setId(id);
			idMap.put(id, product);
			categoryMap.computeIfAbsent(product.getCategory(), k-> new HashSet<>()).add(product);
			priceMap.computeIfAbsent(product.getPrice(), k-> new HashSet<>()).add(product);
		}
		return product;
	}

	private Integer getId() {
		return (int) (Math.random() * ((MAX - MIN) + 1));
	}
	
	@Override
	public ProductModel getById(int id) {
		return idMap.get(id);
	}
	
	@Override
	public List<ProductModel> getAll(){
		log.debug("Category map: {}", categoryMap);
		log.debug("Id map: {}", idMap);
		log.debug("Price map: {}", priceMap);

		return new ArrayList<ProductModel>(idMap.values());
		
	}
	
	@Override
	public List<ProductModel> getByCategory(String category){
		HashSet<ProductModel> res = new HashSet<ProductModel>();
		if(categoryMap.containsKey(category)) {
			res = categoryMap.get(category);
			log.debug("Category map: {}", categoryMap.get(category));
		}
		return new ArrayList<ProductModel>(res);
	}
	
	@Override
	public ProductModel deleteAnouncement(int id) throws NotFoundException {
		ProductModel res = null;
		if(idMap.containsKey(id)) {
			res = idMap.get(id);
			categoryMap.get(res.getCategory()).remove(res);
			priceMap.get(res.getPrice()).remove(res);
			idMap.remove(id);
		} else {
			throw new NotFoundException("Not found");
		}
		return res;
	}
	
	@Override
	public ProductModel updateAnouncement(int id, ProductModel updatedProduct) {
		ProductModel res = null;
		if(idMap.containsKey(id)) {
			res = idMap.get(id);
			categoryMap.get(res.getCategory()).remove(res);
			categoryMap.get(res.getCategory()).add(updatedProduct);
			priceMap.get(res.getPrice()).remove(res);
			priceMap.get(res.getPrice()).add(updatedProduct);
			idMap.replace(id, updatedProduct);
		}
		return updatedProduct;
	}
	
	@Override
	public List<ProductModel> getProductsAbovePrice(float price){
		return priceMap.subMap(0f, true, price, true)
				.values().stream().flatMap(Set::stream).toList();
	}
	
	@Override
	public void save(String path) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path))) {
			output.writeObject(idMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void restore(String path) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(path))) {
			idMap = (HashMap<Integer, ProductModel>) input.readObject();
			
			idMap.entrySet().stream()
					.forEach(entry -> categoryMap.computeIfAbsent(entry.getValue().getCategory(), k-> new HashSet<>()).add(entry.getValue()));
					
			idMap.entrySet().stream()
			.forEach(entry -> priceMap.computeIfAbsent(entry.getValue().getPrice(), k-> new HashSet<>()).add(entry.getValue()));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
