package codesages.mosaic.helpers;

import android.net.Uri;

/**
 * Created by DELL on 12/11/2016.
 */

public class ImagePath {
    String Path;
    String uri;
    boolean isImageFromGoogleDrive;
    boolean isSuccess;

    public ImagePath(boolean isImageFromGoogleDrive, String imgPath, String imgUri, boolean isSuccess) {
        Path = imgPath;
        this.uri = imgUri;
        this.isImageFromGoogleDrive = isImageFromGoogleDrive;
        this.isSuccess = isSuccess;
    }

    public ImagePath(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public boolean isImageFromGoogleDrive() {
        return isImageFromGoogleDrive;
    }

    public void setImageFromGoogleDrive(boolean imageFromGoogleDrive) {
        isImageFromGoogleDrive = imageFromGoogleDrive;
    }
}
