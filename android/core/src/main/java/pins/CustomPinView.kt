package pins

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
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

    fun setImageWithGlide(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_mini_pin) // Плейсхолдер на время загрузки
            .error(R.drawable.baseline_language_24) // Изображение ошибки
            .into(imageView) // Замените на ваш ImageView

    }
    fun setImageWithCoil(imageUrl: String) {

        imageView.load(imageUrl) {
            placeholder(R.drawable.ic_mini_pin) // Плейсхолдер на время загрузки
            error(R.drawable.baseline_language_24) // Изображение ошибки, если не удалось загрузить
            transformations(CircleCropTransformation()) // Скругление изображения
            listener(
                onStart = {
                    Log.d("setImageLoading", "Загрузка изображения начата: $imageUrl")
                },
                onSuccess = { _, result ->
                    Log.d("setImageSuccess", "Изображение загружено успешно: $imageUrl")
                },
                onError = { _, result ->
                    Log.d("setImageError", "Ошибка при загрузке изображения: $imageUrl")
                }
            )
        }

        Log.d("setImageEnd", imageUrl)
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
