package org.pfry.cdijta.boot;

import javax.naming.InitialContext;

import org.h2.jdbcx.JdbcDataSource;
import org.jnp.interfaces.NamingParser;
import org.jnp.server.NamingBeanImpl;

import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.arjuna.ats.jta.utils.JNDIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultModelJAXBContextFactory;
import org.apache.camel.main.MainSupport;
import org.apache.camel.spi.ModelJAXBContextFactory;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Allows Camel and CDI applications to be booted up on the command line as a
 * Java Application
 */
public abstract class Main extends MainSupport { // abstract to prevent cdi
													// management

	Logger log = LoggerFactory.getLogger(this.getClass());

	private static final NamingBeanImpl NAMING_BEAN = new NamingBeanImpl();

	private static Main instance;
	private JAXBContext jaxbContext;
	private Object cdiContainer; // we don't want to need cdictrl API in OSGi

	public Main() {
		// add options...
	}

	public static void main(String... args) throws Exception {
		Main main = new Main() {
		};
		instance = main;
		main.enableHangupSupport();
		main.run(args);
	}

	/**
	 * Returns the currently executing main
	 *
	 * @return the current running instance
	 */
	public static Main getInstance() {
		return instance;
	}

	@Override
	protected ProducerTemplate findOrCreateCamelTemplate() {
		ProducerTemplate answer = BeanProvider.getContextualReference(ProducerTemplate.class, true);
		if (answer != null) {
			return answer;
		}
		if (getCamelContexts().isEmpty()) {
			throw new IllegalArgumentException("No CamelContexts are available so cannot create a ProducerTemplate!");
		}
		return getCamelContexts().get(0).createProducerTemplate();
	}

	@Override
	protected Map<String, CamelContext> getCamelContextMap() {
		List<CamelContext> contexts = BeanProvider.getContextualReferences(CamelContext.class, true);
		Map<String, CamelContext> answer = new HashMap<String, CamelContext>();
		for (CamelContext context : contexts) {
			String name = context.getName();
			answer.put(name, context);
		}
		return answer;
	}

	public ModelJAXBContextFactory getModelJAXBContextFactory() {
		return new DefaultModelJAXBContextFactory();
	}

	@Override
	protected void doStart() throws Exception {
		doJTAStart();
		org.apache.deltaspike.cdise.api.CdiContainer container = org.apache.deltaspike.cdise.api.CdiContainerLoader
				.getCdiContainer();
		container.boot();
		container.getContextControl().startContexts();
		cdiContainer = container;
		super.doStart();
	}

	@Override
	protected void doStop() throws Exception {
		super.doStop();
		if (cdiContainer != null) {
			((org.apache.deltaspike.cdise.api.CdiContainer) cdiContainer).shutdown();
		}
		doJTAStop();
	}

	protected void doJTAStart() throws Exception {

		log.info("starting JTA stuff");
		// Start JNDI server
		NAMING_BEAN.start();

		// Bind JTA implementation with default names
		JNDIManager.bindJTAImplementation();

		// Bind JTA implementation with JBoss names. Needed for JTA 1.2
		// implementation.
		// See https://issues.jboss.org/browse/JBTM-2054
		NAMING_BEAN.getNamingInstance().createSubcontext(new NamingParser().parse("jboss"));
		jtaPropertyManager.getJTAEnvironmentBean().setTransactionManagerJNDIContext("java:/jboss/TransactionManager");
		jtaPropertyManager.getJTAEnvironmentBean()
				.setTransactionSynchronizationRegistryJNDIContext("java:/jboss/TransactionSynchronizationRegistry");
		JNDIManager.bindJTAImplementation();

		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:test;MODE=Oracle;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;");
		dataSource.setUser("sa");
		dataSource.setPassword("");
		InitialContext context = new InitialContext();
		context.bind("java:/jboss/testDS1", dataSource);

		log.info("done JTA stuff");
		super.doStart();
	}

	public void doJTAStop() throws Exception {
		super.doStop();
		// Stop JNDI server
		NAMING_BEAN.stop();
	}
}
