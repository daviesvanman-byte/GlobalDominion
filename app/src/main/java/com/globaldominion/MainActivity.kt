package com.globaldominion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.globaldominion.ui.CountryControlPanel
import com.globaldominion.ui.ConversationPanel
import com.globaldominion.ui.DecisionPanel
import com.globaldominion.ui.SimpleNewsTicker
import com.globaldominion.ui.theme.GlobalDominionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlobalDominionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GlobalDominionApp()
                }
            }
        }
    }
}

@Composable
fun GlobalDominionApp() {
    // Sample world state
    var worldState by remember { mutableStateOf("Welcome to GlobalDominion Simulation") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(worldState, style = MaterialTheme.typography.titleMedium)

        // Decision panel
        DecisionPanel(
            onDecisionMade = { decision ->
                worldState = "Decision Made: $decision"
            }
        )

        // Country control panel
        CountryControlPanel()

        // Conversation panel
        ConversationPanel(
            onMessageSent = { msg ->
                worldState = "Conversation: $msg"
            }
        )

        // News ticker
        SimpleNewsTicker(
            newsList = listOf(
                "Breaking: AI Country A increases military spending!",
                "Public unrest rises in Country B due to resource shortage."
            )
        )
    }
}
