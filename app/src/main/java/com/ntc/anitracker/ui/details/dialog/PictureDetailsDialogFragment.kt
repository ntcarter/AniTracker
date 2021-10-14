package com.ntc.anitracker.ui.details.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.images.Picture
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

private const val TAG = "PictureDialog"
class PictureDetailsDialogFragment(
    private val imageList: List<Picture>,
    val listener: OnDialogState
) : DialogFragment() {

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // let the fragment know the dialog has been dismissed
        listener.onDialogDismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val layout = inflater.inflate(R.layout.dialog_fragment_pictures, null)
            val sliderView = layout.findViewById<SliderView>(R.id.imageSlider)
            val adapter = ImageSliderAdapter(imageList)

            // sliderView settings
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            sliderView.indicatorSelectedColor = Color.WHITE
            sliderView.indicatorUnselectedColor = Color.GRAY
            sliderView.isAutoCycle = false
            sliderView.setSliderAdapter(adapter)

            builder.setView(layout)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Callback to signal to the host fragment that the open dialog has been dismissed
    interface OnDialogState {
        fun onDialogDismiss()
    }
}