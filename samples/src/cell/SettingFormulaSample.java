package cell;

import com.jniwrapper.win32.jexcel.Application;
import com.jniwrapper.win32.jexcel.Cell;
import com.jniwrapper.win32.jexcel.GenericWorkbook;
import com.jniwrapper.win32.jexcel.Worksheet;

import javax.swing.*;
import java.util.List;

/**
 * This sample demonstrates how to set a formula containing worksheet names to the cell
 */
public class SettingFormulaSample
{
    public static void main(String[] args) throws Exception
    {
        Application application = new Application();

        GenericWorkbook workbook = application.createWorkbook(null);
        workbook.addWorksheet("First worksheet");
        List worksheets = workbook.getWorksheets();
        int size = worksheets.size();

        //Setting non-default names
        workbook.getWorksheet(size - 1).setName("Last worksheet");
        workbook.getWorksheet(size).setName("Average worksheet");

        //Filling worksheets with data
        for (int i = 0; i < size - 1; i++)
        {
            Worksheet worksheet = (Worksheet) worksheets.get(i);
            Cell cell = worksheet.getCell("A1");
            cell.setValue((i + 1) * 111.11);
        }

        //Retrieving worksheet names
        String firstDataSheetName = workbook.getWorksheet(1).getName();
        String lastDataSheetName = workbook.getWorksheet(size - 1).getName();
        Worksheet lastWorksheet = workbook.getWorksheet(size);

        Cell cell = lastWorksheet.getCell("A1");

        //Worksheet names may contain spaces, so single quotes are used in formula
        String averageFormula = "=AVERAGE('" + firstDataSheetName + ":" + lastDataSheetName + "'!A1)";
        //Setting formula to cell
        cell.setValue(averageFormula);

        //Saving results to see them later
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            workbook.saveCopyAs(fileChooser.getSelectedFile());
        }
        application.close();
    }
}
