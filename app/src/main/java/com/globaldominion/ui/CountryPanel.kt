package com.globaldominion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.globaldominion.core.WorldStateManager
import com.globaldominion.data.Country

@Composable
fun CountryPanel(
    modifier: Modifier = Modifier,
    onCountrySelected: (Country) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "🌐 World Powers",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(WorldStateManager.countries) { country ->
                    CountryListItem(
                        country = country,
                        isSelected = country.id == WorldStateManager.selectedCountry.value?.id,
                        onClick = { 
                            WorldStateManager.selectedCountry.value = country
                            onCountrySelected(country)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryListItem(
    country: Country,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) 
            MaterialTheme.colorScheme.primaryContainer 
        else 
            MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = country.flag, fontSize = 24.sp)
                Column {
                    Text(
                        text = country.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = country.alliance ?: "Independent",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MiniStat("💰", country.economy.gdp.toInt().toString())
                MiniStat("⚔️", country.military.strength.toString())
            }
        }
    }
}

@Composable
private fun MiniStat(icon: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 12.sp)
        Text(
            text = value,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CountryDetailPanel(
    country: Country,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
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
                        text = country.alliance ?: "Independent Nation",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Economy Section
            SectionHeader("💰 Economy")
            StatRow("GDP", "$${country.economy.gdp.toInt()}B")
            StatRow("Debt", "$${country.economy.debt.toInt()}B")
            StatRow("Stability", "${country.economy.stability}%")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Military Section
            SectionHeader("⚔️ Military")
            StatRow("Strength", "${country.military.strength}")
            StatRow("Readiness", "${country.military.readiness}%")
            StatRow("Nuclear", if (country.military.nuclearCapable) "Yes ☢️" else "No")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Public Opinion Section
            SectionHeader("👥 Public Opinion")
            StatRow("Approval", "${country.publicOpinionData.approval}%")
            StatRow("Unrest", "${country.publicOpinionData.unrest}%")
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
