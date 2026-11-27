package com.davidsantos.cartube.car

import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

/** Ponto de entrada que o Android Auto chama ao abrir o app no carro. */
class TubeCarAppService : CarAppService() {

    // Uso pessoal (instalado fora da Play Store): aceita qualquer host do Android Auto.
    override fun createHostValidator(): HostValidator = HostValidator.ALLOW_ALL_HOSTS_VALIDATOR

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onCreateSession(): Session = TubeSession()
}

class TubeSession : Session() {
    override fun onCreateScreen(intent: Intent): Screen = TubeScreen(carContext)
}
