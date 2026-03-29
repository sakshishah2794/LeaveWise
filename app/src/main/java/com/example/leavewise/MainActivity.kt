package com.example.leavewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.leavewise.ui.navigation.AppNavigation
import com.example.leavewise.ui.theme.LeaveWiseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LeaveWiseTheme {
                AppNavigation()
            }
        }
    }
}



