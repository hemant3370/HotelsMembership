package hotelsmembership.com.Binding;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import hotelsmembership.com.R;

/**
 * Created by HemantSingh on 20/03/17.
 */

public class GlideBindingAdapter {
    @BindingAdapter({"bind:image_url"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail(0.5f)
                .placeholder(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_hotel_black_24dp))
                .fitCenter().into(imageView);
    }
}
