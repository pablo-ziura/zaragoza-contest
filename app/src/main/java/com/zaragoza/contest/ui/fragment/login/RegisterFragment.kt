package com.zaragoza.contest.ui.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentRegisterBinding
import com.zaragoza.contest.domain.model.User
import com.zaragoza.contest.ui.viewmodel.CreateUserState
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        _binding?.btnActionRegister?.setOnClickListener {
            createUser()
        }
    }

    private fun initViewModel() {
        userViewModel.createUserLiveData.observe(viewLifecycleOwner) { state ->
            handleUserCreationState(state)
        }
    }

    private fun handleUserCreationState(state: CreateUserState) {
        Log.i("USER STATE", state.toString())
    }

    private fun createUser() {
        val userNickname = binding.tilEditNicknameRegister.text.toString()
        val userPassword = binding.tilEditPasswordRegister.text.toString()
        val userEmail = binding.tilEditMailRegister.text.toString()

        userViewModel.createUser(
            User(
                id = "1",
                email = userEmail,
                nickname = userNickname,
                password = userPassword
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}