package org.pfry;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jboss.narayana.quickstarts.jta.jpa.TestEntity;
import org.jboss.narayana.quickstarts.jta.jpa.TestEntityRepository;

public class JTAProcessor {
	
	@Inject
	TestEntityRepository testEntityRepository;

	public void doSomething() throws NamingException, NotSupportedException, SystemException{
		testEntityRepository.save(new TestEntity("HELLO"));
	}

}
