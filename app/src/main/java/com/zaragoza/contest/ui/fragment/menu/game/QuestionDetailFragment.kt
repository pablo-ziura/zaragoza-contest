package com.zaragoza.contest.ui.fragment.menu.game

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zaragoza.contest.R
import com.zaragoza.contest.databinding.FragmentQuestionDetailBinding
import com.zaragoza.contest.model.Question
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.viewmodel.GetQuestionListState
import com.zaragoza.contest.ui.viewmodel.QuestionViewModel
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class QuestionDetailFragment : Fragment() {

    private var _binding: FragmentQuestionDetailBinding? = null
    private val binding get() = _binding!!

    private val questionTextViews by lazy {
        listOf(
            binding.tvQuestionNrOneInfoFragment,
            binding.tvQuestionNrTwoInfoFragment,
            binding.tvQuestionNrThreeInfoFragment,
            binding.tvQuestionNrFourInfoFragment
        )
    }

    private val questionViewModel: QuestionViewModel by activityViewModel()
    private val scoreViewModel: ScoreViewModel by activityViewModel()

    private var currentQuestion: Question? = null

    private var lastScore: Int = 0

    companion object {
        const val TOTAL_TIME = 20500L
        const val MIN_SCORE = 1000
        const val MAX_SCORE = 5000
    }

    private var timer: CountDownTimer? = null

    private var selectedAnswer: String? = null

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
        setupNextQuestionButton()

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
                binding.spinnerQuestionDetailFragment.visibility = View.VISIBLE
            }

            is ResourceState.Success -> {
                binding.spinnerQuestionDetailFragment.visibility = View.GONE
                if (currentQuestionIndex < state.result.size) {
                    val question = state.result[currentQuestionIndex]
                    currentQuestion = question
                    initUI(question)
                } else {
                    navigateToBonusQuestionFragment()
                }
            }

            is ResourceState.Error -> {
                binding.spinnerQuestionDetailFragment.visibility = View.GONE
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                binding.spinnerQuestionDetailFragment.visibility = View.GONE
            }
        }
    }

    private fun initUI(question: Question) {

        bindNewQuestion(question)
        setupClickListeners()
        setupTimer()

    }

    private fun bindNewQuestion(question: Question) {
        binding.apply {
            tvStatementQuestionInfoFragment.text = question.statement
            tvQuestionNrOneInfoFragment.text = question.firstAnswer
            tvQuestionNrTwoInfoFragment.text = question.secondAnswer
            tvQuestionNrThreeInfoFragment.text = question.thirdAnswer
            tvQuestionNrFourInfoFragment.text = question.fourthAnswer

            questionTextViews.forEach { it.isClickable = true }
        }
    }

    private fun setupClickListeners() {
        val clickListener = View.OnClickListener { view ->
            timer?.cancel()

            selectedAnswer = (view as TextView).text.toString()

            val timeDisplayed = binding.tvSecondsQuestionInfoFragment.text.toString().toLong()
            val timeElapsed =
                TOTAL_TIME - timeDisplayed * 1000

            val isCorrect = isAnswerCorrect(selectedAnswer, currentQuestion)

            val score = if (isCorrect) {
                calculateScore(timeElapsed)
            } else {
                0
            }

            updateUIWithResult(isCorrect, score)
        }

        questionTextViews.forEach { it.setOnClickListener(clickListener) }
    }

    private fun setupTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(TOTAL_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
                binding.tvSecondsQuestionInfoFragment.text = remainingSeconds.toString()
            }

            override fun onFinish() {
                updateUIWithResult(false, 0)
            }
        }.start()
    }

    private fun updateUIWithResult(isCorrect: Boolean, score: Int) {

        lastScore = score

        val resultMessage = if (isCorrect) {
            "¡Respuesta correcta!"
        } else {
            "Respuesta incorrecta."
        }

        currentQuestion?.let { showAlertDialog(resultMessage, it.answerDescription) }

        val scoreMessage = "Has obtenido $score puntos"

        binding.tvResultInfoFragment.text = resultMessage
        binding.tvCurrentScoreInfoFragment.text = scoreMessage

        questionTextViews.forEach { it.isClickable = false }

        binding.tvResultInfoFragment.visibility = View.VISIBLE
        binding.tvCurrentScoreInfoFragment.visibility = View.VISIBLE
        binding.btnNextQuestionInfoFragment.visibility = View.VISIBLE
    }

    private fun setupNextQuestionButton() {
        binding.btnNextQuestionInfoFragment.setOnClickListener {
            scoreViewModel.updateCurrentUserScore(lastScore)
            questionViewModel.getNextQuestion()
            setupTimer()
            binding.tvResultInfoFragment.visibility = View.INVISIBLE
            binding.tvCurrentScoreInfoFragment.visibility = View.INVISIBLE
            binding.btnNextQuestionInfoFragment.visibility = View.INVISIBLE
        }
    }

    private fun navigateToBonusQuestionFragment() {
        val score = scoreViewModel.fetchCurrentScore()
        val bundle = Bundle()
        bundle.putInt("finalScore", score)
        findNavController().navigate(
            R.id.action_questionDetailFragment_to_bonusQuestionMapFragment,
            bundle
        )
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
        val timeRange = TOTAL_TIME
        val scoreRange = MAX_SCORE - MIN_SCORE

        val timeLeft = TOTAL_TIME - timeElapsed
        return MIN_SCORE + ((timeLeft.toFloat() / timeRange) * scoreRange).toInt()
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        onPositiveClick: (() -> Unit)? = null
    ) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                onPositiveClick?.invoke()
            }
            .create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
    }
}