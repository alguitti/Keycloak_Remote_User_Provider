package br.com.andre.api;

import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import br.com.andre.api.external.LegacyUserSimpleHttp;

public class RestUserProviderFactory implements UserStorageProviderFactory<RestUserProvider>{

	private static final String ID = "rest-user-provider";
//	private static final String HELP = "The improved user provider!";
	private static final String BASE_URL = "Base URL";
//	private static final String AUTH_USERNAME = "Auth Username";
//	private static final String AUTH_PASSWORD = "Auth Password";
//	private static final String ERROR = "Preenche a porra da config, ANIMAL!!!";
	
	@Override
	public RestUserProvider create(KeycloakSession session, ComponentModel model) {
		return new RestUserProvider(session, model, 
				new LegacyUserSimpleHttp(session, model));
	}

	@Override
	public String getId() {
		return ID;
	}
	
//	@Override
//	public String getHelpText() {
//		return HELP;
//	}
	
	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return ProviderConfigurationBuilder.create()
				.property(BASE_URL, "Base URL", "Base URL of the API", ProviderConfigProperty.STRING_TYPE, "", null)
//				.property(AUTH_USERNAME, "BasicAuth Username", "Username for BasicAuth at the API", ProviderConfigProperty.STRING_TYPE, "", null)
//				.property(AUTH_PASSWORD, "BasicAuth Password", "Password for BasicAuth at the API", ProviderConfigProperty.PASSWORD, "", null)
				.build();
	}
	
//	@Override
//	public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
//		if (StringUtil.isBlank(config.get(BASE_URL))
//				|| StringUtil.isBlank(config.get(AUTH_USERNAME))
//				|| StringUtil.isBlank(config.get(AUTH_PASSWORD))
//			) {
//			throw new ComponentValidationException(ERROR);
//		}
//		
//		
//	}

}
