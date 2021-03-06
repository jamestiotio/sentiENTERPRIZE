package com.jamestiotio.sentienterprize.gravatar;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 * A Gravatar is a dynamic image resource that is requested from the
 * gravatar.com server. This class calculates the Gravatar url and fetches
 * Gravatar images. See https://en.gravatar.com/site/implement/hash.
 *
 * This class is thread-safe (i.e. Gravatar objects can be shared).
 */
public final class Gravatar {
    private final static int DEFAULT_SIZE = 40;
    private final static String GRAVATAR_URL = "https://s.gravatar.com/avatar/";
    private static final GravatarRating DEFAULT_RATING = GravatarRating.GENERAL_AUDIENCES;
    private static final GravatarDefaultImage DEFAULT_IMAGE = GravatarDefaultImage.GRAVATAR_ICON;

    private int size = DEFAULT_SIZE;
    private GravatarRating rating = DEFAULT_RATING;
    private GravatarDefaultImage defaultImage = DEFAULT_IMAGE;

    /**
     * Specify a gravatar size between 1 and 2048 pixels. If you omit this, a
     * default size of 40 pixels is used.
     */
    public void setSize(int sizeInPixels) {
        Validate.isTrue(sizeInPixels >= 1 && sizeInPixels <= 2048,
                "sizeInPixels needs to be between 1 and 2048 inclusive");
        this.size = sizeInPixels;
    }

    /**
     * Specify a rating to ban gravatar images with explicit content.
     */
    public void setRating(GravatarRating rating) {
        Validate.notNull(rating, "rating");
        this.rating = rating;
    }

    /**
     * Specify the default image to be produced if no gravatar image was found.
     */
    public void setDefaultImage(GravatarDefaultImage defaultImage) {
        Validate.notNull(defaultImage, "defaultImage");
        this.defaultImage = defaultImage;
    }

    /**
     * Returns the Gravatar URL for the given email address.
     */
    public String getUrl(String email) {
        Validate.notNull(email, "email");

        // Hexadecimal MD5 hash of the requested user's lowercased email address
        // with all whitespace trimmed
        String emailHash = new String(Hex.encodeHex(DigestUtils.md5(email.toLowerCase().trim())));
        String params = formatUrlParameters();
        if (!(params.isEmpty())) {
            return GRAVATAR_URL + emailHash + ".jpg" + params;
        }
        else {
            return GRAVATAR_URL + emailHash + ".jpg";
        }
    }

    /**
     * Downloads the gravatar for the given URL using Java {@link URL} and
     * returns a byte array containing the gravatar jpg, returns null if no
     * gravatar was found.
     */
    public byte[] download(String email) throws GravatarDownloadException {
        InputStream stream = null;
        try {
            URL url = new URL(getUrl(email));
            stream = url.openStream();
            return IOUtils.toByteArray(stream);
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new GravatarDownloadException(e);
        } finally {
            cleanup(Objects.requireNonNull(stream));
        }
    }

    private static void cleanup(Closeable stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new GravatarDownloadException(e);
        }
    }

    private String formatUrlParameters() {
        List<String> params = new ArrayList<>();

        if (size != DEFAULT_SIZE) {
            params.add("s=" + size);
        }

        if (rating != DEFAULT_RATING) {
            params.add("r=" + rating.getCode());
        }

        if (defaultImage != GravatarDefaultImage.GRAVATAR_ICON) {
            params.add("d=" + defaultImage.getCode());
        }

        if (params.isEmpty()) {
            return "";
        }
        else {
            return "?" + StringUtils.join(params.iterator(), "&");
        }
    }
}
