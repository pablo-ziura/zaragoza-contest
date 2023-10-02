package com.zaragoza.contest.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentQuestionInfoBinding

class QuestionInfoFragment : Fragment() {

    private var _binding: FragmentQuestionInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionInfoBinding.inflate(inflater)
        return binding.root
    }

}