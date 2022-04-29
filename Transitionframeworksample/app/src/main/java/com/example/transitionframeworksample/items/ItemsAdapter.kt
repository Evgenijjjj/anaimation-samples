package com.example.transitionframeworksample.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.transitionframeworksample.R
import com.example.transitionframeworksample.model.Item

class ItemsAdapter(private val onClick: (item: Item, List<Pair<View, String>>) -> Unit) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private var items: List<Item>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image_with_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        items?.let { holder.bind(it[position]) }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    internal fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal fun bind(model: Item) {
            val imageTransitionName = "$IMAGE_TRANSITION_PREF$adapterPosition"
            val imageView = itemView.findViewById<ImageView>(R.id.image)
            imageView.setImageResource(model.imageRes)
            imageView.transitionName = imageTransitionName

            val textTransitionName = "$TEXT_TRANSITION_PREF$adapterPosition"
            val textView = itemView.findViewById<TextView>(R.id.text)
            textView.transitionName = textTransitionName
            textView.setText(model.stringRes)

            itemView.setOnClickListener {
                onClick.invoke(
                    model,
                    listOf(
                        imageView to imageTransitionName,
                        textView to textTransitionName
                    )
                )
            }
        }
    }

    companion object {
        const val IMAGE_TRANSITION_PREF = "image_transition_"
        const val TEXT_TRANSITION_PREF = "text_transition_"
    }
}
