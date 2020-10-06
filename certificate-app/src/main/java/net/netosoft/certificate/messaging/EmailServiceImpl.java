package net.netosoft.certificate.messaging;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import net.netosoft.certificate.model.EmailTemplate;
import net.netosoft.certificate.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
public class EmailServiceImpl{
	private static final Logger LOGGER =
			LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${spring.mail.username}")
	private String from;
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration configuration;

	public void sendMessageWithAttachment(
			EmailTemplate emailTmpl,
			Person person,
			File attachment){
		try{
			
			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from);
			helper.setTo(person.getEmail());
			helper.setSubject(emailTmpl.getSubject());

			// html template
			Map<String, Object> mailModel = new HashMap<>();
			mailModel.put("personName", person.getName());
			mailModel.put("eventName", emailTmpl.getEventName());
			String body = buildEmailBody(emailTmpl.getTemplateName(), mailModel);
			helper.setText(body, true);

			FileSystemResource file = new FileSystemResource(attachment);
			helper.addAttachment(file.getFilename(), file);

			mailSender.send(helper.getMimeMessage());

			LOGGER.info("Email sent to {} - {}", person.getName(), person.getEmail());
		}catch(MessagingException ex){
			ex.printStackTrace(System.err);
		}
	}

	private String buildEmailBody(String template, Map<String, Object> model){
		StringBuilder body = new StringBuilder();
		try{
			body.append(
					FreeMarkerTemplateUtils.processTemplateIntoString(
							configuration.getTemplate(template),
							model
					));
		}catch(IOException | TemplateException ex){
			LOGGER.error(ex.getMessage(), ex);
		}
		return body.toString();
	}

}
