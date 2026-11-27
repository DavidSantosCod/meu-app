package com.davidsantos.cartube

/**
 * Ponte entre o celular e a tela do carro (mesmo processo).
 * Quando você compartilha um vídeo pelo app do YouTube, o link chega aqui
 * e é aberto no carro. Se a tela do carro ainda não estiver pronta, o link
 * fica guardado até ela abrir.
 */
object PendingLink {
    private var pending: String? = null

    /** Retorna true se o carro abriu o link na hora. */
    var listener: ((String) -> Boolean)? = null

    fun send(url: String): Boolean {
        val handled = listener?.invoke(url) ?: false
        if (!handled) pending = url
        return handled
    }

    fun consume(): String? = pending.also { pending = null }
}
