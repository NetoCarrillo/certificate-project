package net.netosoft.certificate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate{
	private String subject;
	private String eventName;
	private String templateName;
}
