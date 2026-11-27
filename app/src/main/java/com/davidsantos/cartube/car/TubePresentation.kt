package com.davidsantos.cartube.car

import android.annotation.SuppressLint
import android.app.Presentation
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.Display
import android.view.Gravity
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView

/** O conteúdo desenhado na tela do carro: o site do YouTube + aviso de movimento. */
class TubePresentation(context: Context, display: Display) : Presentation(context, display) {

    private lateinit var root: FrameLayout
    private lateinit var web: WebView
    private lateinit var cover: TextView
    private var fullscreenView: View? = null
    private var fullscreenCallback: WebChromeClient.CustomViewCallback? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = FrameLayout(context).apply { setBackgroundColor(Color.BLACK) }

        web = WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            webViewClient = WebViewClient() // mantém a navegação dentro do app
            webChromeClient = object : WebChromeClient() {
                // Botão de tela cheia do player do YouTube
                override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                    exitFullscreen()
                    fullscreenView = view
                    fullscreenCallback = callback
                    root.addView(view, 1, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
                }

                override fun onHideCustomView() {
                    fullscreenView?.let { root.removeView(it) }
                    fullscreenView = null
                    fullscreenCallback = null
                }
            }
            loadUrl(HOME)
        }
        root.addView(web, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        cover = TextView(context).apply {
            text = "🚗 Vídeo pausado em movimento\nO áudio continua"
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.BLACK)
            isClickable = true // bloqueia toques enquanto o carro anda
            visibility = View.GONE
        }
        root.addView(cover, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        setContentView(root)
    }

    fun setBlocked(blocked: Boolean) {
        cover.visibility = if (blocked) View.VISIBLE else View.GONE
        cover.bringToFront()
    }

    fun tap(x: Float, y: Float) {
        val t = SystemClock.uptimeMillis()
        val down = MotionEvent.obtain(t, t, MotionEvent.ACTION_DOWN, x, y, 0)
        val up = MotionEvent.obtain(t, t + 60, MotionEvent.ACTION_UP, x, y, 0)
        down.source = InputDevice.SOURCE_TOUCHSCREEN
        up.source = InputDevice.SOURCE_TOUCHSCREEN
        root.dispatchTouchEvent(down)
        root.dispatchTouchEvent(up)
        down.recycle()
        up.recycle()
    }

    fun scroll(dx: Float, dy: Float) {
        if (cover.visibility == View.VISIBLE || fullscreenView != null) return
        web.evaluateJavascript("window.scrollBy(${dx.toInt()},${dy.toInt()})", null)
    }

    fun togglePlay() = video("if(v.paused){v.play()}else{v.pause()}")

    fun seekBy(seconds: Int) = video("v.currentTime=Math.max(0,v.currentTime+($seconds))")

    fun goHome() {
        exitFullscreen()
        web.loadUrl(HOME)
    }

    fun load(url: String) {
        exitFullscreen()
        web.loadUrl(url)
    }

    fun destroyWeb() {
        exitFullscreen()
        web.stopLoading()
        web.destroy()
    }

    private fun exitFullscreen() {
        fullscreenCallback?.onCustomViewHidden()
        fullscreenView?.let { root.removeView(it) }
        fullscreenView = null
        fullscreenCallback = null
    }

    /** Executa um comando no elemento <video> da página do YouTube. */
    private fun video(code: String) {
        web.evaluateJavascript(
            "(function(){var v=document.querySelector('video');if(!v)return;$code})()",
            null,
        )
    }

    companion object {
        const val HOME = "https://m.youtube.com"
    }
}
