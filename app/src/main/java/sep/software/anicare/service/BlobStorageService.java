package sep.software.anicare.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import de.greenrobot.event.EventBus;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.event.AniCareException;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public interface BlobStorageService {
    public String getHostUrl();
    public String getHostUrl(String uri);
    public String getUserProfileHostUrl();
    public String getUserProfileImgUrl(String id);
    public String getItemImgHostUrl();
    public String getItemImgUrl(String id);

    public void isExistAsync(final String containerName, final String id, final EntityCallback<Boolean> callback);


    public void uploadBitmapAsync(final String containerName, String id, final Bitmap bitmap, final EntityCallback<String> callback);


    public void downloadBitmapAsync(final String containerName, String id, final EntityCallback<Bitmap> callback);


    public void downloadToFileAsync(final Context context, final String containerName, String id, final String path, final EntityCallback<String> callback);


    public void deleteBitmapAsync(final String containerName, String id, final EntityCallback<Boolean> callback);
}
