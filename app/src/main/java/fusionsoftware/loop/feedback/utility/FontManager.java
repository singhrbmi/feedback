package fusionsoftware.loop.feedback.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

public class FontManager {

    // usage: yourTextView.setTypeface(FontManager.getTypeface(FontManager.YOURFONT));
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface getFontTypeface(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                            assetPath);

                    cache.put(assetPath, typeface);
                } catch (Exception e) {
                    Log.e("tag", "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    //get FontTypeface for Material Design Icons
    public static Typeface getFontTypefaceMaterialDesignIcons(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                            assetPath);

                    cache.put(assetPath, typeface);
                } catch (Exception e) {
                    Log.e("tag", "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}
