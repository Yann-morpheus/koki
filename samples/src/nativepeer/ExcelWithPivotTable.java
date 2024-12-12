package nativepeer;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.types.LocaleID;
import com.jniwrapper.win32.excel._Worksheet;
import com.jniwrapper.win32.excel.impl.PivotTableImpl;
import com.jniwrapper.win32.jexcel.Application;
import com.jniwrapper.win32.jexcel.Cell;
import com.jniwrapper.win32.jexcel.Workbook;
import com.jniwrapper.win32.jexcel.Worksheet;

import java.io.File;

/**
 * <p>  This sample demonstrates how to trigger Pivot Table update after the raw data has been changed
 * using JExcel.
 * <p/>
 * <p>  This sample requires following preset to run successfully:
 * <ol><li>the standard jexcel.jar must be replaced with jexcel-full.jar which can be retrieved from
 * <a href="https://sites.google.com/a/teamdev.com/jexcel-support/hotfixes"> our hotfixes </a> page </li>
 * <li>the MS Excel workbook (PivotTest.xlsx) which comes with this sample must be present in the
 * project working directory</li></ol>
 */
public class ExcelWithPivotTable
{
    //Please, change the file path according to your environment
    private static final String filePath = "PivotTest.xlsx";
    private static final String dataSheetName = "Data";
    private static final String pivotTableSheetName = "Pivot";

    public static void main(String[] args)
    {
        Application application = null;
        Workbook workbook = null;
        try
        {
            application = new Application();
            //Open Workbook
            workbook = application.openWorkbook(new File(filePath));

            //Get dataSheet with raw data by name
            Worksheet dataSheet = workbook.getWorksheet(dataSheetName);
            //Get dataSheet with pivot table by name
            Worksheet pivotTableSheet = workbook.getWorksheet(pivotTableSheetName);

            //Get cell with data calculated by pivot table
            Cell result = pivotTableSheet.getCell("C6");
            System.out.println("Original value in pivot table " + result.getNumber());

            //Get cell with raw data
            Cell inputData = dataSheet.getCell("B2");
            System.out.println("Original value in data cell " + inputData.getString());
            //Update the source data
            inputData.setValue("UA");

            final _Worksheet pivotTableSheetPeer = pivotTableSheet.getPeer();
            //Put into OLE Message Loop action that will trigger the pivot table recalculation
            pivotTableSheet.getOleMessageLoop().doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    //Get the pivot table Dispatch interface. In this case we've got only one pivot table. In
                    //more complicated case we will need to look for the pivot table. The 'getPeer' method
                    //used to access wrappers of the MS Excel Objects.
                    IDispatch dispatch = pivotTableSheetPeer.pivotTables(new Variant(1),
                            new Int32(LocaleID.LOCALE_USER_DEFAULT));

                    //Query for PivotTable interface. This is the one of the ways to do it using ComfyJ,
                    //which is the base for JExcel
                    PivotTableImpl pivotTableObject = new PivotTableImpl(dispatch);

                    //Call refresh table
                    pivotTableObject.refreshTable();

                    //Release IDispatch
                    dispatch.setAutoDelete(false);
                    dispatch.release();

                    //Release pivot table
                    pivotTableObject.setAutoDelete(false);
                    pivotTableObject.release();
                }
            });

            System.out.println("Result value in data cell " + inputData.getString());

            System.out.println("Result value in pivot table " + result.getNumber());

            //Save the workbook with the same name
            workbook.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //Close the workbook and the application
            if (workbook != null)
            {
                workbook.close(false);
            }
            if (application != null)
            {
                application.close();
            }
        }
    }
}