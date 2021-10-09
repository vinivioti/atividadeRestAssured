# atividadeRestAssured

Segunda atividade do curso automação de testes

Professor: João Victorino

## Alunos:

Leonardo Ananias do Nascimento Azogue 

Vinissius Vioti dos Santos 

## **Descrição Atividade:** 

Escolher uma API pública para consumir e criar testes automatizados com o restAssured.

## URL DA API 
https://gorest.co.in

* OBS.: Para consumir a API do gorest é necessário ter um token authorization * 

* OBS2.: O token authorization se encontra como uma constante no projeto *

## Cenários de Testes

** Pré-condição

Realiza uma requisição do tipo POST na URL: https://gorest.co.in/public/v1/users criando um novo usuário no sistema.

### Cenário de Sucesso

Realiza uma requisição do tipo GET na URL https://gorest.co.in/public/v1/users/{{idCriado}} passando o id criado na pré-condição.

### Cenário de Falha 

Realiza uma requisição do tipo GET na URL https://gorest.co.in/public/v1/users/{{idInvalido}} passando o id invalido.
