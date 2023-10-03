package com.zaragoza.contest.ui.fragment.menu.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zaragoza.contest.databinding.FragmentQuestionDetailBinding

class QuestionDetailFragment : Fragment() {

    private var _binding: FragmentQuestionDetailBinding? = null
    private val binding get() = _binding!!

    private val args: QuestionDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("PREGUNTA", args.questionId.toString())

    }

}