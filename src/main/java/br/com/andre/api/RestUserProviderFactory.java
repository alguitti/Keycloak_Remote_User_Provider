package br.com.andre.api;

import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.keycloak.utils.StringUtil;

import br.com.andre.api.external.LegacyUserSimpleHttp;

public class RestUserProviderFactory implements UserStorageProviderFactory<RestUserProvider>{

	private static final String ID = "rest-user-provider";
	private static final String HELP = "The Rest User Provider";
	private static final String BASE_URL = "Base URL";
	private static final String ERROR = "Precisa da URL";
	
	@Override
	public RestUserProvider create(KeycloakSession session, ComponentModel model) {
		return new RestUserProvider(session, model, 
				new LegacyUserSimpleHttp(session, model));
	}

	@Override
	public String getId() {
		return ID;
	}
	
	@Override
	public String getHelpText() {
		return HELP;
	}
	
	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return ProviderConfigurationBuilder.create()
				.property(BASE_URL, "Base URL", "Base URL of the API", ProviderConfigProperty.STRING_TYPE, "", null)
				.build();
	}
	
	@Override
	public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
		if (StringUtil.isBlank(config.get(BASE_URL))) {
			throw new ComponentValidationException(ERROR);
		}
		
		
	}

}
