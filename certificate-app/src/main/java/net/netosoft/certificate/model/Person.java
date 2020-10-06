package net.netosoft.certificate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person{
	
	private String name;
	private String email;
	private int serial;
	
}
