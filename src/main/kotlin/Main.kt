import model.Conta
import service.CaixaEletronico

fun main() {
    val contas = mutableMapOf(
        "1234" to Conta("1234", "Ana Silva",   "1111", 1500.0, 500.0),
        "5678" to Conta("5678", "Bruno Costa", "2222",  800.0, 300.0),
        "9999" to Conta("9999", "Carla Souza", "3333", 2500.0, 1000.0)
    )

    CaixaEletronico(contas).iniciar()
}