package com.globaldominion.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globaldominion.data.EventType
import com.globaldominion.data.NewsEvent

@Composable
fun NewsTicker(
    events: List<NewsEvent>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "📰",
                    fontSize = 18.sp
                )
                Text(
                    text = "WORLD NEWS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (events.isEmpty()) {
                Text(
                    text = "No news yet...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(events.take(10)) { event ->
                        NewsEventItem(event)
                    }
                }
            }
        }
    }
}

// Simple string list version
@Composable
fun SimpleNewsTicker(
    newsList: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "📰",
                    fontSize = 18.sp
                )
                Text(
                    text = "WORLD NEWS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (newsList.isEmpty()) {
                Text(
                    text = "No news yet...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 150.dp)
                ) {
                    items(newsList) { news ->
                        SimpleNewsItem(news)
                    }
                }
            }
        }
    }
}

@Composable
private fun SimpleNewsItem(news: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "📢", fontSize = 14.sp)
        Text(
            text = news,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun NewsEventItem(event: NewsEvent) {
    val (icon, color) = when (event.type) {
        EventType.LEADER_ANNOUNCEMENT -> "📢" to Color(0xFF2196F3)
        EventType.MILITARY_ACTION -> "⚔️" to Color(0xFFF44336)
        EventType.DISASTER -> "🌋" to Color(0xFFFF5722)
        EventType.ECONOMIC_CHANGE -> "📈" to Color(0xFF4CAF50)
        EventType.PUBLIC_PROTEST -> "✊" to Color(0xFFFF9800)
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = icon, fontSize = 16.sp)
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.country,
                    fontSize = 10.sp,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                )
                SeverityIndicator(event.severity)
            }
            Text(
                text = event.headline,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = event.description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun SeverityIndicator(severity: Float) {
    val color = when {
        severity >= 0.7f -> Color(0xFFF44336)
        severity >= 0.4f -> Color(0xFFFF9800)
        else -> Color(0xFF4CAF50)
    }
    val label = when {
        severity >= 0.7f -> "HIGH"
        severity >= 0.4f -> "MED"
        else -> "LOW"
    }
    
    Text(
        text = label,
        fontSize = 8.sp,
        color = color,
        fontWeight = FontWeight.Bold
    )
}
