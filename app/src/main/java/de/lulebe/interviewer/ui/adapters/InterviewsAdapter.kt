package de.lulebe.interviewer.ui.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.Interview
import de.lulebe.interviewer.ui.views.CutCornersCardView

class InterviewsAdapter(
        val clickCb: (Interview) -> Unit,
        val editCb: (Interview) -> Unit,
        val deleteCb: (Interview) -> Unit
) : RecyclerView.Adapter<InterviewsAdapter.ViewHolder>() {

    private var interviews : List<Interview>? = null

    fun setNewList(list: List<Interview>?) {
        list?.let { newList ->
            val diffResult = DiffUtil.calculateDiff(object: DiffUtil.Callback() {
                override fun getOldListSize() = interviews?.size ?: 0
                override fun getNewListSize() = newList.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return interviews?.get(oldItemPosition) == newList[newItemPosition]
                }
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return interviews?.get(oldItemPosition)?.id == newList[newItemPosition].id
                }
            })
            interviews = list
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_interview, parent, false))
    }

    override fun getItemCount() = interviews?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.root.hideMenu()
        interviews?.get(position)?.let { interview ->
            holder.tvName.text = interview.name
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView as CutCornersCardView
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)!!
        init {
            root.setOnClickListener {
                interviews?.get(adapterPosition)?.let {
                        if (root.isMenuVisible())
                            root.toggleMenu()
                        else
                            clickCb(it)
                }
            }
            itemView.findViewById<View>(R.id.btn_edit).setOnClickListener {
                interviews?.get(adapterPosition)?.let { editCb(it) }
            }
            itemView.findViewById<View>(R.id.btn_delete).setOnClickListener {
                interviews?.get(adapterPosition)?.let { deleteCb(it) }
            }
        }
    }

}