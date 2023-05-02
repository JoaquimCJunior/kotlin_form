# Kotlin-form
Um simples framework em Kotlin que gera um formulário HTML e conexão com MySQL


## Como configurar a conexão
- Crie sua conexão no banco de dados 
- No arquivo `main.kt` Altere a linha abaixo com o nome da sua conexão com o banco de dados, a porta , usuário e senha

`val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kotlin_form","root", "root")`

## Como usar
Altere a classe `entrada` no  arquivo `main.kt` com os atributos que deseja gerar o formulário a ser preenchido.
Os tipos de dados suportados e os respectivos campos nos formulário gerados são:
- Frase ( label ) 
- Texto (text area ) 
- NumeroInteiro ( input number )
- Data ( input date )
- Botao ( button )
 
Agora execute o programa e acesse o servidor local em sua máquina *http://localhost:8080/formulario*

