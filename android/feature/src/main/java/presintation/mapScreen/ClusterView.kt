package presintation.mapScreen

import android.content.Context
import android.widget.LinearLayout
import com.yandex.mapkit.map.PlacemarkMapObject

class ClusterView(context: Context) : LinearLayout(context) {

    fun setData(placemarks : List<PlacemarkMapObject>) {
        for(item in placemarks){
            if(placemarks[0] != item){
                item.isVisible = false
            }
        }
    }
}