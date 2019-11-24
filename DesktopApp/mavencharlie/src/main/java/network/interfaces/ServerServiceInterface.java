package network.interfaces;

import dtos.File;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ServerServiceInterface {

    @POST("/peers/files")
    Call<ResponseBody> addFilesToPeer(@Body List<File> file, @Query("peer_id") long peer_id);

    @PUT("/peers/files")
    Call<ResponseBody> updateFileOfPeer(@Body File file, @Body String fileName, @Query("peer_id") long peer_id);

    @DELETE("/peers/files")
    Call<ResponseBody> removeFileFromPeer(@Body File file, @Query("peer_id") long peer_id);
}
