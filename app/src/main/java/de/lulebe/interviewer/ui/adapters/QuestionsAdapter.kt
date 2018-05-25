package de.lulebe.interviewer.ui.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.Question
import de.lulebe.interviewer.ui.views.CutCornersCardView

class QuestionsAdapter(
    val clickCb: (Question) -> Unit,
    val answersCb: (Question) -> Unit,
    val editCb: (Question) -> Unit,
    val deleteCb: (Question) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    private var questions : List<Question>? = null

    fun setNewList(list: List<Question>?) {
        list?.let { newList ->
            val diffResult = DiffUtil.calculateDiff(object: DiffUtil.Callback() {
                override fun getOldListSize() = questions?.size ?: 0
                override fun getNewListSize() = newList.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return questions?.get(oldItemPosition) == newList[newItemPosition]
                }
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return questions?.get(oldItemPosition)?.id == newList[newItemPosition].id
                }
            })
            questions = list
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false))
    }

    override fun getItemCount() = questions?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.root.hideMenu()
        questions?.get(position)?.let { question ->
            holder.tvName.text = question.question
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView as CutCornersCardView
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)!!
        init {
            root.setOnClickListener {
                questions?.get(adapterPosition)?.let {
                    if (root.isMenuVisible())
                        root.toggleMenu()
                    else
                        clickCb(it)
                }
            }
            itemView.findViewById<View>(R.id.btn_answers).setOnClickListener {
                questions?.get(adapterPosition)?.let { answersCb(it) }
            }
            itemView.findViewById<View>(R.id.btn_edit).setOnClickListener {
                questions?.get(adapterPosition)?.let { editCb(it) }
            }
            itemView.findViewById<View>(R.id.btn_delete).setOnClickListener {
                questions?.get(adapterPosition)?.let { deleteCb(it) }
            }
        }
    }

}