package rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import io.restassured.RestAssured;
import rest.core.BaseTest;
import utils.DataUtils;

public class MovimentacaoTest extends BaseTest {
	
	@Test
	public void mov01_deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		
			given()
				.body(mov)
			.when()
				.post("/transacoes")
		.then()
				.statusCode(201)

			;
	}
	
	@Test
	public void mov02_deveValidarDadosMovimentacaoComSucesso() {
		
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
	public void mov03_naoDeveInserirMovimentacaoComDataFutura() {
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
	public void mov04_naoDeveExcluirContaComMovimentacao() {
		Integer CONTA_ID = getIdContaPeloNome("Conta com movimentacao");

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
	public void mov05_deveExcluirMovimentacao() {
		Integer MOV_ID = getIdMovimentacaoPelaDesc("Movimentacao para exclusao");
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
	
	
	public Integer getIdMovimentacaoPelaDesc(String desc) {
		return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");
	}
	
	
	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
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
