package com.moviebuff.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.moviebuff.MainActivity
import com.moviebuff.R
import com.moviebuff.data.model.Movie


class HorizontalPagerAdapter() : PagerAdapter() {

    private var dataSet = ArrayList<Movie>()

    fun setDataSet(dataSet  :ArrayList<Movie>){
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return  dataSet.size
    }


    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context).inflate(R.layout.row_movie, container, false)


        val img: ImageView = view.findViewById<View>(R.id.ivMain) as ImageView
        Glide.with(view)
            .load(dataSet[position].getImageUrl())
            .into( img)
        container.addView(view)

        view.findViewById<TextView>(R.id.tvMovieName).text = dataSet[position].original_title

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)

    }
}