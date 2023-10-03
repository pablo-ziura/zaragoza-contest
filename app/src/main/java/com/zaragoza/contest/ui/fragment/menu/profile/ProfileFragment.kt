package com.zaragoza.contest.ui.fragment.menu.profile

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentProfileBinding
import com.zaragoza.contest.model.User
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.viewmodel.GetUserInfoState
import com.zaragoza.contest.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModel()

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initCamera()

    }

    private fun initViewModel() {
        userViewModel.getUserInfoLiveData.observe(viewLifecycleOwner) { state ->
            handleGetUserInfoState(state)
        }
        userViewModel.getUserInfo("2WTr1lvoeVO5RPa6EQ3iOPgcr2R2")
    }

    private fun handleGetUserInfoState(state: GetUserInfoState) {
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

    private fun initUI(user: User) {
        _binding?.tvNicknameProfileFragment?.text = user.nickname
        _binding?.tvEmailProfileFragment?.text = user.email
        _binding?.tvScoreProfileFragment?.text = user.score.toString()
        _binding?.btnChangeUserImageProfileFragment?.setOnClickListener {
            if (checkPermissions()) {
                _binding?.pvPhotoProfileFragment?.visibility = View.VISIBLE
                startCamera()
            } else {
                requestPermissions()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(_binding?.pvPhotoProfileFragment?.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun startCamera() {

        val imageCapture = imageCapture ?: return

        _binding?.btnCapturePhoto?.setOnClickListener {

            val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
                .format(System.currentTimeMillis())
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
                }
            }

            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    requireContext().contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                .build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
                    }
                }
            )
            _binding?.pvPhotoProfileFragment?.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}