package net.netosoft.certificate.config;

import net.netosoft.certificate.messaging.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class Config{
	@Value("${net.netosoft.certificates.root}")
	private String root;

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration(){
		FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
		fmConfigFactoryBean.setTemplateLoaderPath("file:" + root);
		return fmConfigFactoryBean;
	}
}
