package com.zaragoza.contest.ui.fragment.menu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zaragoza.contest.R
import com.zaragoza.contest.databinding.FragmentMenuBinding
import com.zaragoza.contest.ui.fragment.menu.scores.ScoresRanking
import com.zaragoza.contest.ui.viewmodel.QuestionViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val questionViewModel: QuestionViewModel by activityViewModel()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfileMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_profileFragment)
        }

        binding.btnBestScoresMenu.setOnClickListener {
            val intent = Intent(requireContext(), ScoresRanking::class.java)
            startActivity(intent)
        }

        binding.btnStartContestMenu.setOnClickListener {
            questionViewModel.resetGame()
            sharedPreferences.edit().putInt("currentUserScore", 0).apply()
            findNavController().navigate(R.id.action_menuFragment_to_questionDetailFragment)
        }

        binding.btnStartMapMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_bonusQuestionMapFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}