package sample.controllers;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;

import java.io.IOException;

public class FileController {

    private String localDir;
    private String username;
    private String password;
    private String remoteHost;
    private String remoteFile;


    public void whenDownloadFileUsingVfs_thenSuccess() throws IOException {
        FileSystemManager manager = VFS.getManager();

        FileObject local = manager.resolveFile(
                System.getProperty("user.dir") + "/" + localDir + "vfsFile.txt");
        FileObject remote = manager.resolveFile(
                "sftp://" + username + ":" + password + "@" + remoteHost + "/" + remoteFile);

        local.copyFrom(remote, Selectors.SELECT_SELF);

        local.close();
        remote.close();
    }

    public static void main(String[] args) {

    }
}
