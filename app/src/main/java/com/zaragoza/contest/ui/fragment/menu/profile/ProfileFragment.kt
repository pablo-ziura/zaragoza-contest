package com.zaragoza.contest.ui.fragment.menu.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.zaragoza.contest.databinding.FragmentProfileBinding
import com.zaragoza.contest.model.User
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.viewmodel.GetUserInfoState
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModel()

    private var currentUser: User? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleImageResult(result.resultCode, result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                //
            }

            is ResourceState.Success -> {
                currentUser = state.result
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

    private fun initUI(user: User) {
        _binding?.tvNicknameProfileFragment?.text = user.nickname
        _binding?.tvEmailProfileFragment?.text = user.email
        _binding?.tvScoreProfileFragment?.text = user.score.toString()
        user.urlImage?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(_binding?.ivUserImageProfileFragment!!)
        }

        binding.btnChangeUserImageProfileFragment.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun handleImageResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            _binding?.ivUserImageProfileFragment?.setImageURI(imageUri)
            currentUser?.urlImage = imageUri.toString()
            currentUser?.let { userViewModel.uploadProfileImage(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}