/*
 * Project6
 * Created by Jeffrey Hammond on 11/29/16.
 */

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


class Album implements Serializable {

    private ArrayList<File> files = new ArrayList<>();
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> fileCaption = new ArrayList<>();
    private int positionWhenSaved = 0;
    private int isThumbnailPanelDisplayed = 0;

    Album (String fileDir) {
        File dir = new File(fileDir);
        for (File fileURL: dir.listFiles((dir1, name) -> name.matches("\\w*(.jpeg|.jpg)"))) {
            files.add(fileURL);
        }

        for (File fileName : files) fileNames.add(fileName.getName());

        for (String caption : fileNames) fileCaption.add("");
    }

    ArrayList<File> getFiles() {
        return files;
    }

    int getAlbumSize() {
        return files.size();
    }

    String getFileURL(int pos) {
        return files.get(pos).getAbsolutePath();
    }

    File getFileAtLocation(int pos) {
        return files.get(pos);
    }

    ArrayList<String> getFileNames() {
        return fileNames;
    }

    String getName(int pos) {
        return fileNames.get(pos);
    }

    int getFileLocation(File name) {
                return files.indexOf(name);
    }

    int getNameLocation(String name) {
        String[] tokens = name.trim().split("\\s+");
        return fileNames.indexOf(tokens[0]);
    }

    String getFileCaption(int pos) {
        return fileCaption.get(pos);
    }

    void setFileCaption(String caption, int pos) {
        this.fileCaption.set(pos, caption);
    }

    void setPositionWhenSaved (int pos) {
        this.positionWhenSaved = pos;
    }

    int getPositionWhenSaved() {
        return positionWhenSaved;
    }

    int getIsThumbnailPanelDisplayed() {
        return isThumbnailPanelDisplayed;
    }

    void setIsThumbnailPanelDisplayed(int isThumbnailButtonEnabled) {
        this.isThumbnailPanelDisplayed = isThumbnailButtonEnabled;
    }
}
