package com.jamestiotio.sentienterprize;

import org.junit.Test;

import static org.junit.Assert.*;

import com.jamestiotio.sentienterprize.gravatar.*;

// Use https://en.gravatar.com/site/check/ to validate and verify the Gravatar URL.
public class GravatarTest {
    @Test
    public void checkNumOfRatingEnums() {
        int numOfEnums = GravatarRating.values().length;
        int result = 4;

        assertEquals(numOfEnums, result);
    }

    @Test
    public void checkRatingEnumCodes() {
        assertEquals("g", GravatarRating.GENERAL_AUDIENCES.getCode());
        assertEquals("pg", GravatarRating.PARENTAL_GUIDANCE_SUGGESTED.getCode());
        assertEquals("r", GravatarRating.RESTRICTED.getCode());
        assertEquals("x", GravatarRating.EXPLICIT.getCode());
    }

    @Test
    public void checkNumOfDefaultImageEnums() {
        int numOfEnums = GravatarDefaultImage.values().length;
        int result = 8;

        assertEquals(numOfEnums, result);
    }

    @Test
    public void checkDefaultImageEnumCodes() {
        assertEquals("", GravatarDefaultImage.GRAVATAR_ICON.getCode());
        assertEquals("identicon", GravatarDefaultImage.IDENTICON.getCode());
        assertEquals("monsterid", GravatarDefaultImage.MONSTERID.getCode());
        assertEquals("wavatar", GravatarDefaultImage.WAVATAR.getCode());
        assertEquals("404", GravatarDefaultImage.HTTP_404.getCode());
        assertEquals("mm", GravatarDefaultImage.MYSTERY_MAN.getCode());
        assertEquals("retro", GravatarDefaultImage.RETRO.getCode());
        assertEquals("blank", GravatarDefaultImage.BLANK.getCode());
    }

    @Test
    public void checkDefaultURLWithSampleEmail() {
        String url = new Gravatar().getUrl("test@example.com");
        String result = "https://s.gravatar.com/avatar/55502f40dc8b7c769880b10874abc9d0.jpg";

        assertEquals(result, url);
    }

    @Test(expected = NullPointerException.class)
    public void checkURLWithCustomInvalidEmail() {
        Gravatar gravatar = new Gravatar();
        String result = gravatar.getUrl(null);
    }

    @Test
    public void checkURLWithCustomValidSize() {
        Gravatar gravatar = new Gravatar();
        gravatar.setSize(256);
        String url = gravatar.getUrl("test@example.com");
        String result = "https://s.gravatar.com/avatar/55502f40dc8b7c769880b10874abc9d0.jpg?s=256";

        assertEquals(result, url);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkURLWithCustomInvalidLargeSize() {
        Gravatar gravatar = new Gravatar();
        gravatar.setSize(4096);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkURLWithCustomInvalidNegativeSize() {
        Gravatar gravatar = new Gravatar();
        gravatar.setSize(-1);
    }

    @Test
    public void checkURLWithCustomValidRating() {
        Gravatar gravatar = new Gravatar();
        gravatar.setRating(GravatarRating.EXPLICIT);
        String url = gravatar.getUrl("test@example.com");
        String result = "https://s.gravatar.com/avatar/55502f40dc8b7c769880b10874abc9d0.jpg?r=x";

        assertEquals(result, url);
    }

    @Test(expected = NullPointerException.class)
    public void checkURLWithCustomInvalidRating() {
        Gravatar gravatar = new Gravatar();
        gravatar.setRating(null);
    }

    @Test
    public void checkURLWithCustomValidDefaultImage() {
        Gravatar gravatar = new Gravatar();
        gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
        String url = gravatar.getUrl("test@example.com");
        String result = "https://s.gravatar.com/avatar/55502f40dc8b7c769880b10874abc9d0.jpg?d=identicon";

        assertEquals(result, url);
    }

    @Test(expected = NullPointerException.class)
    public void checkURLWithCustomInvalidDefaultImage() {
        Gravatar gravatar = new Gravatar();
        gravatar.setDefaultImage(null);
    }

    @Test
    public void checkURLWithCustomSizeRatingDefaultImage() {
        Gravatar gravatar = new Gravatar();
        gravatar.setDefaultImage(GravatarDefaultImage.MONSTERID);
        gravatar.setRating(GravatarRating.PARENTAL_GUIDANCE_SUGGESTED);
        gravatar.setSize(80);
        String url = gravatar.getUrl("test@example.com");
        String result = "https://s.gravatar.com/avatar/55502f40dc8b7c769880b10874abc9d0.jpg?s=80&r=pg&d=monsterid";

        assertEquals(result, url);
    }

    @Test
    public void checkDownload() {
        Gravatar gravatar = new Gravatar();
        byte[] bytes = gravatar.download("test@example.com");
        assertTrue("Content is present.", bytes.length > 100);

        gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
        bytes = gravatar.download("test@example.com");
        assertTrue("Content is present.", bytes.length > 100);

        gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
        bytes = gravatar.download("test@example.com");
        assertTrue("Content is present.", bytes.length > 100);

        gravatar.setSize(2048);
        bytes = gravatar.download("test@example.com");
        assertTrue("Content is present.", bytes.length > 100);
    }
}