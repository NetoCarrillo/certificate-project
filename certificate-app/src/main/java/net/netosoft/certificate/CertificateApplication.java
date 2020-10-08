package net.netosoft.certificate;

import net.netosoft.certificate.app.CertificateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CertificateApplication implements CommandLineRunner{

	@Autowired
	private CertificateFactory factory;

	public void run(String... args) throws Exception{
		factory.sendCertificates("D:/constancias/interprepas/interprepas.xlsx");
	}

	public static void main(String[] args){
		SpringApplication.run(CertificateApplication.class, args);
	}
}
