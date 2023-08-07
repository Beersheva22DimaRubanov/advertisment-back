package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.model.ProductModel;
import telran.spring.service.ProductServiceImpl;

@SpringBootTest(classes = {ProductServiceImpl.class})
class ProductServiceImplTest {

	@Autowired
	ProductServiceImpl service;

	@Test
	void productServiceImplRightFlow() {
		ProductModel res = new ProductModel();
		res.setCategory("Test");
		res.setData(null);
		res.setId(0);
		res.setName("Test");
		res.setPrice(0f);
		assertEquals(res, service.addAnouncement(res));
	}
}
