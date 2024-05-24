package br.com.andre.api.external;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.component.ComponentModel;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.KeycloakSession;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.andre.api.LegacyUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LegacyUserSimpleHttp implements LegacyUserClient{

	private static final String BASE_URL = "Base URL";

	private CloseableHttpClient httpClient;
	private final String baseUrl;
	
	public LegacyUserSimpleHttp(KeycloakSession session, ComponentModel model) {
		this.httpClient = session.getProvider(HttpClientProvider.class).getHttpClient();
		this.baseUrl = model.get(BASE_URL);
	}
	

	@Override
	@SneakyThrows
	public LegacyUser getUserByUsername(String username) {
		String url = String.format("%s/username/%s", baseUrl, username);
		SimpleHttp.Response response = SimpleHttp.doGet(url, httpClient).asResponse();
		if (response.getStatus() != 200) {
			log.error("[SIMPLE HTTP] - USER NOT FOUND BY USERNAME!");
			return null;
		}
		return response.asJson(LegacyUser.class);
	}

	@Override
	@SneakyThrows
	public String verifyPassword(String username, String password) {
		String url = String.format("%s/verify/%s/%s", baseUrl, username, password);
		SimpleHttp.Response response = SimpleHttp.doGet(url, httpClient).asResponse();
		return response.asString();
	}


	@Override
	@SneakyThrows
	public List<LegacyUser> getAll() {
		SimpleHttp http = SimpleHttp.doGet(baseUrl, httpClient);
		return http.asJson(new TypeReference<List<LegacyUser>>() {});
	}


	@Override
	@SneakyThrows
	public LegacyUser getUserById(String id) {
		String url = String.format("%s/%s", baseUrl, id);
		SimpleHttp.Response response = SimpleHttp.doGet(url, httpClient).asResponse();
		if (response.getStatus() != 200) {
			log.error("[SIMPLE HTTP] - USER NOT FOUND BY ID!");
			return null;
		}
		return response.asJson(LegacyUser.class);
	}

}
