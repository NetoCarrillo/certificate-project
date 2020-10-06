package net.netosoft.certificate.builder;

import net.netosoft.certificate.model.Person;

import java.io.File;

public interface CertificateBuilder{
	void buildCertificate(File outputFile, Person Person);
}
