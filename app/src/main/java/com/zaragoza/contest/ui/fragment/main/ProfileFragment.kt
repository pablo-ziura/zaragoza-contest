package com.zaragoza.contest.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentProfileBinding
import com.zaragoza.contest.domain.model.User
import com.zaragoza.contest.ui.viewmodel.GetUserInfoState
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import com.zaragoza.contest.utils.ResourceState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModel()

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

    }

    private fun initViewModel() {
        userViewModel.getUserInfoLiveData.observe(viewLifecycleOwner) { state ->
            handleUGetUserInfoState(state)
        }
        userViewModel.getUserInfo("2WTr1lvoeVO5RPa6EQ3iOPgcr2R2")
    }

    private fun handleUGetUserInfoState(state: GetUserInfoState) {
        when (state) {
            is ResourceState.Loading -> {
                //
            }

            is ResourceState.Success -> {
                initUI(state.result)
            }

            is ResourceState.Error -> {
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                //
            }
        }
    }

    fun initUI(user: User) {
        Log.i("USER INFO", user.email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}