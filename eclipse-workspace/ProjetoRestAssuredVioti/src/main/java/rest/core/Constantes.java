package rest.core;

import io.restassured.http.ContentType;

public interface Constantes {
	
	String APP_URL_BASE = "https://barrigarest.wcaquino.me";
	Integer APP_PORT = 443; // se for http = 80
	String APP_BASE_PATH = "";
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 5000L;
	

}
