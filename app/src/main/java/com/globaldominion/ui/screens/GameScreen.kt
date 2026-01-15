package com.globaldominion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globaldominion.core.WorldState
import com.globaldominion.data.*
import com.globaldominion.ui.NewsTicker
import com.globaldominion.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(worldState: WorldState) {
    var showDecisionDialog by remember { mutableStateOf(false) }
    var selectedDecisionCategory by remember { mutableStateOf<DecisionCategory?>(null) }
    
    val playerCountry = worldState.playerCountry.value
    val selectedCountry = worldState.selectedCountry.value
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🌍",
                            fontSize = 24.sp
                        )
                        Column {
                            Text(
                                text = "GLOBAL DOMINION",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Year ${worldState.year.value} • Turn ${worldState.turn.value}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { worldState.advanceTurn() }) {
                        Icon(Icons.Default.PlayArrow, "Next Turn")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            if (playerCountry != null) {
                BottomActionBar(
                    onDecisionClick = { category ->
                        selectedDecisionCategory = category
                        showDecisionDialog = true
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Player country summary
            playerCountry?.let { player ->
                PlayerSummaryCard(
                    country = player,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            
            // World countries grid
            Text(
                text = "WORLD POWERS",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(worldState.countries) { country ->
                    CountryCard(
                        country = country,
                        isSelected = country.id == selectedCountry?.id,
                        isPlayer = country.isPlayerControlled,
                        relationToPlayer = playerCountry?.relations?.get(country.id),
                        onClick = { worldState.selectedCountry.value = country },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
            
            // Selected country details
            selectedCountry?.let { country ->
                CountryDetailsCard(
                    country = country,
                    isPlayer = country.isPlayerControlled,
                    playerCountry = playerCountry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            
            // News ticker
            NewsTicker(
                events = worldState.newsEvents,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    
    // Decision dialog
    if (showDecisionDialog && playerCountry != null) {
        DecisionDialog(
            category = selectedDecisionCategory,
            decisions = worldState.getAvailableDecisions(playerCountry)
                .filter { selectedDecisionCategory == null || it.category == selectedDecisionCategory },
            onDecisionSelected = { decision ->
                worldState.executeDecision(decision, playerCountry)
                showDecisionDialog = false
            },
            onDismiss = { showDecisionDialog = false }
        )
    }
}

@Composable
fun PlayerSummaryCard(
    country: Country,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = country.flag, fontSize = 40.sp)
                Column {
                    Text(
                        text = country.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = country.government?.type?.name?.replace("_", " ") ?: "Unknown",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn("💰 GDP", country.gdp, Color(0xFF4CAF50))
                StatColumn("⚔️ Military", country.militaryPower, Color(0xFFF44336))
                StatColumn("🔬 Tech", country.technology, Color(0xFF2196F3))
                StatColumn("📦 Resources", country.resources, Color(0xFFFF9800))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn("👍 Support", country.publicOpinion.governmentSupport, Color(0xFF9C27B0))
                StatColumn("✊ Unrest", country.publicOpinion.protestIntensity, Color(0xFFE91E63))
                StatColumn("⚠️ Escalation", country.escalationRisk, Color(0xFFFF5722))
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = "${value.toInt()}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun CountryDetailsCard(
    country: Country,
    isPlayer: Boolean,
    playerCountry: Country?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = country.flag, fontSize = 32.sp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = country.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = country.government?.type?.name?.replace("_", " ") ?: "Unknown",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                if (!isPlayer && playerCountry != null) {
                    val relation = playerCountry.relations[country.id] ?: 50f
                    RelationIndicator(relation)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats
            CompactStatBar("Economy", country.gdp, Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(4.dp))
            CompactStatBar("Military", country.militaryPower, Color(0xFFF44336))
            Spacer(modifier = Modifier.height(4.dp))
            CompactStatBar("Technology", country.technology, Color(0xFF2196F3))
            Spacer(modifier = Modifier.height(4.dp))
            CompactStatBar("Resources", country.resources, Color(0xFFFF9800))
            Spacer(modifier = Modifier.height(4.dp))
            CompactStatBar("Support", country.publicOpinion.governmentSupport, Color(0xFF9C27B0))
            Spacer(modifier = Modifier.height(4.dp))
            CompactStatBar("Unrest", country.publicOpinion.protestIntensity, Color(0xFFE91E63))
            
            // Relations with player
            if (!isPlayer && playerCountry != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
                
                val relation = playerCountry.relations[country.id] ?: 50f
                Text(
                    text = "Relations with ${playerCountry.name}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatBar(
                    label = "",
                    value = relation,
                    color = when {
                        relation >= 60f -> Color(0xFF4CAF50)
                        relation >= 40f -> Color(0xFFFFEB3B)
                        else -> Color(0xFFF44336)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomActionBar(
    onDecisionClick: (DecisionCategory?) -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("💰", "Economy", DecisionCategory.ECONOMY, onDecisionClick)
            ActionButton("⚔️", "Military", DecisionCategory.MILITARY, onDecisionClick)
            ActionButton("🔬", "Tech", DecisionCategory.TECHNOLOGY, onDecisionClick)
            ActionButton("🏠", "Domestic", DecisionCategory.DOMESTIC, onDecisionClick)
            ActionButton("🌐", "Foreign", DecisionCategory.FOREIGN_POLICY, onDecisionClick)
        }
    }
}

@Composable
private fun ActionButton(
    icon: String,
    label: String,
    category: DecisionCategory,
    onClick: (DecisionCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
    ) {
        FilledTonalButton(
            onClick = { onClick(category) },
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = icon, fontSize = 20.sp)
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun DecisionDialog(
    category: DecisionCategory?,
    decisions: List<Decision>,
    onDecisionSelected: (Decision) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = category?.name?.replace("_", " ") ?: "All Decisions",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(decisions) { decision ->
                    DecisionItem(
                        decision = decision,
                        onClick = { onDecisionSelected(decision) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DecisionItem(
    decision: Decision,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = decision.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = decision.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                decision.effects.forEach { effect ->
                    EffectChip(effect)
                }
            }
        }
    }
}

@Composable
private fun EffectChip(effect: DecisionEffect) {
    val (icon, color) = when (effect.stat) {
        StatType.GDP -> "💰" to Color(0xFF4CAF50)
        StatType.MILITARY -> "⚔️" to Color(0xFFF44336)
        StatType.SUPPORT -> "👍" to Color(0xFF9C27B0)
        StatType.PROTEST -> "✊" to Color(0xFFE91E63)
        StatType.TECHNOLOGY -> "🔬" to Color(0xFF2196F3)
        StatType.RESOURCES -> "📦" to Color(0xFFFF9800)
        StatType.ESCALATION -> "⚠️" to Color(0xFFFF5722)
        StatType.RELATIONS -> "🤝" to Color(0xFF00BCD4)
    }
    
    val sign = if (effect.change >= 0) "+" else ""
    
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = "$icon $sign${effect.change.toInt()}",
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}