package com.example.transitionframeworksample.items

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.transitionframeworksample.NavigationActivity
import com.example.transitionframeworksample.R
import com.example.transitionframeworksample.item.ItemFragment
import com.example.transitionframeworksample.model.Item
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import kotlin.random.Random

class ItemsFragment : Fragment() {

    private val executorService = Executors.newSingleThreadExecutor()
    private var adapter: ItemsAdapter? = null
    private val data = CopyOnWriteArrayList<Item>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        if (adapter == null) {
            adapter = ItemsAdapter(::onItemClicked)
            generateItems { items ->
                data.addAll(items)
                adapter?.setItems(items)
                view.findViewById<RecyclerView>(R.id.recycler).let { recycler ->
                    recycler.adapter = adapter
                    recycler.animate()
                        .setDuration(350L)
                        .setInterpolator(AccelerateInterpolator())
                        .alpha(1f)
                        .withEndAction { recycler?.isVisible = true }
                        .start()
                }
            }
        } else {
            view.findViewById<RecyclerView>(R.id.recycler).let { recycler ->
                recycler.alpha = 1f
                recycler.adapter = adapter
            }
        }
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun onItemClicked(item: Item, transitions: List<Pair<View, String>>) {
        val fragment =
            ItemFragment.newInstance(item, transitions.map { it.first.tag as String to it.second })

        (activity as NavigationActivity).showFragment(
            from = this,
            to = fragment,
            transitions = transitions,
            "ItemFragmentTag"
        )
    }

    private fun generateItems(onReady: (List<Item>) -> Unit) {
        executorService.execute {
            val drawables: List<Int> = List(10) {
                resources.getIdentifier(
                    "img_$it",
                    "drawable",
                    requireContext().packageName
                )
            }

            val strings: List<Int> = List(9) {
                resources.getIdentifier(
                    "sample_text_$it",
                    "string",
                    requireContext().packageName
                )
            }

            val random = Random(1)
            val items = List(10000) {
                Item(
                    imageRes = drawables[random.nextInt(10)],
                    stringRes = strings[random.nextInt(9)]
                )
            }

            Handler(Looper.getMainLooper()).post {
                onReady.invoke(items)
            }
        }
    }
}