package br.cefetrj.sca.web.conversores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.service.UserProfileService;

/**
 * A converter class used in views to map id's to actual userProfile objects.
 */
@Component
public class RoleToUserProfileConverter implements
		Converter<Object, PerfilUsuario> {

	@Autowired
	UserProfileService userProfileService;

	/**
	 * Gets UserProfile by Id
	 * 
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	public PerfilUsuario convert(Object element) {
		Integer id = Integer.parseInt((String) element);
		PerfilUsuario profile = userProfileService.findById(id);
		System.out.println("Profile : " + profile);
		return profile;
	}

}