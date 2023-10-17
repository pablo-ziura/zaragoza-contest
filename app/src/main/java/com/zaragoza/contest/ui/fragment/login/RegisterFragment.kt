package com.zaragoza.contest.ui.fragment.login

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentRegisterBinding
import com.zaragoza.contest.model.User
import com.zaragoza.contest.ui.common.ResourceState
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

        _binding?.btnActionSignIn?.setOnClickListener {
            if (validateInputs()) {
                createUser()
            }
        }
    }

    private fun initViewModel() {
        userViewModel.createUserLiveData.observe(viewLifecycleOwner) { state ->
            handleUserCreationState(state)
        }
    }

    private fun handleUserCreationState(state: CreateUserState) {
        when (state) {
            is ResourceState.Loading -> {
                //
            }

            is ResourceState.Success -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Nuevo Usuario")
                    .setMessage("¡Usuario creado con éxito!")
                    .setPositiveButton("OK") { _, _ ->
                        parentFragmentManager.popBackStack()
                    }
                    .show()
            }

            is ResourceState.Error -> {
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                //
            }
        }
    }

    private fun createUser() {
        val userNickname = binding.tilEditNicknameRegister.text.toString()
        val userPassword = binding.tilEditPasswordRegister.text.toString()
        val userEmail = binding.tilInputMailRegister.text.toString()

        userViewModel.createUser(
            User(
                id = "1",
                email = userEmail,
                nickname = userNickname,
                password = userPassword
            )
        )
    }

    private fun validateInputs(): Boolean {
        val userNickname = binding.tilEditNicknameRegister.text.toString()
        val userPassword = binding.tilEditPasswordRegister.text.toString()
        val userConfirmPassword = binding.tilConfirmPasswordRegister.text.toString()
        val userEmail = binding.tilInputMailRegister.text.toString()

        if (userNickname.isBlank() || userPassword.isBlank() || userConfirmPassword.isBlank() || userEmail.isBlank()) {
            Toast.makeText(requireContext(), "Debe rellenar todos los campos", Toast.LENGTH_LONG)
                .show()
            return false
        }

        if (userPassword != userConfirmPassword) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG)
                .show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(requireContext(), "Formato de e-mail incorrecto", Toast.LENGTH_LONG)
                .show()
            return false
        }

        if (userPassword.length < 6) {
            Toast.makeText(
                requireContext(),
                "La contraseña debe tener al menos 6 caracteres",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}