package pins

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.TypedValueCompat.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.core.R

/**
 * Custom view representing a selected pin with various configurations based on favorite status,
 * Ultima status, and Open Kitchen status. Inflates different layouts depending on
 * the combination of these attributes.
 */
class CustomPinViewSelected @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
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
        if(!isFavorite && !isUltima && !isOpenKitchen){ //0
            inflate(context, R.layout.view_custom_pin_selected, this)
        } else if(!isFavorite && !isUltima && isOpenKitchen){ //1
            inflate(context, R.layout.view_pin_selected__openk, this)
        } else if(!isFavorite && isUltima && !isOpenKitchen){ //2
            inflate(context, R.layout.view_pin_selected__ultima, this)
        } else if(!isFavorite && isUltima && isOpenKitchen){ //3
            inflate(context, R.layout.view_pin_selected__ultima_openk, this)
        } else if(isFavorite && !isUltima && !isOpenKitchen){ //4
            inflate(context, R.layout.view_pin_selected__fav, this)
        } else if(isFavorite && !isUltima && isOpenKitchen){ //5
            inflate(context, R.layout.view_pin_selected__openk_fav, this)
        } else if(isFavorite && isUltima && !isOpenKitchen){ //6
            inflate(context, R.layout.view_pin_selected__ultima_fav, this)
        } else if(isFavorite && isUltima && isOpenKitchen){ //7
            inflate(context, R.layout.view_pin_selected__ultima_openk_fav, this)
        }

        // Get references to the views
        imageView = findViewById(R.id.ivPictureOfPlace1)
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