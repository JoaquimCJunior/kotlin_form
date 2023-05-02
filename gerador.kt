import java.io.File
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

class GerarFormulario(private val input: Any) {

    fun reflectionClass(): String {
        val objClasse = input::class
        val propriedadesClasse = objClasse.declaredMemberProperties

        var formulario = "<h1>${objClasse.simpleName}</h1><br><br>"

        propriedadesClasse.forEach {

            val verbosoProp = it.returnType.jvmErasure.java.getDeclaredField("verboso")
            verbosoProp.isAccessible = true
            val verboso = verbosoProp.get(it.call(input)) as String

            if(it.returnType.jvmErasure.isSubclassOf(Frase::class)) {
                val tipoProp = it.returnType.jvmErasure.java.getDeclaredField("tipo")
                tipoProp.isAccessible = true
                val tipo = tipoProp.get(it.call(input)) as String

                val dadosFrase = """
                <label for="${it.name}">${verboso}:</label>
                <input type="${tipo}" id="${it.name}" name="${it.name}"><br><br>"""
                formulario += dadosFrase
            } else if(it.returnType.jvmErasure.isSubclassOf(Texto::class)) {
                val dadosTexto = """
                <label for="${it.name}">${verboso}:</label><br>
                <textarea id="${it.name}" name="${it.name}" rows="4" cols="50"></textarea><br><br>"""
                formulario += dadosTexto
            } else if(it.returnType.jvmErasure.isSubclassOf(NumeroInteiro::class)) {
                val dadosNumeroInteiro = """
                <label for="${it.name}">${verboso}:</label>
                <input type="number" id="${it.name}" name="${it.name}"><br><br>"""
                formulario += dadosNumeroInteiro
            } else if(it.returnType.jvmErasure.isSubclassOf(Botao::class)){
                val dadosBotao = """
                <button type="submit">${verboso}</button><br><br>"""
                formulario += dadosBotao
            } else {
                val valorPropriedade = it.call(input)
                formulario += valorPropriedade?.let {GerarFormulario(it).reflectionClass()} ?: ""
            }
        }
        return formulario
    }
}