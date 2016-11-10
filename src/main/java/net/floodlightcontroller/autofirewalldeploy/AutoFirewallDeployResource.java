package net.floodlightcontroller.autofirewalldeploy;

import java.util.ArrayList;


import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class AutoFirewallDeployResource extends ServerResource {

	@Get("json")
	public ArrayList<Integer> retrieve() {
		AutoFirewallDeployService pihr = (AutoFirewallDeployService) getContext()
				.getAttributes().get(
						AutoFirewallDeployService.class.getCanonicalName());
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.addAll(pihr.getlist());
		return l;
	}

	@Post
	public String store(String fmJson) {
		AutoFirewallDeployService afd = (AutoFirewallDeployService) getContext()
				.getAttributes().get(
						AutoFirewallDeployService.class.getCanonicalName());

		int num = Integer.parseInt(fmJson);
		afd.addNumber(num);
		return "OK!";

	}
}
