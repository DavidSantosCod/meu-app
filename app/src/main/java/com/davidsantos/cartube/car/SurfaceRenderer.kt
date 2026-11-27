package com.davidsantos.cartube.car

import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.os.Handler
import android.os.Looper
import androidx.car.app.CarContext
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import com.davidsantos.cartube.PendingLink

/**
 * Recebe a superfície da tela do carro e cria uma "tela virtual" sobre ela.
 * Nessa tela virtual mostramos uma Presentation com o WebView do YouTube.
 * Toques e rolagens do carro são repassados para o WebView.
 */
class SurfaceRenderer(private val carContext: CarContext) : SurfaceCallback {

    private val main = Handler(Looper.getMainLooper())
    private var virtualDisplay: VirtualDisplay? = null
    private var presentation: TubePresentation? = null
    private var blocked = true

    init {
        PendingLink.listener = { url ->
            val p = presentation
            if (p != null) {
                main.post { p.load(url) }
                true
            } else false
        }
    }

    override fun onSurfaceAvailable(container: SurfaceContainer) {
        val surface = container.surface ?: return
        val existing = virtualDisplay
        if (existing != null) {
            // Mudou o tamanho/superfície: reaproveita para não perder o vídeo.
            existing.resize(container.width, container.height, container.dpi)
            existing.surface = surface
            return
        }
        val dm = carContext.getSystemService(DisplayManager::class.java)
        val vd = dm.createVirtualDisplay(
            "CarTube",
            container.width,
            container.height,
            container.dpi,
            surface,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY,
        )
        virtualDisplay = vd
        presentation = TubePresentation(carContext, vd.display).also {
            it.show()
            it.setBlocked(blocked)
            PendingLink.consume()?.let(it::load)
        }
    }

    override fun onSurfaceDestroyed(container: SurfaceContainer) {
        // A superfície sumiu (ex.: outro app na frente). Mantém o WebView vivo
        // para o áudio continuar; só desconecta a superfície.
        virtualDisplay?.surface = null
    }

    override fun onClick(x: Float, y: Float) {
        main.post { presentation?.tap(x, y) }
    }

    override fun onScroll(distanceX: Float, distanceY: Float) {
        main.post { presentation?.scroll(distanceX, distanceY) }
    }

    fun setBlocked(value: Boolean) {
        blocked = value
        main.post { presentation?.setBlocked(value) }
    }

    fun togglePlay() = main.post { presentation?.togglePlay() }
    fun seekBy(seconds: Int) = main.post { presentation?.seekBy(seconds) }
    fun goHome() = main.post { presentation?.goHome() }

    fun release() {
        PendingLink.listener = null
        presentation?.destroyWeb()
        presentation?.dismiss()
        presentation = null
        virtualDisplay?.release()
        virtualDisplay = null
    }
}
