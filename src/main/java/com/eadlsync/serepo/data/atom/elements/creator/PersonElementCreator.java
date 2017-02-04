package com.eadlsync.serepo.data.atom.elements.creator;

import java.lang.reflect.Field;

import com.eadlsync.serepo.data.atom.annotations.Annotations;
import com.eadlsync.serepo.data.atom.annotations.AtomPerson;
import org.jboss.resteasy.plugins.providers.atom.Person;

public class PersonElementCreator extends AbstractAtomElementListCreator<Person> {

	public PersonElementCreator() {
	}

	@Override
	protected Person createElement(Object object) throws Exception {

		Annotations.throwExceptionIfNotPresent(object, AtomPerson.class);

		Person person = new Person();

		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(AtomPerson.Name.class)) {
				person.setName((String) field.get(object));
			} else if (field.isAnnotationPresent(AtomPerson.Email.class)) {
				person.setEmail((String) field.get(object));
			}
		}

		return person;

	}

}
