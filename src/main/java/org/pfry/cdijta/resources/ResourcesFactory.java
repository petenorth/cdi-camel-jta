package org.pfry.cdijta.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.TransactionManager;

import org.apache.camel.cdi.CdiBeanRegistry;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * ResourcesFactory is where we produce the main resources used by this project.
 * 
 * Pay extra attention to the {@link Named} annotation. In order to add the
 * {@link PlatformTransactionManager} and the {@link SpringTransactionPolicy} to
 * {@link CdiBeanRegistry} we can't just produce them like we would do for CDI
 * access. The problem is that by default CDI is accessing the objects by type.
 * Camel though, does not work like this. Camel is Spring based so it access
 * them by name. Thus, we need the {@link Named} annotation to define their name
 * by the name Camel is expecting to find, or else {@link CdiBeanRegistry} wont
 * contain them.
 * 
 * @author patouchas, pfry
 *
 */
@ApplicationScoped
public class ResourcesFactory {
	
	@Inject
	TransactionManager transactionManager;

	@Produces
	@Named("transactionManager")
	@ApplicationScoped
	public PlatformTransactionManager getSpringTransactionManager() {
		return new JtaTransactionManager(transactionManager);
	}

	@Produces
	@Named("PROPAGATION_REQUIRED")
	@ApplicationScoped
	public SpringTransactionPolicy getPropagationRequired(
			@Named("transactionManager") PlatformTransactionManager manager) {
		SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
		propagationRequired.setTransactionManager(manager);
		propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
		return propagationRequired;
	}
	
	@Produces
	@Named("PROPAGATION_MANDATORY")
	@ApplicationScoped
	public SpringTransactionPolicy getPropagationMandatory(
			@Named("transactionManager") PlatformTransactionManager manager) {
		SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
		propagationRequired.setTransactionManager(manager);
		propagationRequired.setPropagationBehaviorName("PROPAGATION_MANDATORY");
		return propagationRequired;
	}	
	
}

