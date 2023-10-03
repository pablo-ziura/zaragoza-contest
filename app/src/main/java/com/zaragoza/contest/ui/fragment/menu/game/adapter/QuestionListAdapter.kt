package com.zaragoza.contest.ui.fragment.menu.game.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zaragoza.contest.databinding.RowQuestionListItemBinding
import com.zaragoza.contest.model.Question

class QuestionListAdapter : RecyclerView.Adapter<QuestionListAdapter.QuestionListViewHolder>() {

    private var questionList: List<Question> = emptyList()
    var onClickListener: (Question) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionListViewHolder {
        val binding =
            RowQuestionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
        val question = questionList[position]

        holder.questionNumberTextView.text = question.statement
        holder.questionIdTextView.text = question.id.toString()

        holder.rootView.setOnClickListener {
            onClickListener.invoke(question)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Question>) {
        questionList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    inner class QuestionListViewHolder(binding: RowQuestionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val rootView = binding.root
        val questionNumberTextView = binding.tvQuestionNrList
        val questionIdTextView = binding.tvQuestionIdList
    }
}