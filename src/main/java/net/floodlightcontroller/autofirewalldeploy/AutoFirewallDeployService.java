package net.floodlightcontroller.autofirewalldeploy;

import java.util.ArrayList;
import net.floodlightcontroller.core.module.IFloodlightService;

public interface AutoFirewallDeployService extends IFloodlightService{

//	public void addRule(RulePair rule);
//	
//	public void removeRule(int ruleid);
//	
//	public HashMap<RulePair,Decision> getRules();
//	
	public void addNumber(int num);
	
	public ArrayList<Integer> getlist();
}
