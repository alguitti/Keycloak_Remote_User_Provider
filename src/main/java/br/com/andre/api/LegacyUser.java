package br.com.andre.api;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegacyUser implements Serializable{

	private static final long serialVersionUID = -8814286513325184019L;

	private String id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String password;

}
