package model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transacao(
    val tipo: TipoTransacao,
    val valor: Double,
    val dataHora: LocalDateTime = LocalDateTime.now()
) {
    fun formatar(): String {
        val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return "[${dataHora.format(fmt)}] ${tipo.name.padEnd(22)} R$ %,.2f".format(valor)
    }
}