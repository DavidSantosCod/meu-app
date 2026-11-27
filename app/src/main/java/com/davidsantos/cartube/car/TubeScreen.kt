package com.davidsantos.cartube.car

import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.OnClickListener
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.NavigationTemplate
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.davidsantos.cartube.R

/**
 * Tela principal no carro. Usa o NavigationTemplate porque é o único modelo
 * que dá acesso à superfície inteira da tela, onde desenhamos o YouTube.
 * Os botões de controle ficam na faixa de ações (lado direito).
 */
class TubeScreen(carContext: CarContext) : Screen(carContext), DefaultLifecycleObserver {

    private val renderer = SurfaceRenderer(carContext)
    private val speedGuard = SpeedGuard(carContext) { moving -> renderer.setBlocked(moving) }

    init {
        lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        carContext.getCarService(AppManager::class.java).setSurfaceCallback(renderer)
        speedGuard.start()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        speedGuard.stop()
        renderer.release()
    }

    override fun onGetTemplate(): Template {
        val controls = ActionStrip.Builder()
            .addAction(action(R.drawable.ic_replay) { renderer.seekBy(-10) })
            .addAction(action(R.drawable.ic_play_pause) { renderer.togglePlay() })
            .addAction(action(R.drawable.ic_forward) { renderer.seekBy(10) })
            .addAction(action(R.drawable.ic_home) { renderer.goHome() })
            .build()

        // Botão "mover" (PAN): ao ativar, arrastar o dedo rola a página.
        val panStrip = ActionStrip.Builder().addAction(Action.PAN).build()

        return NavigationTemplate.Builder()
            .setActionStrip(controls)
            .setMapActionStrip(panStrip)
            .setPanModeListener { }
            .build()
    }

    private fun action(icon: Int, onClick: () -> Unit): Action =
        Action.Builder()
            .setIcon(CarIcon.Builder(IconCompat.createWithResource(carContext, icon)).build())
            .setOnClickListener(OnClickListener { onClick() })
            .build()
}
