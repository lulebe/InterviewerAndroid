package de.lulebe.interviewer.ui.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.QuestionData
import de.lulebe.interviewer.data.QuestionDataMC
import de.lulebe.interviewer.ui.helpers.ReorderRecyclerviewCallback

class CreateMCOptionsAdapter(
        val deleteCb: (QuestionDataMC) -> Unit
) : RecyclerView.Adapter<CreateMCOptionsAdapter.ViewHolder>() {

    var items = mutableListOf<QuestionData>()
    private val ith = ItemTouchHelper(ReorderRecyclerviewCallback({ from, to ->
        val item = items[from]
        items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }))

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        ith.attachToRecyclerView(recyclerView)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val l = LayoutInflater.from(parent.context).inflate(R.layout.item_createquestion_mcoption, parent, false)
        return ViewHolder(l)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position] as QuestionDataMC
        holder.tvTitle.text = item.option
        holder.tvTitle.setTextColor(
                if (item.success)
                    Color.GREEN
                else
                    Color.RED
        )
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        private val deleteBtn = view.findViewById<View>(R.id.btn_delete)
        private val moveHandle = view.findViewById<View>(R.id.reorder_handle)
        init {
            deleteBtn.setOnClickListener {
                val pos = adapterPosition
                deleteCb(items[pos] as QuestionDataMC)
                notifyItemRemoved(pos)
            }
            moveHandle.setOnTouchListener { view, motionEvent ->
                if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN)
                    ith.startDrag(this)
                false
            }
        }
    }

}