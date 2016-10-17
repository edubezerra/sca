package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.DocumentoProfessor;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.DocumentoProfessorRepositorio;

@Service
@Transactional
public class DocumentoProfessorService {
	
	protected static String allowedTypes[] = {
		// Plaintext
		"text/plain",
		// Images
		"image/jpeg",
		"image/jpg",
		"image/png",
		// PDF
		"application/pdf",
		// Word
		"application/msword",
		"application/vnd.openxmlformats-officedocument.wordprocessingml.template",
		"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
	};
	
	
	@Autowired
	private DocumentoProfessorRepositorio repositorio;

	protected boolean isTypeAllowed(MultipartFile file) {
		
		String type = file.getContentType();
		
		for(String allowedType : allowedTypes) {
			if(type.equals(allowedType))
				return true;
		}
		
		return false;
	}
	
	
	public void saveDocumentoProfessor(String nome, String categoria, MultipartFile file, Professor professor) throws IOException, IllegalArgumentException {
		
		if (file == null || file.isEmpty() || !this.isTypeAllowed(file)) {
			throw new IllegalArgumentException("Erro: Documento inválido.");
		}
		
		Comprovante doc = new Comprovante(file.getContentType(),
				file.getBytes(), file.getOriginalFilename());
		
		DocumentoProfessor documentoProfessor = new DocumentoProfessor(nome, categoria, doc, new Date(), professor);
		
		repositorio.save(documentoProfessor);
	}
	
	public void deleteDocumentoProfessor(Long id) throws Exception {
		this.deleteDocumentoProfessor(repositorio.findDocumentoProfessorById(id));
	}
	
	public void deleteDocumentoProfessor(DocumentoProfessor doc) throws Exception {
		
		if(doc == null)
			throw new Exception("Documento não encontrado!");
	
		doc.setProfessor(null);
		repositorio.delete(doc);
	}
}
