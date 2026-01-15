package com.globaldominion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.globaldominion.ui.NewsTicker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlobalDominionApp()
        }
    }
}

@Composable
fun GlobalDominionApp() {
    Surface {
        Text("Welcome to GlobalDominion Simulation")
        NewsTicker() // Placeholder for news ticker UI
    }
}
