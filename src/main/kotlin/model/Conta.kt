package model

class Conta(
    val numero: String,
    val titular: String,
    private var senha: String,
    saldoInicial: Double = 0.0,
    val limite: Double = 500.0
) {
    private var _saldo: Double = saldoInicial
    val saldo: Double get() = _saldo

    private var saquesHoje: Int = 0
    private val MAX_SAQUES_DIA = 3
    private val historico = mutableListOf<Transacao>()

    fun validarSenha(tentativa: String): Boolean = tentativa == senha

    fun podeSacar(valor: Double): Pair<Boolean, String> {
        return when {
            valor <= 0                   -> false to "Valor inválido."
            valor % 10 != 0.0            -> false to "Saque deve ser múltiplo de R$ 10."
            saquesHoje >= MAX_SAQUES_DIA -> false to "Limite de $MAX_SAQUES_DIA saques diários atingido."
            valor > (_saldo + limite)    -> false to "Saldo + limite insuficiente."
            else                         -> true to "OK"
        }
    }

    fun sacar(valor: Double) {
        _saldo -= valor
        saquesHoje++
        historico.add(Transacao(TipoTransacao.SAQUE, valor))
    }

    fun depositar(valor: Double) {
        _saldo += valor
        historico.add(Transacao(TipoTransacao.DEPOSITO, valor))
    }

    fun transferir(valor: Double, destino: Conta) {
        _saldo -= valor
        destino._saldo += valor
        historico.add(Transacao(TipoTransacao.TRANSFERENCIA_ENVIADA, valor))
        destino.historico.add(Transacao(TipoTransacao.TRANSFERENCIA_RECEBIDA, valor))
    }

    fun extrato(): List<Transacao> = historico.toList()

    fun resetarSaquesDiarios() { saquesHoje = 0 }
}