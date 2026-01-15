package com.globaldominion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globaldominion.data.DecisionCategory

@Composable
fun DecisionPanel(
    onDecisionMade: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "⚡ Quick Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(DecisionCategory.values().toList()) { category ->
                    val (icon, label) = when (category) {
                        DecisionCategory.MILITARY -> "⚔️" to "Military"
                        DecisionCategory.ECONOMY -> "💰" to "Economy"
                        DecisionCategory.TECHNOLOGY -> "🔬" to "Technology"
                        DecisionCategory.FOREIGN_POLICY -> "🌐" to "Diplomacy"
                        DecisionCategory.DOMESTIC -> "🏠" to "Domestic"
                    }
                    
                    ElevatedButton(
                        onClick = { onDecisionMade("$label action selected") },
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text("$icon $label", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
