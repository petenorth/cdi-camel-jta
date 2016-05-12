package org.pfry.cdijta.route;

import java.util.List;

import org.jboss.narayana.quickstarts.jta.jpa.TestEntity;

public class CountProcessor {
	
	public String getCount(List<TestEntity> entities)
	{
		return "Count of TestEntities is " + entities.size();
	}

}
