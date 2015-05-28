package misc;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ReadExcel {

	private String inputFile;

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void read() throws IOException {
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			w = Workbook.getWorkbook(inputWorkbook, ws);

			// Obtém a primeira aba da planilha.
			Sheet sheet = w.getSheet(0);

			// Percorre as 10 primeiras linhas das 10 primeiras colunas.
			for (int j = 0; j < Math.min(10, sheet.getColumns()); j++) {
				for (int i = 0; i < Math.min(10, sheet.getRows()); i++) {
					Cell cell = sheet.getCell(j, i);
					CellType type = cell.getType();
					if (type == CellType.LABEL) {
						System.out.println("I got a label "
								+ cell.getContents());
					}

					if (type == CellType.NUMBER) {
						System.out.println("I got a number "
								+ cell.getContents());
					}

				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ReadExcel test = new ReadExcel();
		test.setInputFile("./Matrícula-DEPIN-2014-1.xls");
		test.read();
	}

}