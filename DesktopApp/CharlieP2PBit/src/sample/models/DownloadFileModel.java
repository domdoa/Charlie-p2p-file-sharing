package sample.models;

public class DownloadFileModel {
    private String fileName;
    private String size;
    private String progress;
    private String speed;

    public DownloadFileModel(String fileName, String size,String progress, String speed){
        this.fileName = fileName;
        this.size = size;
        this.progress = progress;
        this.speed = speed;
    }

    //Getters  !Required for displaying the data!
    public String getFileName() {return this.fileName;}
    public String getSize() {return this.size;}
    public String getProgress() {return this.progress;}
    public String getSpeed() {return this.speed;}

    //Setters
    public void setFileName(String fileName) {this.fileName = fileName;}
    public void setSize(String size) {this.size = size;}
    public void setProgress(String progress) {this.progress = progress;}
    public void setSpeed(String speed) {this.speed = speed;}


}
