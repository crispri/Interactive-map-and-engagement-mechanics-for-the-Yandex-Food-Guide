package pins

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.TypedValueCompat.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.core.R

class CustomPinViewSelected @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val titleTextView: TextView
    private val ratingTextView: TextView
    private val descriptionTextView: TextView

    init {
        // Inflate the layout
        inflate(context, R.layout.view_custom_pin_selected, this)

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