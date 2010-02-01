package org.apertium.api.translate;

import java.util.*;

public class Services {
	public enum ServiceType { APERTIUM, GOOGLE };
	
	private Map<String, ServiceType> serviceMap = null;
	private Map<ServiceType, String> revServiceMap = null;
	
	public Services() {
		serviceMap = new HashMap<String, ServiceType>();
		
		serviceMap.put("Apertium XML-RPC", ServiceType.APERTIUM);
		serviceMap.put("Google", ServiceType.GOOGLE);
		
		revServiceMap = new HashMap<ServiceType, String>();
		for (String l : serviceMap.keySet()) {
			revServiceMap.put(serviceMap.get(l), l);
		}
	}
	
	public Set<String> getServices() {
		return serviceMap.keySet();
	}
	
	public Set<ServiceType> getServiceTypes() {
		return revServiceMap.keySet();
	}
	
	public String getService(ServiceType t) {
		return revServiceMap.get(t);
	}
	
	public ServiceType getServiceType(String s) {
		return serviceMap.get(s);
	}
}
