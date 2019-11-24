package dtos;

public class File {
    private long id;
    private long user_id;
    private String name;
    private String ext;
    private String md5Sign;
    private String size;

    public File() {}

    public File(long id, long user_id, String name, String ext, String md5Sign, String size) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.ext = ext;
        this.md5Sign = md5Sign;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMd5Sign() {
        return md5Sign;
    }

    public void setMd5Sign(String md5Sign) {
        this.md5Sign = md5Sign;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
