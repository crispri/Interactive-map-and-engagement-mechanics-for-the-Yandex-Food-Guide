package pins

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.TypedValueCompat.dpToPx
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.core.R

class CustomPinView @JvmOverloads constructor(
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
        inflate(context, R.layout.view_custom_pin, this)

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
            .transform(CenterCrop(), RoundedCorners(dpToPx(50f, context.resources.displayMetrics).toInt()))
            .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                override fun onResourceReady(resource: android.graphics.drawable.Drawable, transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?) {
                    imageView.setImageDrawable(resource)
                    onSuccess() // Notify that the image has been loaded
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
            })
    }

//        imageView.load(imageUrl) {
//            placeholder(R.drawable.ic_mini_pin) // Плейсхолдер на время загрузки
//            error(R.drawable.baseline_language_24)error(R.drawable.baseline_language_24) // Изображение ошибки, если не удалось загрузить
//            transformations(CircleCropTransformation()) // Скругление изображения
//            listener(
//                onStart = {
//                    Log.d("setImageLoading", "Загрузка изображения начата: $imageUrl")
//                },
//                onSuccess = { _, result ->
//                    Log.d("setImageSuccess", "Изображение загружено успешно: $imageUrl")
//                },
//                onError = { _, result ->
//                    Log.d("setImageError", "Ошибка при загрузке изображения: $imageUrl")
//                }
//            )
//        }
//
//        Log.d("setImageEnd", imageUrl)
//    }

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
