package com.zaragoza.contest.ui.fragment.menu.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaragoza.contest.databinding.FragmentQuestionListBinding
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.fragment.menu.game.adapter.QuestionListAdapter
import com.zaragoza.contest.ui.viewmodel.GetQuestionListState
import com.zaragoza.contest.ui.viewmodel.QuestionViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class QuestionListFragment : Fragment() {

    private var _binding: FragmentQuestionListBinding? = null
    private val binding get() = _binding!!

    private val questionListAdapter = QuestionListAdapter()

    private val questionViewModel: QuestionViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionListAdapter.onClickListener = { question ->
            findNavController().navigate(
                QuestionListFragmentDirections.actionQuestionListFragmentToQuestionDetailFragment(
                    question.id
                )
            )
        }

        initViewModel()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.rvQuestionsList.adapter = questionListAdapter
        binding.rvQuestionsList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initViewModel() {
        questionViewModel.getQuestionListLiveData.observe(viewLifecycleOwner) { state ->
            handleGetQuestionListState(state)
        }
        questionViewModel.getQuestionList()
    }

    private fun handleGetQuestionListState(state: GetQuestionListState) {
        when (state) {
            is ResourceState.Loading -> {
                Log.i("RESPONSE", "CARGANDO")
            }

            is ResourceState.Success -> {
                questionListAdapter.submitList(state.result)
            }

            is ResourceState.Error -> {
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                //
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}