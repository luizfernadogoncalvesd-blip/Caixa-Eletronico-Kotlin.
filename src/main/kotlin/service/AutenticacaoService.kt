package service

import model.Conta

class AutenticacaoService(private val MAX_TENTATIVAS: Int = 3) {

    fun autenticar(conta: Conta, leitorSenha: () -> String): Boolean {
        var tentativas = 0
        while (tentativas < MAX_TENTATIVAS) {
            val senha = leitorSenha()
            if (conta.validarSenha(senha)) return true
            tentativas++
            println("❌ Senha incorreta. Tentativa $tentativas/$MAX_TENTATIVAS")
        }
        println("🔒 Conta bloqueada. Procure sua agência.")
        return false
    }
}