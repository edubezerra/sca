package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
	@Autowired
	private DocumentoProfessorRepositorio repositorio;

	public void saveDocumentoProfessor(String nome, String categoria, MultipartFile file, Professor professor) throws IOException, IllegalArgumentException {
		
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException(
					"Erro: Comprovante não encontrado. "
							+ "Por favor, forneça um documento válido.");
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
