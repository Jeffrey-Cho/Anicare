package sep.software.anicare.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import sep.software.anicare.event.AniCareException;
import sep.software.anicare.callback.EntityCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import de.greenrobot.event.EventBus;

class BlobStorageService {

    private static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;AccountName=portalvhdsj2ksq9qld7v06;AccountKey=EKOOgf0UchaHBQ25meKH3utLq8bTLZK0fIIwmeXAYlXcTujIlpTkCaycMmkyasMjxUmSpmKTls2hJK7+gV46RA==";
    public static final String CONTAINER_USER_PROFILE = "anicare-profile";
    public static final String CONTAINER_IMAGE = "anicare-image";

    private static CloudBlobClient blobClient;


    public BlobStorageService() {

        CloudStorageAccount account = null;
        try {
            account = CloudStorageAccount.parse(storageConnectionString);
        } catch (InvalidKeyException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (URISyntaxException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        }
        blobClient = account.createCloudBlobClient();
    }


    public String getHostUrl() {
        return "https://portalvhdsj2ksq9qld7v06.blob.core.windows.net/";
    }
    public String getHostUrl(String uri) {
        return getHostUrl() + uri + "/";
    }
    public String getUserProfileHostUrl() {
        return getHostUrl(CONTAINER_USER_PROFILE);
    }
    public String getUserProfileImgUrl(String id) {
        return getUserProfileHostUrl() + id;
    }
    public String getItemImgHostUrl() {
        return getHostUrl(CONTAINER_IMAGE);
    }
    public String getItemImgUrl(String id) {
        return getItemImgHostUrl() + id;
    }


    private boolean isExistSync(String containerName, String id) {
        CloudBlobContainer container = null;
        CloudBlockBlob blob = null;
        boolean result = true;
        try {
            container = blobClient.getContainerReference(containerName);
            blob = container.getBlockBlobReference(id);
            result = blob.exists();
        } catch (URISyntaxException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (StorageException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        }
        return result;
    }


    private String uploadBitmapSync(String containerName, String id, Bitmap bitmap) {
        CloudBlobContainer container = null;
        CloudBlockBlob blob = null;
        try {
            container = blobClient.getContainerReference(containerName);
            blob = container.getBlockBlobReference(id);

            // Compress Bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            // Add header for picasso
            blob.getProperties().setCacheControl("only-if-cached, max-age=" + Integer.MAX_VALUE);

            blob.upload(new ByteArrayInputStream(baos.toByteArray()), baos.size());
            baos.close();
        } catch (URISyntaxException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (StorageException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (IOException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        }
        return id;
    }


    private Bitmap downloadBitmapSync(String containerName, String id) {
        CloudBlobContainer container = null;
        CloudBlockBlob blob = null;
        Bitmap bm = null;
        try {
            container = blobClient.getContainerReference(containerName);
            blob = container.getBlockBlobReference(id);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            blob.download(baos);
            bm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
        } catch (URISyntaxException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (StorageException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        }
        return bm;
    }


    private String downloadToFileSync(Context context, String containerName, String id, String path) {
        CloudBlobContainer container = null;
        CloudBlockBlob blob = null;
        try {
            container = blobClient.getContainerReference(containerName);
            blob = container.getBlockBlobReference(id);
            blob.downloadToFile(context.getFilesDir() + "/" + path);
        } catch (URISyntaxException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (StorageException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (IOException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        }
        return context.getFilesDir() + "/" + path;
    }


    private boolean deleteBitmapSync(String containerName, String id) {
        CloudBlobContainer container = null;
        CloudBlockBlob blob = null;
        try {
            container = blobClient.getContainerReference(containerName);
            blob = container.getBlockBlobReference(id);
            blob.delete();
        } catch (URISyntaxException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        } catch (StorageException e) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.BLOBSTORAGE_ERROR));
        }
        return true;
    }



    public void isExistAsync(final String containerName, final String id, final EntityCallback<Boolean> callback) {

        (new AniCareAsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String id = params[0];
                return isExistSync(containerName, id);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(id);
    }


    public void uploadBitmapAsync(final String containerName, String id, final Bitmap bitmap, final EntityCallback<String> callback) {

        (new AniCareAsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String id = params[0];
                return uploadBitmapSync(containerName, id, bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(id);
    }


    public void downloadBitmapAsync(final String containerName, String id, final EntityCallback<Bitmap> callback) {

        (new AniCareAsyncTask<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... params) {
                String id = params[0];
                return downloadBitmapSync(containerName, id);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(id);
    }


    public void downloadToFileAsync(final Context context, final String containerName, String id, final String path, final EntityCallback<String> callback) {

        (new AniCareAsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String id = params[0];
                return downloadToFileSync(context, containerName, id, path);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(id);
    }


    public void deleteBitmapAsync(final String containerName, String id, final EntityCallback<Boolean> callback) {

        (new AniCareAsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String id = params[0];
                return deleteBitmapSync(containerName, id);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(id);
    }
}
