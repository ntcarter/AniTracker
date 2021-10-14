package com.ntc.anitracker.ui.details.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ntc.anitracker.api.models.images.Picture
import com.ntc.anitracker.databinding.ItemImageSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter


class ImageSliderAdapter(
    var picList: List<Picture>
) : SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val binding =
            ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderAdapterVH(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem: String? = picList[position].large ?: picList[position].small ?: picList[position].image_url

        if(sliderItem != null){
            viewHolder.bind(sliderItem)
        }
    }

    override fun getCount(): Int {
        return picList.size
    }

    inner class SliderAdapterVH(private val binding: ItemImageSliderBinding) :
        ViewHolder(binding.root) {
            fun bind(sliderItem: String) {
                Glide.with(itemView)
                    .load(sliderItem)
                    .into(binding.sliderImage)
            }
    }
}