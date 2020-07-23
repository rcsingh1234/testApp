package com.test.card.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

data class Result(
    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered,
    var status : String,
    var isSelected : Boolean
){

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: CircleImageView, image_path: String?) {
            Glide.with(view.context)
                .load(image_path)
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("description")
        fun setDescription(view: TextView, description: String?) {
            view.text = description
        }
    }
}