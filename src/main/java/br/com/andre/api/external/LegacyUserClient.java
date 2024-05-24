package br.com.andre.api.external;

import java.util.List;

import br.com.andre.api.LegacyUser;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface LegacyUserClient {

	@GET
	List<LegacyUser> getAll();

	@GET
	@Path("/username/{username}")
	LegacyUser getUserByUsername(@PathParam("username") String username);
	
	@GET
	@Path("/verify/{username}/{password}")
	String verifyPassword(@PathParam("username") String username, @PathParam("password") String password);
	
	@GET
	@Path("/{id}")
	LegacyUser getUserById(@PathParam("id") String id);
	
	
}
