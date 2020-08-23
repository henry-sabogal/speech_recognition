package com.mastergenova.speechrecognition

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.widget.Toolbar
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), AboutFragment.OnFragmentInteractionListener, TextToSpeechFragment.OnFragmentInteractionListener, SpeechToTextFragment.OnFragmentInteractionListener {

    val MY_DATA_CHECK_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(navController)

        val checkIntent = Intent()
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA)
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE)

    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode != MY_DATA_CHECK_CODE){
            val installIntent = Intent()
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
            startActivity(installIntent)
        }else{
            System.out.println("Instalado TTS")
        }
    }
}
