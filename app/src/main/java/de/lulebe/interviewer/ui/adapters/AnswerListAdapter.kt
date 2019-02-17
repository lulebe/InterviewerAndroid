package de.lulebe.interviewer.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.AnswerDataText
import de.lulebe.interviewer.data.AnswerType
import de.lulebe.interviewer.data.AnswerWithData



class AnswerListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var answers : List<AnswerWithData> = emptyList()
    private var answerType = AnswerType.TEXT

    fun setAnswerList(ansList: List<AnswerWithData>) {
        answers = ansList
        notifyDataSetChanged()
    }

    fun setAnswerType(t: AnswerType) {
        answerType = t
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (answerType) {
            else -> TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_answerlist_answer_text, parent, false))
        }
    }

    override fun getItemCount() = answers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val answer = answers[position]
        val formatter = java.text.DateFormat.getDateInstance(
                java.text.DateFormat.DEFAULT)
        formatter.timeZone = answer.validSince.timeZone
        val formattedTime = formatter.format(answer.validSince.time)
        when (holder) {
            is TextViewHolder -> {
                val answerText = answer as AnswerDataText.AnswerWithTextData
                holder.tvTime.text = formattedTime
                holder.tvText.text = answerText.data
            }
        }
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        val tvText = itemView.findViewById<TextView>(R.id.tv_text)
    }

}