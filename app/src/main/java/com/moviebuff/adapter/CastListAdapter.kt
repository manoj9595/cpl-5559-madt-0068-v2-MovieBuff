package com.moviebuff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviebuff.R
import com.moviebuff.data.model.Cast
import de.hdodenhof.circleimageview.CircleImageView

class CastListAdapter :RecyclerView.Adapter<CastListAdapter.ViewHolder>(){

    var mDataSet = ArrayList<Cast>()

    fun setDataSet(it: java.util.ArrayList<Cast>) {
        mDataSet = it
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvType = itemView.findViewById<TextView>(R.id.tvType)
        val tvCharacterName = itemView.findViewById<TextView>(R.id.tvCharacterName)
        val ivMain = itemView.findViewById<CircleImageView>(R.id.ivMain)
        fun bind(cast: Cast) {
            tvName.text = "Name : ${cast.name}"


            tvCharacterName.text = "Character : ${if(cast.character.isNullOrEmpty()) "N/A" else cast.character}"

            Glide.with(ivMain)
                .load(cast.getImageUrl())
                .placeholder(R.drawable.ic_default_profile)
                .error(R.drawable.ic_default_profile)
                .into(ivMain)

            tvType.text = if(cast.department==null) "Cast" else "Crew : ${cast.department}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_cast,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDataSet[position])
    }


}