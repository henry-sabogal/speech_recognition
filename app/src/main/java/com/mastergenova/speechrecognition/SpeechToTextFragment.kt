package com.mastergenova.speechrecognition

import android.app.Activity
import android.app.Instrumentation
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SpeechToTextFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SpeechToTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpeechToTextFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var speechRecognizer:SpeechRecognizer
    lateinit var et_text:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_speech_to_text, container, false)

        et_text = root.findViewById<View>(R.id.textSpoken) as TextView
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)

        val speakBtn = root.findViewById<View>(R.id.speechBtn) as Button
        speakBtn.setOnClickListener{
            val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("spa"))
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            try {
                startActivityForResult(speechRecognizerIntent, REQUEST_CODE_STT)
            }catch (e: ActivityNotFoundException){
                e.printStackTrace()
                Toast.makeText(activity, "Your device does not support STT", Toast.LENGTH_LONG).show()
            }
        }


        // Inflate the layout for this fragment
        return root
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
            listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_STT -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if(!result.isNullOrEmpty()){
                        val recognizedText = result[0]
                        et_text.setText(recognizedText)
                    }
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SpeechToTextFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                SpeechToTextFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

        private const val REQUEST_CODE_STT = 1
    }
}
