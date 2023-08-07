package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import telran.spring.controller.ServiceController;
import telran.spring.model.ProductModel;
import telran.spring.service.ProductService;

@Service
class MockService implements ProductService {

	@Override
	public ProductModel addAnouncement(ProductModel product) {
		return product;
	}

	@Override
	public ProductModel getById(int id) {
		ProductModel res = new ProductModel();
		res.setCategory("Test");
		res.setData(null);
		res.setId(0);
		res.setName("Test");
		res.setPrice(0f);
		return res;
	}

	@Override
	public List<ProductModel> getAll() {
		ProductModel res = new ProductModel();
		res.setCategory("Test");
		res.setData(null);
		res.setId(0);
		res.setName("Test");
		res.setPrice(0f);
		ArrayList<ProductModel> list = new ArrayList<ProductModel>();
		list.add(res);
		return list;
	}

	@Override
	public List<ProductModel> getByCategory(String category) {
		ProductModel res = new ProductModel();
		res.setCategory(category);
		res.setData(null);
		res.setId(0);
		res.setName("Test");
		res.setPrice(0f);
		ArrayList<ProductModel> list = new ArrayList<ProductModel>();
		list.add(res);
		return list;
	}

	@Override
	public ProductModel deleteAnouncement(int id) {
		ProductModel res = new ProductModel();
		res.setCategory("Test");
		res.setData(null);
		res.setId(0);
		res.setName("Test");
		res.setPrice(0f);
		return res;
	}

	@Override
	public ProductModel updateAnouncement(int id, ProductModel updatedProduct) {

		return updatedProduct;
	}

	@Override
	public List<ProductModel> getProductsAbovePrice(float price) {
		ProductModel res = new ProductModel();
		res.setCategory("test");
		res.setData(null);
		res.setId(0);
		res.setName("Test");
		res.setPrice(0f);
		ArrayList<ProductModel> list = new ArrayList<ProductModel>();
		list.add(res);
		return list;
	}

	@Override
	public void save(String path) {
		
	}

	@Override
	public void restore(String path) {
	
	}
}

@WebMvcTest({ ServiceController.class, MockService.class })
class ControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	ProductModel product;

	String productsUrl = "http://localhost:8080/products";
	String categoryUrl = String.format("%s/category/test", productsUrl);
	String priceUrl = String.format("%s/price/100", productsUrl);

	@BeforeEach
	void setUp() {
		product = new ProductModel();
		product.setCategory("Test");
		product.setData(null);
		product.setId(0);
		product.setName("Test");
		product.setPrice(0f);
	}

	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc);
	}

	@Test
	void sendRightFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(product);
		String response = getRequestBas(messageJson).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("{\"id\":0,\"category\":\"Test\",\"name\":\"Test\",\"price\":0.0,\"data\":null}", response);
	}

	private ResultActions getRequestBas(String messageJson) throws Exception {
		return mockMvc.perform(post(productsUrl).contentType(MediaType.APPLICATION_JSON).content(messageJson))
				.andDo(print());
	}

	@Test
	void getAllRightFlow() throws Exception {
		String response = mockMvc.perform(get(productsUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertEquals("[{\"id\":0,\"category\":\"Test\",\"name\":\"Test\",\"price\":0.0,\"data\":null}]", response);
	}

	@Test
	void getByCategoryRightFlow() throws Exception {
		String response = mockMvc.perform(get(categoryUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertEquals("[{\"id\":0,\"category\":\"test\",\"name\":\"Test\",\"price\":0.0,\"data\":null}]", response);
	}

	@Test
	void getByPriceRightFlow() throws Exception {
		String response = mockMvc.perform(get(priceUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertEquals("[{\"id\":0,\"category\":\"test\",\"name\":\"Test\",\"price\":0.0,\"data\":null}]", response);
	}

	@Test
	void deleteRightFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(product);
		String response = getRequestBas(messageJson).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("{\"id\":0,\"category\":\"Test\",\"name\":\"Test\",\"price\":0.0,\"data\":null}", response);
		String deleteResponse = mockMvc.perform(delete(productsUrl + "/id/0")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertEquals("{\"id\":0,\"category\":\"Test\",\"name\":\"Test\",\"price\":0.0,\"data\":null}", deleteResponse);
	}

}
