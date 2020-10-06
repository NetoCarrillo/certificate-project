package net.netosoft.certificate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateTemplate{

	private String template;
	private String name;
	private File outputDir;
	private String outputFileName;
	private String publicDir;
	private EmailTemplate emailTemplate;
	
}
