package rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import io.restassured.RestAssured;
import rest.core.BaseTest;

public class SaldoTest extends BaseTest {
	
	@Test
	public void deveCalcularSaldoContas() {
		Integer CONTA_ID = getIdContaPeloNome("Conta para saldo");

					given()
				.log().all()
			.when()
				.get("/saldo")
		.then()
				.log().all()
				.statusCode(200)
				.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))

			;
	}
	
	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
	}

}
