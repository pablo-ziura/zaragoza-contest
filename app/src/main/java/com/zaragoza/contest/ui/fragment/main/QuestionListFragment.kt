package com.zaragoza.contest.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentQuestionListBinding

class QuestionListFragment : Fragment() {

    private var _binding: FragmentQuestionListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionListBinding.inflate(inflater)
        return binding.root
    }

}