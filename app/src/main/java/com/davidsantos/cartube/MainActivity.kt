package com.davidsantos.cartube

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

/** Tela do celular: instruções, permissão de velocidade e envio de links ao carro. */
class MainActivity : Activity() {

    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (20 * resources.displayMetrics.density).toInt()
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad * 2, pad, pad)
        }

        column.addView(TextView(this).apply {
            text = "CarTube"
            textSize = 30f
            gravity = Gravity.CENTER_HORIZONTAL
        })

        column.addView(TextView(this).apply {
            textSize = 16f
            setPadding(0, pad, 0, pad)
            text = """
                Como usar:
                1. Conecte o celular ao carro e abra "CarTube" no Android Auto.
                2. Toque na tela do carro para navegar no YouTube.
                3. Use os botões da direita: voltar 10s, play/pause, avançar 10s e início.
                4. Para rolar a página, toque no botão de mover (cruz) e arraste.
                5. Para mandar um vídeo do celular: no app do YouTube, toque em
                   Compartilhar → CarTube. Ou cole o link abaixo.

                Por segurança, o vídeo só aparece com o carro parado.
                Em movimento, continua só o áudio.
            """.trimIndent()
        })

        status = TextView(this).apply { textSize = 14f; setPadding(0, 0, 0, pad) }
        column.addView(status)

        val linkField = EditText(this).apply {
            hint = "Cole um link do YouTube"
            inputType = InputType.TYPE_TEXT_VARIATION_URI
        }
        column.addView(linkField)

        column.addView(Button(this).apply {
            text = "Enviar para o carro"
            setOnClickListener { sendLink(linkField.text.toString()) }
        })

        setContentView(ScrollView(this).apply { addView(column) })

        askSpeedPermission()
        handleShare(intent)
    }

    override fun onResume() {
        super.onResume()
        status.text = if (hasSpeedPermission())
            "✅ Permissão de velocidade concedida"
        else
            "⚠️ Sem permissão de velocidade: o vídeo ficará bloqueado no carro."
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShare(intent)
    }

    private fun handleShare(intent: Intent?) {
        if (intent?.action != Intent.ACTION_SEND) return
        val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
        sendLink(text)
    }

    private fun sendLink(raw: String) {
        val url = Regex("https?://\\S+").find(raw)?.value
        if (url == null || !(url.contains("youtube.com") || url.contains("youtu.be"))) {
            Toast.makeText(this, "Link do YouTube inválido", Toast.LENGTH_SHORT).show()
            return
        }
        val opened = PendingLink.send(url)
        val msg = if (opened) "Abrindo no carro…" else "Guardado: abre quando o CarTube abrir no carro"
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun hasSpeedPermission() =
        checkSelfPermission(SPEED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private fun askSpeedPermission() {
        if (!hasSpeedPermission()) requestPermissions(arrayOf(SPEED_PERMISSION), 1)
    }

    companion object {
        const val SPEED_PERMISSION = "com.google.android.gms.permission.CAR_SPEED"
    }
}
