package pins

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.example.core.R

class NormalPinViewSelected @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val titleTextView: TextView
    private val ratingTextView: TextView

    init {
        // Inflate the layout
        inflate(context, R.layout.view_normal_pin_selected, this)

        // Get references to the views
        titleTextView = findViewById(R.id.titleTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
    }


    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setRating(rating: String) {
        ratingTextView.text = rating
    }

}