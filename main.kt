import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.sql.DriverManager
import java.net.URLDecoder

class Entrada() {
    val nome = Frase("text",255, "Nome")
    val idade = NumeroInteiro("Idade")
    val email = Frase("email",255, "E-mail")
    val botao = Botao("Concluir")
}

object FormHandler : HttpHandler {
    override fun invoke(request: Request): Response =
        Response(Status.OK).body("${createHtml()}")
}

fun createTitle(text: String): String {
    return "<h1>${text}</h1>"
}

fun creatForm(action: String, method: String = "post"): String{
    val input = Entrada()
    val form = GerarFormulario(input)
    return """
        <form action="$action" method="$method">
            ${form.reflectionClass()}
        </form>
    """
}

fun createHtml(): String {
    return """
        <!DOCTYPE html>
        <html>
            <meta charset="utf-8">
            <body style="text-align: center">
                ${creatForm("submit")}          
            </body>
        </html>

    """.trimIndent()
}

fun main() {

    val app: HttpHandler = routes(
        "/formulario" bind Method.GET to FormHandler,
        "/submit" bind Method.POST to { request: Request ->
            val formData = request.bodyString() // obter os dados do formulário
            val params = formData.split("&").map { it.split("=") }.associate { it[0] to it[1] } // converter os dados em um mapa de parâmetros

            // salvar os dados no banco de dados
            val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kotlin_form",
                "Junior", "Junior1234")

            val stmt = conn.createStatement()

            // criar a tabela pessoa caso ela não exista
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "nome VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(50) NOT NULL, " +
                    "idade INT NOT NULL, PRIMARY KEY (id))")

            // inserir os dados na tabela pessoa
            stmt.executeUpdate("INSERT INTO usuarios (nome, email, idade) " +
                    "VALUES ('${URLDecoder.decode(params["nome"], "UTF-8")}', '${URLDecoder.decode(params["email"], "UTF-8")}', ${params["idade"]})")

            Response(Status.SEE_OTHER).header("Location", "/formulario").body("")
        }
    )

    app.asServer(SunHttp(8080)).start()
}
