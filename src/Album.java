import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Project6
 * Created by jeffreyhammond on 12/8/16.
 */
public class Album implements Serializable {

    private ArrayList<File> files = new ArrayList<>();
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> fileCaption = new ArrayList<>();

    Album (String fileDir) {
        File dir = new File(fileDir);
        for (File fileURL: dir.listFiles((dir1, name) -> name.matches("\\w*(.jpeg|.jpg)"))) {
            files.add(fileURL);
        }

        for (File fileName : files) {
            fileNames.add(fileName.getName());
        }

        for (String caption : fileNames) {
            fileCaption.add("");
        }
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public int getFilesSize() {
        return files.size();
    }

    public String getFilesURL(int pos) {
        return files.get(pos).getAbsolutePath();
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public int getNameLocation(String name) {
        return fileNames.indexOf(name);
    }

    public ArrayList<String> getFileCaption() {
        return fileCaption;
    }

}
