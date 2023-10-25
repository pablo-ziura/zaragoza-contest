package com.zaragoza.contest.ui.fragment.menu.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.zaragoza.contest.R
import com.zaragoza.contest.databinding.FragmentFinalScoreBinding
import com.zaragoza.contest.model.User
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.viewmodel.GetUserInfoState
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FinalScoreFragment : Fragment() {

    private var _binding: FragmentFinalScoreBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModel()
    private val scoreViewModel: ScoreViewModel by activityViewModel()

    private var finalScore: Int? = 0
    private var updatedUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinalScoreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finalScore = scoreViewModel.fetchCurrentScore()

        initUserViewModel()

    }

    private fun initUserViewModel() {
        userViewModel.getUserInfoLiveData.observe(viewLifecycleOwner) { state ->
            handleGetUserInfoState(state)
        }
        val userId = userViewModel.fetchUserId()
        if (userId != null) {
            userViewModel.getUserInfo(userId)
        }
    }

    private fun handleGetUserInfoState(state: GetUserInfoState) {

        when (state) {
            is ResourceState.Loading -> {
                binding.spinnerFinalScoreFragment.visibility = View.VISIBLE
            }

            is ResourceState.Success -> {
                binding.spinnerFinalScoreFragment.visibility = View.GONE
                val user = state.result
                if (user.score != null && user.score!! < finalScore!!) {
                    user.score = finalScore
                    updatedUser = user
                    userViewModel.editUser(user)
                }
                finalScore?.let { initUI(user, it) }
            }

            is ResourceState.Error -> {
                binding.spinnerFinalScoreFragment.visibility = View.GONE
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                binding.spinnerFinalScoreFragment.visibility = View.GONE
            }
        }
    }

    private fun initUI(user: User, score: Int) {

        binding.tvPointsFinalScoreFragment.text =
            getString(R.string.current_mark_final_score_fragment, score)

        if (score > (user.score ?: 0)) {
            binding.tvMaxFinalScoreFragment.text =
                getString(R.string.max_mark_obtained_final_score_fragment)
        } else {
            binding.tvMaxFinalScoreFragment.text =
                getString(R.string.max_mark_final_score_fragment, user.score.toString())
        }

        binding.btnBackMenuFinalScoreFragment.setOnClickListener {
            findNavController().navigate(
                R.id.action_finalScoreFragment_to_menuFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.finalScoreFragment, true)
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}