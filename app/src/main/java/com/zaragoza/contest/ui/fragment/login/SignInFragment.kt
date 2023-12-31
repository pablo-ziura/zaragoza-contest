package com.zaragoza.contest.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zaragoza.contest.R
import com.zaragoza.contest.databinding.FragmentSignInBinding
import com.zaragoza.contest.ui.MenuActivity
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.viewmodel.CheckUserState
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        binding.btnActionSignIn.setOnClickListener {
            checkUser()
        }

        binding.btnRegisterSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
        }

    }

    private fun initViewModel() {
        userViewModel.checkUserLiveData.observe(viewLifecycleOwner) { state ->
            handleCheckUserState(state)
        }
    }

    private fun handleCheckUserState(state: CheckUserState) = when (state) {
        is ResourceState.Loading -> {
            binding.spinnerSignInFragment.visibility = View.VISIBLE
        }

        is ResourceState.Success -> {
            binding.spinnerSignInFragment.visibility = View.GONE
            val userId = state.result
            if (userId != null) {
                when (userViewModel.saveUserId(userId)) {
                    is ResourceState.Success -> {
                        Log.i("SignInFragment", userId)
                        val intent = Intent(requireContext(), MenuActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {
                        Log.e("SignInFragment", "Error al guardar el User ID")
                    }
                }
                val intent = Intent(requireContext(), MenuActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Usuario o contraseña incorrectos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        is ResourceState.Error -> {
            binding.spinnerSignInFragment.visibility = View.GONE
            Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
        }

        is ResourceState.None -> {
            binding.spinnerSignInFragment.visibility = View.GONE
        }
    }

    private fun checkUser() {
        val userPassword = binding.tilInputPasswordRegister.text.toString()
        val userEmail = binding.tilInputMailRegister.text.toString()

        if (userEmail.isBlank() || userPassword.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Por favor, rellene todos los campos",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        userViewModel.checkUser(userEmail = userEmail, userPassword = userPassword)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}