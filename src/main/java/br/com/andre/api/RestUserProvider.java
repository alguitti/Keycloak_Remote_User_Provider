package br.com.andre.api;

import java.util.Map;
import java.util.stream.Stream;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import br.com.andre.api.external.LegacyUserClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestUserProvider implements UserStorageProvider,UserLookupProvider,CredentialInputValidator, UserQueryProvider{

	private final KeycloakSession session;
    private final ComponentModel model;
    private final LegacyUserClient client;
	
    public RestUserProvider(KeycloakSession session, ComponentModel model, LegacyUserClient client) {
    	this.session = session;
    	this.model = model;	
    	this.client = client;
    }
    
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		boolean response = supportsCredentialType(credentialType);
		log.info("[USER PROVIDER] - IS CONFIGURED FOR: " + credentialType + ", RESPONSE: " + response);
		return response;
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
		log.info("[USER PROVIDER] - VERIFYING PASSWORD");
		String response = client.verifyPassword(user.getUsername(), credentialInput.getChallengeResponse());
		if (response.equalsIgnoreCase("MATCH!")) {
			log.info("[USER PROVIDER] - MATCH!");
			return true;
		} return false;
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		return PasswordCredentialModel.TYPE.equals(credentialType);
	}

	@Override
	public UserModel getUserById(RealmModel realm, String id) {
		log.info("[USER PROVIDER] - FINDING BY ID: " + id);
		LegacyUser user = client.getUserById(StorageId.externalId(id));
		return adaptToModel(user, realm);
	}

	@Override
	public UserModel getUserByUsername(RealmModel realm, String username) {
		log.info("[USER PROVIDER] - FINDING BY USERNAME: " + username);
		LegacyUser user = client.getUserByUsername(username);
		//return new UserAdapter(session, realm, model, user);
		return adaptToModel(user, realm);
	}

	@Override
	public UserModel getUserByEmail(RealmModel realm, String email) {
		log.info("[USER PROVIDER] - FINDING BY EMAIL: " + email);
		String username = email.substring(0, email.indexOf('@'));
		return getUserByUsername(realm, username); 
	}
	
	//adapter
	private UserModel adaptToModel(LegacyUser user, RealmModel realm) {
		
		return new AbstractUserAdapter(session, realm, model) {
			
			@Override
			public String getUsername() {
				return user.getUsername();
			}
			
			@Override
			public SubjectCredentialManager credentialManager() {
				return new LegacyUserCredentialManager(session, realm, this);
			}
			
			@Override
			public String getEmail() {
				return user.getEmail();
			}
			
			@Override
			public String getFirstName() {
				return user.getFirstName();
			}
			
			@Override
			public String getLastName() {
				return user.getLastName();
			}
		};
		
	}

	@Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, String search) {
        return client.getAll().stream().map(legacy -> adaptToModel(legacy, realm));
    }
	
	@Override
	public Stream<UserModel> searchForUserStream(RealmModel realm, String search, Integer firstResult,
			Integer maxResults) {
		return searchForUserStream(realm, search);
	}

	@Override
	public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult,
			Integer maxResults) {
		return client.getAll().stream().map(legacy -> adaptToModel(legacy, realm));
	}

	@Override
	public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult,
			Integer maxResults) {
		return Stream.empty();
	}

	@Override
	public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
		return Stream.empty();
	}

}
