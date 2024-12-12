package range;

import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.excel.Comment;
import com.jniwrapper.win32.excel.Range;
import com.jniwrapper.win32.jexcel.Application;
import com.jniwrapper.win32.jexcel.Cell;
import com.jniwrapper.win32.jexcel.Workbook;
import com.jniwrapper.win32.jexcel.Worksheet;

public class SettingGettingRangeCommentsExample
{
    public static void main(String[] args) throws Exception
    {
        Application application = new Application();
        final Workbook workbook = application.createWorkbook(null);

        application.setVisible(true);

        try
        {
            System.out.println("Before setting comment. Press 'Enter' to proceed >");
            System.in.read();

            final Worksheet activeWorksheet = workbook.getActiveWorksheet();
            activeWorksheet.getOleMessageLoop().doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    Cell cell = activeWorksheet.getCell("A1");
                    cell.getPeer().addComment(new Variant("Comment"));
                    cell.release();
                }
            });

            System.out.println("Before getting comment. Press 'Enter' to proceed >");
            System.in.read();

            workbook.getOleMessageLoop().doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    Cell cell = activeWorksheet.getCell("A1");
                    Comment comment = cell.getPeer().getComment();
                    Variant unspecified = Variant.createUnspecifiedParameter();
                    System.out.println("comment = " + comment.text(unspecified, unspecified, unspecified));
                }
            });

            System.out.println("Before setting new comment. Press 'Enter' to proceed >");
            System.in.read();

            activeWorksheet.getOleMessageLoop().doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    Cell cell = activeWorksheet.getCell("A1");
                    Range cellPeer = cell.getPeer();
                    Comment comment = cellPeer.getComment();
                    comment.delete();
                    comment.setAutoDelete(false);
                    comment.release();

                    cellPeer.addComment(new Variant("Another comment"));
                    cell.release();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Before closing application. Press 'Enter' to proceed >");
        System.in.read();

        application.close();
    }
}
