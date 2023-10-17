package com.zaragoza.contest.ui.fragment.menu.game

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zaragoza.contest.databinding.FragmentQuestionDetailBinding

class QuestionDetailFragment : Fragment() {

    companion object {
        const val TOTAL_TIME = 30000L
        const val MIN_SCORE = 500
        const val MAX_SCORE = 1000
    }

    private var _binding: FragmentQuestionDetailBinding? = null
    private val binding get() = _binding!!

    private val args: QuestionDetailFragmentArgs by navArgs()

    private val handler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0L
    private var selectedAnswer: String? = null
    private var responseTime: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupTimer()
        setupClickListeners()
    }

    private fun initUI() {
        val question = args.question
        binding.apply {
            tvStatementQuestionInfoFragment.text = question.statement
            tvQuestionNrOneInfoFragment.text = question.firstAnswer
            tvQuestionNrTwoInfoFragment.text = question.secondAnswer
            tvQuestionNrThreeInfoFragment.text = question.thirdAnswer
            tvQuestionNrFourInfoFragment.text = question.fourthAnswer
        }
        startTime = System.currentTimeMillis()
    }

    private fun setupTimer() {
        handler.post(object : Runnable {
            override fun run() {
                val elapsedMillis = System.currentTimeMillis() - startTime
                if (elapsedMillis < TOTAL_TIME) {
                    val remainingMillis = TOTAL_TIME - elapsedMillis
                    val progress = ((remainingMillis / TOTAL_TIME.toFloat()) * 100).toInt()
                    binding.pbTimeQuestionInfoFragment.progress = 100 - progress

                    handler.postDelayed(this, 1000)
                } else {
                    handler.removeCallbacksAndMessages(null)
                    showDialog()
                }
            }
        })
    }

    private fun setupClickListeners() {
        val clickListener = View.OnClickListener { view ->
            selectedAnswer = (view as TextView).text.toString()
            responseTime = System.currentTimeMillis() - startTime

            handler.removeCallbacksAndMessages(null)
            showDialog()
        }

        binding.tvQuestionNrOneInfoFragment.setOnClickListener(clickListener)
        binding.tvQuestionNrTwoInfoFragment.setOnClickListener(clickListener)
        binding.tvQuestionNrThreeInfoFragment.setOnClickListener(clickListener)
        binding.tvQuestionNrFourInfoFragment.setOnClickListener(clickListener)
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        val correctAnswerText = getCorrectAnswerText()

        val isCorrect = selectedAnswer == correctAnswerText
        val timeTaken = responseTime ?: TOTAL_TIME
        val playerScore = if (isCorrect) calculateScore(timeTaken) else 0

        alertDialog.setTitle(if (isCorrect) "Respuesta correcta" else "Respuesta incorrecta")

        val timeTakenMessage = "Tiempo en responder: ${timeTaken / 1000} segundos"
        if (selectedAnswer != null) {
            alertDialog.setMessage("Respuesta seleccionada: $selectedAnswer\n$timeTakenMessage\nPuntuación: $playerScore")
        } else {
            alertDialog.setMessage("Tiempo agotado, $timeTakenMessage\nPuntuación: $playerScore")
        }

        alertDialog.setPositiveButton("OK") { _, _ ->
            findNavController().popBackStack()
        }

        alertDialog.show()
    }

    private fun getCorrectAnswerText(): String {
        return when (args.question.rightAnswer) {
            1 -> args.question.firstAnswer
            2 -> args.question.secondAnswer
            3 -> args.question.thirdAnswer
            4 -> args.question.fourthAnswer
            else -> throw IllegalArgumentException("Respuesta correcta no válida")
        }
    }

    private fun calculateScore(timeElapsed: Long): Int {
        return MIN_SCORE + ((TOTAL_TIME - timeElapsed).toFloat() / (TOTAL_TIME - 1000) * (MAX_SCORE - MIN_SCORE)).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}