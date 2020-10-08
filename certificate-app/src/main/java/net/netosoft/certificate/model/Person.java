package net.netosoft.certificate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person{
	
	private String name;
	private String email;
	private int serial;
	private Map<String, String> params;
	
}
