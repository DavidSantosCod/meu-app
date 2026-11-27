package com.davidsantos.cartube.car

import android.content.pm.PackageManager
import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.hardware.CarHardwareManager
import androidx.car.app.hardware.common.CarValue
import androidx.car.app.hardware.common.OnCarDataAvailableListener
import androidx.car.app.hardware.info.CarInfo
import androidx.car.app.hardware.info.Speed
import androidx.core.content.ContextCompat

/**
 * Lê a velocidade do carro e avisa se ele está em movimento.
 * Regra de segurança: se a velocidade for desconhecida, considera em movimento.
 */
class SpeedGuard(
    private val carContext: CarContext,
    private val onMovingChanged: (Boolean) -> Unit,
) {
    private var registered = false
    private var lastMoving: Boolean? = null

    private val listener = OnCarDataAvailableListener<Speed> { speed ->
        val value = speed.rawSpeedMetersPerSecond
        val moving = value.status != CarValue.STATUS_SUCCESS ||
            (value.value ?: 0f) > PARKED_LIMIT_MPS
        if (moving != lastMoving) {
            lastMoving = moving
            onMovingChanged(moving)
        }
    }

    fun start() {
        onMovingChanged(true) // começa bloqueado até confirmar que está parado
        if (carContext.checkSelfPermission(PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            register()
        } else {
            carContext.requestPermissions(listOf(PERMISSION)) { granted, _ ->
                if (PERMISSION in granted) register()
            }
        }
    }

    fun stop() {
        if (!registered) return
        runCatching { carInfo().removeSpeedListener(listener) }
        registered = false
    }

    private fun register() {
        runCatching {
            carInfo().addSpeedListener(ContextCompat.getMainExecutor(carContext), listener)
            registered = true
        }.onFailure { Log.w(TAG, "Não foi possível ler a velocidade", it) }
    }

    private fun carInfo(): CarInfo =
        carContext.getCarService(CarHardwareManager::class.java).carInfo

    companion object {
        private const val TAG = "CarTube"
        private const val PERMISSION = "com.google.android.gms.permission.CAR_SPEED"
        /** ~4 km/h: abaixo disso consideramos parado. */
        private const val PARKED_LIMIT_MPS = 1.1f
    }
}
