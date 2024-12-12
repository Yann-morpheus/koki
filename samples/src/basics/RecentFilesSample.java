package basics;

import com.jniwrapper.win32.jexcel.Application;

import java.io.File;
import java.util.List;

/**
 * This sample starts a new Microsoft Excel application, prints the list of recently opened files
 * and closes application after itself.
 * <p/>
 * The sample works with MS Excel in non-embedded mode.
 */
public class RecentFilesSample
{
    public static void main(String[] args) throws Exception
    {
        //Start new MS Excel application
        Application application = new Application();

        //Perform actions with MS Excel
        printRecentFiles(application);

        //Perform cleanup after yourself and close the MS Excel application forcing it to quit
        application.close(true);
    }

    /**
     * Retrieves the recent files list and prints it to std out.
     *
     * @param application - MS Excel application instance.
     */
    public static void printRecentFiles(Application application)
    {
        List files = application.getRecentFiles();
        for (int i = 0; i < files.size(); i++)
        {
            File file = (File) files.get(i);
            System.out.println(file.getAbsolutePath());
        }
    }
}