package com.ntc.anitracker

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ntc.anitracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var clicked = false

    // Tracks if custom or default colors are active
    private var defaultColors = false

    // animations
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }

    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }

    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }

    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            val navController = navHostFragment.findNavController()
            fabSettings.setOnClickListener {
                settingsClicked()
            }
            fabBackOne.setOnClickListener {
                navController.navigateUp()
            }
            fabHome.setOnClickListener {
                if(navController.currentDestination?.id != R.id.galleryFragment){
                    navController.navigate(R.id.galleryFragment)
                }
            }
            fabSearch.setOnClickListener {
                // TODO navigate to search screen
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun settingsClicked() {
        setVisibility(clicked)
        setAnimations(clicked)

        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            // make all the fab's visible
            binding.apply {
                fabBackOne.visibility = View.VISIBLE
                fabHome.visibility = View.VISIBLE
                fabSearch.visibility = View.VISIBLE
            }
        } else {
            // make all the fab's gone
            binding.apply {
                fabSearch.visibility = View.GONE
                fabHome.visibility = View.GONE
                fabBackOne.visibility = View.GONE
            }
        }
    }

    private fun setAnimations(clicked: Boolean) {
        if (!clicked) {
            binding.apply {
                fabHome.startAnimation(fromBottom)
                fabBackOne.startAnimation(fromBottom)
                fabSearch.startAnimation(fromBottom)
                fabSettings.startAnimation(rotateOpen)
            }
        } else {
            binding.apply {
                fabHome.startAnimation(toBottom)
                fabBackOne.startAnimation(toBottom)
                fabSearch.startAnimation(toBottom)
                fabSettings.startAnimation(rotateClose)
            }
        }
    }

    interface ColorChanger {
        fun changeColors(isDefault: Boolean)
    }
}