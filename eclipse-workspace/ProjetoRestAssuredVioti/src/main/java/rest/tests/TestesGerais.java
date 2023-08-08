package rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import rest.core.BaseTest;
import utils.DataUtils;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestesGerais extends BaseTest{
	
//	private String TOKEN;
	
	private static String CONTA_NAME	 = "Conta" + System.nanoTime();
	private static Integer CONTA_ID; 
	private static Integer MOV_ID;
	

	@Test
	public void t01_deveIncluirContaComSucesso() {

			CONTA_ID = given()
				.log().all()
				.body("{ \"nome\": \""+CONTA_NAME+"\" }")
			.when()
				.post("/contas")
		.then()
				.log().all()
				.statusCode(201)
				.extract().path("id")

			;
	}
	
	@Test
	public void t02_naoDeveIncluirContaComNomeRepetido() {

			given()
				.log().all()
				.body("{ \"nome\": \""+CONTA_NAME+"\" }")
			.when()
				.post("/contas")
		.then()
				.log().all()
				.statusCode(400)
				.body("error", is("Já existe uma conta com esse nome!"))

			;
	}
		
	@Test
	public void t03_deveConsultarContaComSucesso() {

			given()
				.log().all()
		.when()
				.get("/contas")
		.then()
				.log().all()
				.statusCode(200)
				.body("id", hasItems(CONTA_ID))

			;
	}
	
	@Test
	public void t04_deveAlterarContaComSucesso() {
			//Obter as contas:
			
			given()
				.log().all()
				.body("{ \"nome\": \""+CONTA_NAME+" ALTERADA\" }")
				.pathParam("id", CONTA_ID)
		.when()
				.put("/contas/{id}")
		.then()
				.log().all()
				.statusCode(200)
				.body("nome", is(CONTA_NAME +" ALTERADA"))

			;
	}
	
	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		
		MOV_ID = 	given()
				.body(mov)
			.when()
				.post("/transacoes")
		.then()
				.statusCode(201)
				.extract().path("id")

			;
	}
	
	@Test
	public void t06_deveValidarDadosMovimentacaoComSucesso() {
		
			given()
				.body("{}")
			.when()
				.post("/transacoes")
		.then()
				.statusCode(400)
				.body("$", hasSize(8))
				.body("msg", hasItems("Data da Movimentação é obrigatório",
						"Data do pagamento é obrigatório", 
						"Descrição é obrigatório", 
						"Interessado é obrigatório", 
						"Valor é obrigatório",
						"Valor deve ser um número",
						"Conta é obrigatório",
						"Situação é obrigatório"
						
						))
			;
	}
	
	@Test
	public void t07_nãoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DataUtils.getDataDiferencaDias(2));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(15));
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
	.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItems("Data da Movimentação deve ser menor ou igual à data atual"))

		;
	}
	
	
	@Test
	public void t08_naoDeveExcluirContaComMovimentacao() {

					given()
				.log().all()
				.pathParam("id", CONTA_ID)
			.when()
				.delete("/contas/{id}")
		.then()
				.log().all()
				.statusCode(500)
				.body("constraint", is("transacoes_conta_id_foreign"))

			;
	}
	
	@Test
	public void t09_deveCalcularSaldos() {

					given()
				.log().all()
			.when()
				.get("/saldo")
		.then()
				.log().all()
				.statusCode(200)
				.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("1000.00"))

			;
	}
	
	@Test
	public void t10_deveExcluirMovimentacao() {

					given()
				.log().all()
				.pathParam("id", MOV_ID)
			.when()
				.delete("/transacoes/{id}")
		.then()
				.log().all()
				.statusCode(204)

			;
	}
	
	@Test
	public void t11_deveExcluirContaComSucesso() {
			
					given()
				.log().all()
				.pathParam("id", CONTA_ID)
			.when()
				.delete("/contas/{id}")
		.then()
				.log().all()
				.statusCode(204)


			;
	}
	
	@Test
	public void t12_naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("authorization");
		
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
			
			;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
//		mov.setUsuario_id(usuario_id);
		mov.setDescricao("Descrição do Movimento");
		mov.setEnvolvido("Envolvido na Movimentoção");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-10));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(-5));
		mov.setValor(1000f);
		mov.setStatus(true);
		return mov;
		
	}

}


