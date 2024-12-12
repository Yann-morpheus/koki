package range;

import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.jexcel.ExcelException;
import com.jniwrapper.win32.jexcel.Range;
import com.jniwrapper.win32.jexcel.Worksheet;
import com.jniwrapper.win32.jexcel.ui.JWorkbook;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This sample demonstrates usage of bulk reading and writing operations,
 * a way of fast obtaining and setting values in the range.
 */
public class RangeBulkOperationsSample
{
    public static final Dimension FRAME_SIZE = new Dimension(900, 275);
    private final JFrame _frame;
    private final JWorkbook _workbook;
    private final JTable _table;
    private final AbstractAction copyDataToJTableAction, copyDataToJWorkbookAction;

    public RangeBulkOperationsSample() throws ExcelException
    {

        _frame = new JFrame("JExcel: Batch reading/writing values in the range");
        _workbook = new JWorkbook();
        _table = new JTable(new DataTableModel());

        initFrame(_frame, _workbook);

        final Worksheet worksheet = _workbook.getActiveWorksheet();
        final Range range = worksheet.getRange("A1:D9");
        fillRange(worksheet, range);

        copyDataToJTableAction = new AbstractAction("Copy data from JWorkbook to JTable >")
        {
            public void actionPerformed(ActionEvent e)
            {
                Variant[][] worksheetData = range.getValues();
                for (int i = 0; i < worksheetData.length; i++)
                {
                    for (int j = 0; j < worksheetData[i].length; j++)
                    {
                        _table.setValueAt(worksheetData[i][j].getValue(), j, i);
                    }
                }
            }
        };
        copyDataToJWorkbookAction = new AbstractAction("< Copy data from JTable to JWorkbook")
        {
            public void actionPerformed(ActionEvent e)
            {
                String[][] data = ((DataTableModel) _table.getModel()).getData();
                worksheet.fillWithArray(range.getAddress(), data);
            }
        };
        Container contentPane = _frame.getContentPane();
        initContent(contentPane, _workbook, _table);

        _frame.setVisible(true);
        _workbook.getWorkbook().activate();
        worksheet.activate();
        range.select();

    }

    private void fillRange(Worksheet worksheet, Range range)
    {
        final String[][] string_array = new String[9][4];
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                string_array[i][j] = "Test: " + (j * 10 + i + 1);
            }
        }
        worksheet.fillWithArray(range.getAddress(), string_array);
    }

    private void initFrame(JFrame frame, final JWorkbook workbook)
    {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(FRAME_SIZE);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                workbook.close();
            }
        });
    }

    private void initContent(Container contentPane, final JWorkbook workbook, final JTable table)
    {
        contentPane.setLayout(new GridBagLayout());

        JButton copyToJTable = new JButton(copyDataToJTableAction);
        JButton copyToJWorkbook = new JButton(copyDataToJWorkbookAction);

        workbook.setMinimumSize(new Dimension(100, 100));

        contentPane.add(new JLabel("JWorkbook:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        contentPane.add(workbook, new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        contentPane.add(copyToJTable, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        contentPane.add(copyToJWorkbook, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        contentPane.add(new JLabel("JTable:"), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        Color background = table.getBackground();
        scrollPane.setBackground(background);
        scrollPane.getViewport().setBackground(background);
        contentPane.add(scrollPane, new GridBagConstraints(2, 1, 1, 2, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }


    public static void main(String[] args)
    {
        try
        {
            Object sample = new RangeBulkOperationsSample();
        }
        catch (ExcelException e)
        {
            e.printStackTrace();
        }
    }

    private static class DataTableModel extends AbstractTableModel
    {
        private String[] columnNames = new String[]{"A", "B", "C", "D"};
        private String[][] data;

        private DataTableModel()
        {
            data = new String[9][];
            for (int i = 0; i < data.length; i++)
            {
                data[i] = new String[4];
            }
        }

        public int getRowCount()
        {
            return data.length;
        }

        public int getColumnCount()
        {
            return columnNames.length;
        }

        public String getColumnName(int columnIndex)
        {
            return columnNames[columnIndex];
        }

        public Class getColumnClass(int columnIndex)
        {
            return String.class;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return data[rowIndex][columnIndex];
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            if (aValue != null)
            {
                data[rowIndex][columnIndex] = aValue.toString();
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }

        public String[][] getData()
        {
            return data;
        }

    }
}
