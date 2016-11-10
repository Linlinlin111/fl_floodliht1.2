package net.floodlightcontroller.autofirewalldeploy;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.projectfloodlight.openflow.types.IPv4Address;

import net.floodlightcontroller.autofirewalldeploy.AutoFirewallDeploy.Decision;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//net.floodlightcontroller.autofirewalldeploy.InsertRules

public class InsertRules implements IFloodlightModule{

	
	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;
	
	
	
	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// TODO Auto-generated method stub
		  Collection<Class<? extends IFloodlightService>> l =
		            new ArrayList<Class<? extends IFloodlightService>>();
		        l.add(IFloodlightProviderService.class);
		        return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
	    floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    logger = LoggerFactory.getLogger(InsertRules.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
		sendRule();
	}

	private void sendRule(){
		 Thread t2 = new Thread(
					new Runnable(){
						@Override
						public void run() {
							
							while(true){
								try {
									System.out.println("Checking rules:");
									for(RulePair rule:AutoFirewallDeploy.decision_map.keySet()){
										System.out.println(rule+" "+AutoFirewallDeploy.decision_map.get(rule));
									}
									Thread.sleep(10000);
									System.out.println("in InserRules Thread");
									RulePair flow_rule=new RulePair.Builder().srcIp(IPv4Address.of("10.0.0.1")).dstIp(IPv4Address.of("10.0.0.2")).build();
									Decision decision=Decision.DENY;
									System.out.println(flow_rule.toString());
									
									
									Iterator<RulePair> iterator=AutoFirewallDeploy.decision_map.keySet().iterator();
									
									
									while(iterator.hasNext()){
										RulePair rule=iterator.next();
										if(rule.equals(flow_rule)){
											if(!(AutoFirewallDeploy.decision_map.get(rule)==decision)){
												System.out.println("in InsertRules find: "+flow_rule.toString()+" decision change from " +AutoFirewallDeploy.decision_map.get(flow_rule)+" to "+decision);
												iterator.remove();
												AutoFirewallDeploy.decision_map.put(flow_rule, decision);
												break;
											}
										}
										
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}						
						}
					}
			);
			t2.start();
	}
	


}
