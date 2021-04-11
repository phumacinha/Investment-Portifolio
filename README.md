# Desenvolvimento de testes unitários para validar uma API REST de gestão de investimentos financeiros
Esse projeto foi desenvolvido para estudo de desenvolvimento de testes unitários utilizando JUnit, Mockito e Hamcrest. A proposta do projeto foi elaborada no módulo "Desenvolvimento de testes unitários para validar uma API REST de gerenciamento estoques de cerveja" do bootcamp "Inter Java Developer" oferecido pela Digital Innovation One.

Diferentemente do que foi desenvolvido no curso, o projeto **Investment Portifolio**, como o próprio nome sugere, auxilia na gestão de uma carteira de investimentos.

Nesse projeto, um usuário pode cadastrar seus investimentos e gerenciá-los: é possível excluir o investimento, fazer retiradas parciais ou reaplicações no investimento.

## Execução do projeto e da suíte de testes
Para executar o projeto no terminal, basta executar o seguinte comando:
```shell
$ mvn spring-boot:run
```
Para executar a suíte de testes desenvolvida para o service e controller da aplicação, execute o comando:
```shell
$ mvn clean test
```
Após rodar a aplicação, ela poderá ser acessada através do endereço
```
http://localhost:8080/api/v1/investments
```

## Ambiente de desenvolvimento
O projeto foi desenvolvido num ambiente configurado da seguinte forma:
- Java 11
- Apache Maven 3.6.3
- Git 2.25.1
- IntelliJ IDEA 2020.3.4