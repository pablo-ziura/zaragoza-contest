package com.zaragoza.contest.ui.fragment.menu.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.zaragoza.contest.databinding.FragmentQuestionDetailBinding
import com.zaragoza.contest.model.Question
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.viewmodel.GetQuestionListState
import com.zaragoza.contest.ui.viewmodel.QuestionViewModel
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class QuestionDetailFragment : Fragment() {

    companion object {
        const val TOTAL_TIME = 30000L
        const val MIN_SCORE = 500
        const val MAX_SCORE = 1000
    }

    private var _binding: FragmentQuestionDetailBinding? = null
    private val binding get() = _binding!!

    private val questionViewModel: QuestionViewModel by activityViewModel()
    private val scoreViewModel: ScoreViewModel by activityViewModel()

    private var currentQuestion: Question? = null

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

        initViewModel()
    }

    private fun initViewModel() {
        questionViewModel.getQuestionListLiveData.observe(viewLifecycleOwner) { state ->
            handleGetQuestionListState(state)
        }
        questionViewModel.getQuestionList()
    }

    private fun handleGetQuestionListState(state: GetQuestionListState) {
        val currentQuestionIndex = questionViewModel.getCurrentQuestionIndex()
        when (state) {
            is ResourceState.Loading -> {
                Log.i("RESPONSE", "CARGANDO")
            }

            is ResourceState.Success -> {
                if (currentQuestionIndex < state.result.size) {
                    val question = state.result[currentQuestionIndex]
                    currentQuestion = question
                    initUI(question)
                } else {
                    showFinalScoreDialog()
                }
            }

            is ResourceState.Error -> {
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                //
            }
        }
    }

    private fun initUI(question: Question) {

        startTime = System.currentTimeMillis()

        bindNewQuestion(question)
        setupTimer()
        setupClickListeners()

    }

    private fun bindNewQuestion(question: Question) {
        binding.apply {
            tvStatementQuestionInfoFragment.text = question.statement
            tvQuestionNrOneInfoFragment.text = question.firstAnswer
            tvQuestionNrTwoInfoFragment.text = question.secondAnswer
            tvQuestionNrThreeInfoFragment.text = question.thirdAnswer
            tvQuestionNrFourInfoFragment.text = question.fourthAnswer
        }
    }

    private fun setupTimer() {
        val elapsedMillis = System.currentTimeMillis() - startTime
        if (elapsedMillis < TOTAL_TIME) {
            val remainingMillis = TOTAL_TIME - elapsedMillis
            val progress = ((remainingMillis / TOTAL_TIME.toFloat()) * 100).toInt()
            binding.pbTimeQuestionInfoFragment.progress = 100 - progress
        }
    }

    private fun setupClickListeners() {
        val clickListener = View.OnClickListener { view ->
            selectedAnswer = (view as TextView).text.toString()
            responseTime = System.currentTimeMillis() - startTime

            val isCorrect = isAnswerCorrect(selectedAnswer, currentQuestion)

            val score = if (isCorrect) {
                responseTime?.let { timeElapsed -> calculateScore(timeElapsed) } ?: 0
            } else {
                0
            }

            showAnswerDialog(isCorrect, score)

        }

        binding.tvQuestionNrOneInfoFragment.setOnClickListener(clickListener)
        binding.tvQuestionNrTwoInfoFragment.setOnClickListener(clickListener)
        binding.tvQuestionNrThreeInfoFragment.setOnClickListener(clickListener)
        binding.tvQuestionNrFourInfoFragment.setOnClickListener(clickListener)
    }

    private fun showAnswerDialog(isCorrect: Boolean, score: Int) {
        val message = if (isCorrect) {
            "¡Respuesta correcta! Tu puntuación es $score."
        } else {
            "Respuesta incorrecta. Tu puntuación es $score."
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Resultado")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                scoreViewModel.updateCurrentUserScore(score)
                questionViewModel.getNextQuestion()
            }
            .create()
            .show()
    }

    private fun showFinalScoreDialog() {

        val score = scoreViewModel.fetchCurrentScore()

        AlertDialog.Builder(requireContext())
            .setTitle("Resultado")
            .setMessage("Puntuación final: $score")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                parentFragmentManager.popBackStack()
            }
            .create()
            .show()
    }

    private fun isAnswerCorrect(selected: String?, question: Question?): Boolean {
        val correctIndex = question?.rightAnswer ?: return false
        val correctAnswer = when (correctIndex) {
            1 -> question.firstAnswer
            2 -> question.secondAnswer
            3 -> question.thirdAnswer
            4 -> question.fourthAnswer
            else -> return false
        }
        return selected == correctAnswer
    }

    private fun calculateScore(timeElapsed: Long): Int {
        return MIN_SCORE + ((TOTAL_TIME - timeElapsed).toFloat() / (TOTAL_TIME - 1000) * (MAX_SCORE - MIN_SCORE)).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}