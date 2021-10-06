import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.launcher.R

@BindingAdapter("imageUrl")
fun imageView(view: ImageView, url: String?) {
    Glide.with(view)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(view)
}

@BindingAdapter("isVisible")
fun View.isVisible(isShow: Boolean) {
    if (isShow) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter("isVisInvis")
fun View.isVisInvis(isShow: Boolean) {
    if (isShow) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}


