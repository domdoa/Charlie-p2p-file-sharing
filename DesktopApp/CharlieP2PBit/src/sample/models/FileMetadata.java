package sample.models;

public class FileMetadata {

    private String id;
    private String userId; //owner, uploader
    private String groupId; // if null, then the file is public
    private String fileName;
    private String extension;
    private long size;
    private String MD5Signature;

    public FileMetadata() {}

    public FileMetadata(String id, String userId, String groupId, String fileName, String extension, int size, String MD5Signature) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.MD5Signature = MD5Signature;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMD5Signature() {
        return MD5Signature;
    }

    public void setMD5Signature(String MD5Signature) {
        this.MD5Signature = MD5Signature;
    }
}
