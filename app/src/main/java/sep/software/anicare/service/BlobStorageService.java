package sep.software.anicare.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import sep.software.anicare.AniCareException;
import sep.software.anicare.interfaces.EntityCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.CorsHttpMethods;
import com.microsoft.azure.storage.CorsProperties;
import com.microsoft.azure.storage.CorsRule;
import com.microsoft.azure.storage.ServiceProperties;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import de.greenrobot.event.EventBus;

import static com.microsoft.azure.storage.CorsHttpMethods.PUT;

public class BlobStorageService {

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
            blob.getProperties().setContentType("image/jpeg");
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

    public boolean setHeader() {

        CorsRule corsRule = new CorsRule();

//            AllowedHeaders = new List<string> { "x-ms-*", "content-type", "accept" },
//            AllowedMethods = CorsHttpMethods.Put,//Since we'll only be calling Put Blob, let's just allow PUT verb
//            AllowedOrigins = new List<string> { "http://localhost:61233" },//This is the URL of our application.
//            MaxAgeInSeconds = 1 * 60 * 60,//Let the browswer cache it for an hour


        corsRule.setAllowedHeaders(new ArrayList<String>() { { add("x-ms-*"); add("content-type"); add("accept"); }});

        EnumSet<CorsHttpMethods> methods = EnumSet.of(CorsHttpMethods.PUT, CorsHttpMethods.GET, CorsHttpMethods.POST, CorsHttpMethods.OPTIONS, CorsHttpMethods.DELETE, CorsHttpMethods.HEAD);
        corsRule.setAllowedMethods(methods);
        corsRule.setAllowedOrigins(new ArrayList<String>() {
            {
                add("http://localhost");
                add("http://hongkunyoo.github.io/dummydata");
                add("*");

        }});
        corsRule.setMaxAgeInSeconds(1 * 60 * 60);

        try {
            ServiceProperties serviceProperties = blobClient.downloadServiceProperties();
            CorsProperties corsSettings = serviceProperties.getCors();
            corsSettings.getCorsRules().add(corsRule);
            blobClient.uploadServiceProperties(serviceProperties);
        } catch (StorageException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
