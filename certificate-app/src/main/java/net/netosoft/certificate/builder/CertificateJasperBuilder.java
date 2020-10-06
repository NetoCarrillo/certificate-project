package net.netosoft.certificate.builder;

import net.netosoft.certificate.model.Person;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class CertificateJasperBuilder implements CertificateBuilder{
	private static final Logger LOGGER =
			LoggerFactory.getLogger(CertificateJasperBuilder.class);

	private final String templateFile;
	private final String publicDir;
	private final NumberFormat numberFormatter;

	public CertificateJasperBuilder(String templateFile, String publicDir){
		this.templateFile = templateFile;
		this.publicDir = publicDir;
		numberFormatter = NumberFormat.getIntegerInstance();
		numberFormatter.setMinimumIntegerDigits(4);
		numberFormatter.setGroupingUsed(false);
	}
	
	@Override
	public void buildCertificate(File outputFile, Person params){
		try{
			JasperReport jasperDesign = JasperCompileManager.compileReport(templateFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperDesign,
					toMap(params),
					new JREmptyDataSource());

			OutputStream outputSteam = new FileOutputStream(outputFile);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputSteam);

		}catch(JRException | FileNotFoundException ex){
			LOGGER.error(ex.getMessage(), ex);
		}

	}
	
	private Map<String, Object> toMap(Person person){
		Map<String, Object> map = new HashMap<>();
		
		map.put("nombre", person.getName());
		map.put("folio", numberFormatter.format(person.getSerial()));
		map.put("publico", publicDir);
		
		return map;
	}
}
