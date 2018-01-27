package loyaltywallet.com.Binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by HemantSingh on 20/03/17.
 */

public class GlideBindingAdapter {
    @BindingAdapter({"bind:image_url"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f)
                .fitCenter().crossFade().into(imageView);
    }
}
