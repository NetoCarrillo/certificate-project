package net.netosoft.certificate.data;

import net.netosoft.certificate.model.CertificateTemplate;
import net.netosoft.certificate.model.EmailTemplate;
import net.netosoft.certificate.model.Person;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class ExcelDataProvider{
	private static final Logger LOGGER =
			LoggerFactory.getLogger(ExcelDataProvider.class);

	@Value("${net.netosoft.certificates.root}")
	private String root;

	@Value("${net.netosoft.certificates.input.dir}")
	private String templateDir;

	@Value("${net.netosoft.certificates.template.name}")
	private String templateName;

	@Value("${net.netosoft.certificates.email.template}")
	private String emailTemplateName;

	@Value("${net.netosoft.certificates.output.file}")
	private String outputFileName;

	@Value("${net.netosoft.certificates.output.dir}")
	private String outputDir;

	@Value("${net.netosoft.certificates.event.definition}")
	private String defSheetName;

	@Value("${net.netosoft.certificates.event.persons}")
	private String personSheetName;

	public CertificateTemplate readCertificateTemplate(File inputFile){
		CertificateTemplate cert = new CertificateTemplate();
		try(FileInputStream excelFile = new FileInputStream(inputFile);
			Workbook workbook = new XSSFWorkbook(excelFile);){

			Sheet sheet = workbook.getSheet(defSheetName);
			String eventName = sheet.getRow(0).getCell(1).getStringCellValue();
			String eventShortName = sheet.getRow(1).getCell(1).getStringCellValue();
			String eventFileName = sheet.getRow(2).getCell(1).getStringCellValue();
			String emailSubject = sheet.getRow(3).getCell(1).getStringCellValue();

			File rootFile = new File(root);
			cert.setTemplate(root + "/" + eventFileName + "/" + templateDir + "/" + templateName);
			cert.setName(eventShortName);
			cert.setOutputDir(Paths.get(root, eventFileName, outputDir).toFile());
			cert.setOutputFileName(outputFileName);
			cert.setPublicDir(root + "/" + eventFileName + "/" + templateDir);

			EmailTemplate emailTemplate = new EmailTemplate();
			emailTemplate.setEventName(eventName);
			emailTemplate.setSubject(emailSubject);
			emailTemplate.setTemplateName(eventFileName + "/" + templateDir + "/" + emailTemplateName);

			cert.setEmailTemplate(emailTemplate);
		}catch(IOException ex){
			LOGGER.error(ex.getMessage(), ex);
		}

		return cert;
	}

	public List<Person> readPersons(File inputFile){
		List<Person> persons = new ArrayList<>();
		try(FileInputStream excelFile = new FileInputStream(inputFile);
				Workbook workbook = new XSSFWorkbook(excelFile);){

			Sheet datatypeSheet = workbook.getSheet(personSheetName);
			Iterator<Row> iterator = datatypeSheet.iterator();

			List<String> headers = getHeaders(iterator.next());

			int count = 1;
			while(iterator.hasNext()){
				Row row = iterator.next();
				
				Person person = new Person();

				person.setName(row.getCell(0).getStringCellValue());
				person.setEmail(row.getCell(1).getStringCellValue());
				person.setSerial(count++);
				person.setParams(getPersonParams(row, headers));

				persons.add(person);
			}
		}catch(IOException ex){
			LOGGER.error(ex.getMessage(), ex);
		}
		
		return persons;
	}

	private List<String> getHeaders(Row headerRow){
		List<String> headers = new ArrayList<>();

		Iterator<Cell> iterator = headerRow.iterator();
		while(iterator.hasNext()){
			headers.add(iterator.next().getStringCellValue());
		}
		return headers;
	}

	private Map<String, String> getPersonParams(Row row, List<String> headers){
		Map<String, String> params = new HashMap<>();
		for(int i = 2; i < headers.size(); i++){
			params.put(headers.get(i), row.getCell(i).getStringCellValue());
		}
		return params;
	}

}
