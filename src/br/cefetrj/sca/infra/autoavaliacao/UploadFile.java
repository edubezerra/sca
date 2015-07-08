package br.cefetrj.sca.infra.autoavaliacao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class UploadFile {

	String saveFile = "/tmp";
	protected Logger logger = Logger.getLogger(UploadFile.class.getName());

	public File receberArquivo(HttpServletRequest req) {
		try {
			boolean ismultipart = ServletFileUpload.isMultipartContent(req);
			if (!ismultipart) {
				return null;
			} else {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;

				items = upload.parseRequest(req);

				if(items.size() != 1){
					throw new RuntimeException("Apenas uma planilha é esperada.");
				}
				
				Iterator<FileItem> itr = items.iterator();
				if(itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (item.isFormField()) {

					} else {
						String itemname = item.getName();
						if ((itemname == null || itemname.equals(""))) {
							throw new RuntimeException("Nome inválido para planilha.");
						}
						String filename = FilenameUtils.getName(itemname);
						File f = checkExist(filename);
						item.write(f);
						return f;
					}
				}
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Planilha não pode ser importada.", e);
			throw new RuntimeException("Planilha não pode ser importada.", e);
		}
	}

	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		try {
			boolean ismultipart = ServletFileUpload.isMultipartContent(req);
			if (!ismultipart) {

			} else {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;

				try {
					items = upload.parseRequest(req);
				} catch (Exception e) {
				}
				Iterator<FileItem> itr = items.iterator();
				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (item.isFormField()) {

					} else {
						String itemname = item.getName();
						if ((itemname == null || itemname.equals(""))) {
							continue;
						}
						String filename = FilenameUtils.getName(itemname);
						File f = checkExist(filename);
						item.write(f);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Planilha não pode ser importada.", e);
		} finally {
			out.close();
		}
	}

	private File checkExist(String fileName) {
		File f = new File(saveFile + "/" + fileName);

		if (f.exists()) {
			StringBuffer sb = new StringBuffer(fileName);
			sb.insert(sb.lastIndexOf("."), "-" + new Date().getTime());
			f = new File(saveFile + "/" + sb.toString());
		}
		return f;
	}

}