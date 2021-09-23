package com.ntc.anitracker.ui.adapters.detailsadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ntc.anitracker.R
import com.ntc.anitracker.api.models.charactersandstaff.CharacterStaff
import com.ntc.anitracker.databinding.ItemCharacterStaffBinding

class CharactersAndStaffAdapter(
    private val infoList: List<CharacterStaff>,
    val titleTextColor:Int,
    val listener: CharacterStaffClick
) : RecyclerView.Adapter<CharactersAndStaffAdapter.CharactersAndStaffViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharactersAndStaffViewHolder {
        val binding =
            ItemCharacterStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersAndStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharactersAndStaffViewHolder, position: Int) {
        val currentItem = infoList[position]
        // decides if a character or staff need to be bound to out item
        if (currentItem.voice_actors.isNullOrEmpty()) {
            holder.bindStaff(currentItem)
        } else {
            holder.bindCharacter(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    interface CharacterStaffClick{
        fun onCharacterClick(mal_id: Int)
        fun onStaffClick(mal_id: Int)
    }

    inner class CharactersAndStaffViewHolder(private val binding: ItemCharacterStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindCharacter(character: CharacterStaff) {
            binding.apply {
                Glide.with(itemView)
                    .load(character.image_url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivPerson)
                ivPerson.setOnClickListener {
                    listener.onCharacterClick(character.mal_id)
                }
                tvName.text = character.name
                tvName.setTextColor(titleTextColor)
                tvInfo.text = character.role
                tvInfo.setTextColor(titleTextColor)

                // Voice actor recyclerView initialization
                if (!character.voice_actors.isNullOrEmpty()) {
                    rvVoiceActors.adapter = VoiceActorAdapter(character.voice_actors, titleTextColor)
                    if(character.voice_actors.size > 1){
                        rvVoiceActors.addItemDecoration(DividerItemDecoration(itemView.context, DividerItemDecoration.HORIZONTAL))
                    }
                    rvVoiceActors.visibility = View.VISIBLE
                } else {
                    // no voice actors make the recyclerview gone
                    rvVoiceActors.visibility = View.GONE
                }
                rvVoiceActors.setHasFixedSize(true)
            }
        }

        fun bindStaff(staff: CharacterStaff){
            binding.apply {
                Glide.with(itemView)
                    .load(staff.image_url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivPerson)
                ivPerson.setOnClickListener {
                    listener.onStaffClick(staff.mal_id)
                }
                tvName.text = staff.name

                var positions = ""
                if(!staff.positions.isNullOrEmpty()) {
                    for(position in staff.positions){
                        positions += position + "\n"
                    }
                }
                tvInfo.text = positions
                rvVoiceActors.visibility = View.GONE
            }
        }
    }
}