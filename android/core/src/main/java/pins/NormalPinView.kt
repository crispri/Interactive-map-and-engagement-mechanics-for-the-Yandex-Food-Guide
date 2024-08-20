package pins

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.example.core.R

class NormalPinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    isFavorite: Boolean,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val titleTextView: TextView
    private val ratingTextView: TextView

    init {
        // Inflate the layout
        if(isFavorite){
            inflate(context, R.layout.view_normal_pin, this)
        } else{
            inflate(context, R.layout.view_normal_pin_fav, this)
        }


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