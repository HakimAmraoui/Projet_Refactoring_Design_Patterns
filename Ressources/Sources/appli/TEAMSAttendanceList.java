package appli;

import java.io.*;
import java.util.LinkedList;

public class TEAMSAttendanceList {

    private final File _file;
    private LinkedList<String> _attlist = null;

    public TEAMSAttendanceList(File _file) {
        this._file = _file;
        this.loadTeamsCSVFile();
    }

    private void loadTeamsCSVFile() {
        BufferedReader b = null;
        try {
            b = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(_file), "UTF16"));
            String readLine;
            this._attlist = new LinkedList<>();
            while ((readLine = b.readLine()) != null) {
                this._attlist.add(readLine);
            }
            // last line is empty
            this._attlist.removeLast();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (b!=null) {
                try {
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public LinkedList<String> get_attlist() {
        return _attlist;
    }
}
