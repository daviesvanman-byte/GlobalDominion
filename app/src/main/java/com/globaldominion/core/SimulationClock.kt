package com.globaldominion.core

import kotlinx.coroutines.*

object SimulationClock {
    private var job: Job? = null
    private var isRunning = false
    var tickInterval: Long = 5000L // 5 seconds per tick
    
    var onTick: (() -> Unit)? = null
    
    fun start(scope: CoroutineScope) {
        if (isRunning) return
        isRunning = true
        
        job = scope.launch {
            while (isActive && isRunning) {
                delay(tickInterval)
                SimulationEngine.tick()
                onTick?.invoke()
            }
        }
    }
    
    fun stop() {
        isRunning = false
        job?.cancel()
        job = null
    }
    
    fun pause() {
        isRunning = false
    }
    
    fun resume(scope: CoroutineScope) {
        if (!isRunning) {
            start(scope)
        }
    }
    
    fun setSpeed(speed: SimulationSpeed) {
        tickInterval = when (speed) {
            SimulationSpeed.SLOW -> 10000L
            SimulationSpeed.NORMAL -> 5000L
            SimulationSpeed.FAST -> 2000L
            SimulationSpeed.VERY_FAST -> 1000L
        }
    }
}

enum class SimulationSpeed {
    SLOW, NORMAL, FAST, VERY_FAST
}
