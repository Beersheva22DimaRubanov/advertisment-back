package telran.spring.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@Data
public class ProductModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Integer id;
	
	@NotEmpty
	String category;
	
	@NotEmpty
	String name;
	
	@NotEmpty
	@Min(0)
	Float price;
	
	String data;
}
