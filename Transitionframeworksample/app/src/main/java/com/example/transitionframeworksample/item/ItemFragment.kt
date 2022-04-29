package com.example.transitionframeworksample.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.transitionframeworksample.R
import com.example.transitionframeworksample.model.Item

class ItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = requireNotNull(arguments?.getParcelable<Item>(EXTRA_ITEM))
        val tags = requireNotNull(arguments?.getStringArray(EXTRA_TRANSITIONS_TAGS))
        val values = requireNotNull(arguments?.getStringArray(EXTRA_TRANSITIONS_VALUES))

        for (tag in tags.withIndex()) {
            view.findViewWithTag<View>(tag.value)?.transitionName = values[tag.index]
        }

        view.findViewById<ImageView>(R.id.image).setImageResource(item.imageRes)
        view.findViewById<TextView>(R.id.text).setText(item.stringRes)
    }

    companion object {

        private const val EXTRA_ITEM = "item"
        private const val EXTRA_TRANSITIONS_TAGS = "tags"
        private const val EXTRA_TRANSITIONS_VALUES = "values"

        fun newInstance(item: Item, transitions: List<Pair<String, String>>): ItemFragment {
            val fragment = ItemFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_ITEM, item)
                putStringArray(EXTRA_TRANSITIONS_TAGS, transitions.map { it.first }.toTypedArray())
                putStringArray(
                    EXTRA_TRANSITIONS_VALUES,
                    transitions.map { it.second }.toTypedArray()
                )
            }

            return fragment
        }
    }
}