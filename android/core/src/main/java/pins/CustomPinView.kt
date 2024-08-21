package pins

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.core.R

/**
 * Custom view representing a pin with various configurations based on favorite status,
 * Ultima status, and Open Kitchen status. Inflates different layouts depending on
 * the combination of these attributes.
 */
class CustomPinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 100,
    isFavorite: Boolean,
    isUltima: Boolean,
    isOpenKitchen: Boolean
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val titleTextView: TextView
    private val ratingTextView: TextView
    private val descriptionTextView: TextView

    init {
        // Inflate the layout
        if(isFavorite && isUltima && isOpenKitchen){  // 1 1 1
            inflate(context, R.layout.view_custom_pin_ultima_openk_fav, this)
        } else if(!isFavorite && isUltima && isOpenKitchen){   // 0 1 1
            inflate(context, R.layout.view_custom_pin_ultima_openk, this)
        } else if(!isFavorite && !isUltima && isOpenKitchen){//0 0 1
            inflate(context, R.layout.view_custom_pin_openk, this)
        } else if(!isFavorite && isUltima && !isOpenKitchen){ // 0 1 0
            inflate(context, R.layout.view_custom_pin_ultima, this)
        } else if(isFavorite && !isUltima && !isOpenKitchen){ // 1 0 0
            inflate(context, R.layout.view_custom_pin_fav, this)
        } else if(isFavorite && isUltima && !isOpenKitchen){ // 1 1 0
            inflate(context, R.layout.view_custom_pin_ultima_fav, this)
        } else if(!isFavorite && !isUltima && !isOpenKitchen){ // 0 0 0
            inflate(context, R.layout.view_custom_pin, this)
        } else if(isFavorite && !isUltima && isOpenKitchen){ // 1 0 1
            inflate(context, R.layout.view_custom_pin_openk_fav, this)
        }


        // Get references to the views
        imageView = findViewById(R.id.ivPictureOfPlace)
        titleTextView = findViewById(R.id.titleTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
    }

    fun setImageWithGlide(url: String, onSuccess: () -> Unit) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_mini_pin)
            .error(R.drawable.baseline_language_24)
            .transform(CenterCrop(), RoundedCorners(dpToPx(40f, context.resources.displayMetrics).toInt()))
            .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                override fun onResourceReady(resource: android.graphics.drawable.Drawable, transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?) {
                    imageView.setImageDrawable(resource)
                    onSuccess() // Notify that the image has been loaded
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
            })
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setRating(rating: String) {
        ratingTextView.text = rating
    }

    fun setDescription(description: String) {
        descriptionTextView.text = description
    }
}
