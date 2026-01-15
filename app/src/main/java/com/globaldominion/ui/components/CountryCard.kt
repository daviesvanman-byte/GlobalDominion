package com.globaldominion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globaldominion.data.Country
import com.globaldominion.data.RelationType
import com.globaldominion.data.toRelationType

@Composable
fun CountryCard(
    country: Country,
    isSelected: Boolean,
    isPlayer: Boolean,
    relationToPlayer: Float?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayer) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Flag and name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(country.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = country.flag,
                        fontSize = 20.sp
                    )
                }
                Column {
                    Text(
                        text = country.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (isPlayer) {
                        Text(
                            text = "YOUR NATION",
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Quick stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStat("💰", country.gdp)
                QuickStat("⚔️", country.militaryPower)
                QuickStat("👥", country.publicOpinion.governmentSupport)
            }
            
            // Relation indicator (if not player)
            if (!isPlayer && relationToPlayer != null) {
                Spacer(modifier = Modifier.height(8.dp))
                RelationIndicator(relationToPlayer)
            }
        }
    }
}

@Composable
private fun QuickStat(icon: String, value: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 14.sp)
        Text(
            text = "${value.toInt()}",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RelationIndicator(relation: Float) {
    val relationType = relation.toRelationType()
    val (color, label) = when (relationType) {
        RelationType.ALLIED -> Color(0xFF4CAF50) to "Allied"
        RelationType.FRIENDLY -> Color(0xFF8BC34A) to "Friendly"
        RelationType.NEUTRAL -> Color(0xFFFFEB3B) to "Neutral"
        RelationType.TENSE -> Color(0xFFFF9800) to "Tense"
        RelationType.HOSTILE -> Color(0xFFF44336) to "Hostile"
        RelationType.AT_WAR -> Color(0xFF9C27B0) to "At War"
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
