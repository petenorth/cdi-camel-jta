package org.pfry.cdijta.resources;

import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

public class TransactionManagerProducer {
	
	@Produces
	public TransactionManager getTransactionManager() throws NamingException
	{
		return (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
	}

}
