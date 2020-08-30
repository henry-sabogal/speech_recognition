package com.mastergenova.speechrecognition

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.mlkit.nl.languageid.LanguageIdentification
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TextToSpeechFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TextToSpeechFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextToSpeechFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var mTTS:TextToSpeech
    lateinit var tvLanguage:TextView
    lateinit var tvError:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_text_to_speech, container, false)

        mTTS = TextToSpeech(activity, TextToSpeech.OnInitListener { status ->
            if(status != TextToSpeech.ERROR){
                mTTS.setLanguage(Locale("spa"))
            }
        })

        val editText = root.findViewById<View>(R.id.textWritten) as EditText
        tvError = root.findViewById<View>(R.id.tvError) as TextView
        tvLanguage = root.findViewById<View>(R.id.tvLanguage) as TextView
        val speakbtn = root.findViewById<View>(R.id.speakBtn) as ImageButton
        speakbtn.setOnClickListener{
            if(editText.text.toString().trim().length>0){
                tvError.visibility = View.INVISIBLE
                languageIdentifier(editText.text.toString())
            }else{
                tvError.visibility = View.VISIBLE
                tvError.text = "Please, introduce a text"
            }
        }

        // Inflate the layout for this fragment
        return root
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun languageIdentifier(text: String){
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                if(languageCode == "und"){
                    Toast.makeText(activity, "Can't identify language", Toast.LENGTH_LONG).show()
                    tvError.visibility = View.VISIBLE
                    tvError.text = "Can't identify language"
                }else{
                    tvError.visibility = View.INVISIBLE
                    Toast.makeText(activity, "Language: $languageCode", Toast.LENGTH_LONG).show()
                    tvLanguage.text = "Language: $languageCode"
                    speak(text, languageCode)
                }

            }
            .addOnFailureListener{
                Toast.makeText(activity, "An error occurred", Toast.LENGTH_LONG).show()
                tvError.visibility = View.VISIBLE
                tvError.text = "An error occurred"
            }
    }

    fun speak(text: String, language: String){
        mTTS = TextToSpeech(activity, TextToSpeech.OnInitListener { status ->
            if(status != TextToSpeech.ERROR){
                mTTS.setLanguage(Locale(language))
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }else{
                Toast.makeText(activity, "An error occurred ${TextToSpeech.ERROR.toString()}", Toast.LENGTH_LONG).show()
            }
        })
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

    override fun onDestroy() {
        super.onDestroy()
        mTTS.shutdown()
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
         * @return A new instance of fragment TextToSpeechFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TextToSpeechFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
