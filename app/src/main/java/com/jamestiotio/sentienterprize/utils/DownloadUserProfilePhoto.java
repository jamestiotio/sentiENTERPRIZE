package com.jamestiotio.sentienterprize.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.jamestiotio.sentienterprize.gravatar.Gravatar;
import com.jamestiotio.sentienterprize.gravatar.GravatarDefaultImage;
import com.jamestiotio.sentienterprize.gravatar.GravatarRating;

import java.lang.ref.WeakReference;

public class DownloadUserProfilePhoto extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<Context> contextReference;
    private final ImageView profilePhoto;

    @SuppressWarnings("deprecation")
    public DownloadUserProfilePhoto(Context context, ImageView profilePhoto) {
        this.contextReference = new WeakReference<>(context);
        this.profilePhoto = profilePhoto;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        // Set up Gravatar
        Gravatar gravatar = new Gravatar();
        gravatar.setSize(40);
        gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
        gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
        String email = strings[0];
        byte[] data = gravatar.download(email);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        RoundedBitmapDrawable image = RoundedBitmapDrawableFactory.create(this.contextReference.get().getResources(), bitmap);
        image.setCircular(true);
        this.profilePhoto.setImageDrawable(image);
    }
}