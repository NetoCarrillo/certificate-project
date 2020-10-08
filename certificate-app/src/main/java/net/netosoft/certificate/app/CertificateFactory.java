package net.netosoft.certificate.app;

import net.netosoft.certificate.builder.CertificateBuilder;
import net.netosoft.certificate.builder.CertificateJasperBuilder;
import net.netosoft.certificate.data.ExcelDataProvider;
import net.netosoft.certificate.messaging.EmailServiceImpl;
import net.netosoft.certificate.model.CertificateTemplate;
import net.netosoft.certificate.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateFactory{
	private static final Logger LOGGER =
			LoggerFactory.getLogger(CertificateFactory.class);

	@Autowired
	private ExcelDataProvider dataProvider;

	@Autowired
	private EmailServiceImpl emailService;

	public void sendCertificates(String certFile){
		File definitionFile = new File(certFile);

		// Load certificate template definition
		CertificateTemplate certTemplate =
				dataProvider.readCertificateTemplate(definitionFile);
		LOGGER.info("Certificates will be placed on {}", certTemplate.getOutputDir());

		// Load persons
		List<Person> persons = dataProvider.readPersons(definitionFile);

		// Generate certificates
		Map<Integer, File> certs = generateCertificates(certTemplate, persons);

		// Send email with certificate
		for(Person p : persons){
			emailService.sendMessageWithAttachment(
					certTemplate.getEmailTemplate(), p, certs.get(p.getSerial()));
		}

	}
	
	public Map<Integer, File> generateCertificates(CertificateTemplate template, List<Person> persons){
		
		File outputRoot = new File(template.getOutputDir(), template.getName());
		outputRoot.mkdirs();
		
		CertificateBuilder builder = new CertificateJasperBuilder(template.getTemplate(), template.getPublicDir());

		Map<Integer, File> certs = new HashMap<>();
		for(Person person : persons){
			File outputFile = new File(
					outputRoot,
					String.format(template.getOutputFileName(), person.getSerial(), template.getName()));
			builder.buildCertificate(outputFile, person);
			certs.put(person.getSerial(), outputFile);
		}

		return certs;
	}
}
