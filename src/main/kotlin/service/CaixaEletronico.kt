package service

import model.Conta

class CaixaEletronico(
    private val contas: MutableMap<String, Conta>
) {
    private val auth = AutenticacaoService()

    fun iniciar() {
        println("═══════════════════════════════")
        println("     🏧 CAIXA ELETRÔNICO        ")
        println("═══════════════════════════════")

        val conta = identificarConta() ?: return

        if (!auth.autenticar(conta) { lerSenha() }) return

        menuPrincipal(conta)
    }

    private fun identificarConta(): Conta? {
        print("Número da conta: ")
        val numero = readlnOrNull()?.trim() ?: return null
        val conta = contas[numero]
        if (conta == null) println("⚠️ Conta não encontrada.")
        return conta
    }

    private fun lerSenha(): String {
        print("Senha: ")
        return readlnOrNull()?.trim() ?: ""
    }

    private fun menuPrincipal(conta: Conta) {
        var continuar = true
        while (continuar) {
            println("\n──── MENU ────")
            println("1 - Consultar saldo")
            println("2 - Sacar")
            println("3 - Depositar")
            println("4 - Extrato")
            println("5 - Transferir")
            println("0 - Sair")
            print("Opção: ")

            when (readlnOrNull()?.trim()) {
                "1" -> consultarSaldo(conta)
                "2" -> realizarSaque(conta)
                "3" -> realizarDeposito(conta)
                "4" -> mostrarExtrato(conta)
                "5" -> realizarTransferencia(conta)
                "0" -> {
                    println("👋 Obrigado por usar nosso caixa!")
                    continuar = false
                }
                else -> println("⚠️ Opção inválida.")
            }
        }
    }

    private fun consultarSaldo(conta: Conta) {
        println("💰 Saldo atual: R$ %,.2f".format(conta.saldo))
        println("📊 Limite disponível: R$ %,.2f".format(conta.limite))
    }

    private fun realizarSaque(conta: Conta) {
        print("Valor do saque: R$ ")
        val valor = readlnOrNull()?.toDoubleOrNull() ?: run {
            println("⚠️ Valor inválido."); return
        }

        val (pode, msg) = conta.podeSacar(valor)
        if (!pode) { println("❌ $msg"); return }

        conta.sacar(valor)
        println("✅ Saque realizado. Novo saldo: R$ %,.2f".format(conta.saldo))
        entregarCedulas(valor)
    }

    private fun entregarCedulas(valor: Double) {
        var restante = valor.toInt()
        val cedulas = listOf(100, 50, 20, 10)
        val entrega = mutableMapOf<Int, Int>()
        for (c in cedulas) {
            val qtd = restante / c
            if (qtd > 0) { entrega[c] = qtd; restante %= c }
        }
        println("💵 Cédulas: " + entrega.entries.joinToString(", ") { "${it.value}x R$${it.key}" })
    }

    private fun realizarDeposito(conta: Conta) {
        print("Valor do depósito: R$ ")
        val valor = readlnOrNull()?.toDoubleOrNull() ?: run {
            println("⚠️ Valor inválido."); return
        }
        if (valor <= 0) { println("❌ Valor deve ser positivo."); return }

        conta.depositar(valor)
        println("✅ Depósito realizado. Novo saldo: R$ %,.2f".format(conta.saldo))
    }

    private fun mostrarExtrato(conta: Conta) {
        val extrato = conta.extrato()
        if (extrato.isEmpty()) {
            println("📄 Nenhuma transação registrada."); return
        }
        println("\n──── EXTRATO ────")
        extrato.forEach { println(it.formatar()) }
        println("─────────────────")
        println("Saldo: R$ %,.2f".format(conta.saldo))
    }

    private fun realizarTransferencia(conta: Conta) {
        print("Conta destino: ")
        val destinoNum = readlnOrNull()?.trim() ?: return
        val destino = contas[destinoNum]
        if (destino == null) { println("⚠️ Conta destino não encontrada."); return }
        if (destino.numero == conta.numero) { println("⚠️ Não pode transferir para a mesma conta."); return }

        print("Valor: R$ ")
        val valor = readlnOrNull()?.toDoubleOrNull() ?: run {
            println("⚠️ Valor inválido."); return
        }
        if (valor <= 0 || valor > conta.saldo) {
            println("❌ Saldo insuficiente ou valor inválido."); return
        }

        conta.transferir(valor, destino)
        println("✅ Transferência de R$ %,.2f para ${destino.titular} concluída.".format(valor))
    }
}