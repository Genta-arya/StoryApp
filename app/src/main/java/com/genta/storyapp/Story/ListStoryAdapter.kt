package com.genta.storyapp.Story



import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genta.storyapp.Data.Response.ResponseGetStory
import com.genta.storyapp.databinding.ItemBinding

class ListStoryAdapter: PagingDataAdapter<ResponseGetStory, ListStoryAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGetStory) {
            binding.apply {
                val dateList = data.createdAt!!.split("T")
                val dateListListStoryItem = dateList[0]
                binding.apply {
                    tvItemName.setText(data.name)
                    Glide.with(itemView.context)
                        .load(data.photoUrl)
                        .centerCrop()
                        .into(imgItemPhoto)
                    date.setText(dateListListStoryItem)
                    val listListStoryItemDetail = ResponseGetStory(data.id,data.name,data.description,data.photoUrl,data.createdAt,data.lat,data.lon)
                    Log.d("data:",listListStoryItemDetail.toString())

                    itemView.setOnClickListener {

                        val intent = Intent(itemView.context, DetailActivity::class.java)

                        intent.putExtra(DetailActivity.datas,data)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(imgItemPhoto,"imageListStoryItem"),
                                Pair(tvItemName,"nameListStoryItem"),
                                Pair(date,"dateListStoryItem"),
                            )
                        itemView.context.startActivity(intent, optionsCompat.toBundle())
                    }
                }

            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResponseGetStory>() {
            override fun areItemsTheSame(oldItem: ResponseGetStory, newItem: ResponseGetStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ResponseGetStory, newItem: ResponseGetStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}