package net.floodlightcontroller.autofirewalldeploy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.IListener.Command;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;


import net.floodlightcontroller.core.IFloodlightProviderService;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Set;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.restserver.IRestApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



//net.floodlightcontroller.autofirewalldeploy.AutoFirewallDeploy 

public class AutoFirewallDeploy implements IFloodlightModule,IOFMessageListener,AutoFirewallDeployService{

	
	
	protected IFloodlightProviderService floodlightProvider;
//	protected Set<Long> macAddresses;
	protected static Logger logger;
	public enum Decision{ALLOW,DENY};
	protected static HashMap<RulePair,Decision> decision_map;
	protected ArrayList<Integer> testList;
	
	protected IRestApiService restApi;
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return AutoFirewallDeploy.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
	     return (type == OFType.PACKET_IN && name.equals( "forwarding" ));
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		// TODO Auto-generated method stub
	     Ethernet eth =
	                IFloodlightProviderService.bcStore.get(cntx,
	                                            IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	     if(eth.getEtherType()==EthType.IPv4){
	    	 IPv4 ip = (IPv4) eth.getPayload();
			 IPv4Address srcIp = ip.getSourceAddress();
			 IPv4Address dstIp = ip.getDestinationAddress();
			 RulePair flow_test=new RulePair.Builder().srcIp(srcIp).dstIp(dstIp).build();
			 for(RulePair pair:decision_map.keySet()){
				 if(flow_test.equals(pair)){
					 if(decision_map.get(pair)==Decision.ALLOW){
						 //do sth. when the firewall allow
						 return Command.CONTINUE;
					 }
					 else if(decision_map.get(pair)==Decision.DENY){
						//do sth. when the firewall deny
						 return Command.STOP;
					 }
					 else{
						 logger.error("No such decision in AutoFirewallDeploy");
					 }
				 }
				 
			 }
	     }
		return Command.CONTINUE;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		 Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		    l.add(AutoFirewallDeployService.class);
		    return l;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		 Map<Class<? extends IFloodlightService>, IFloodlightService> m = new HashMap<Class<? extends IFloodlightService>, IFloodlightService>();
		    m.put(AutoFirewallDeployService.class, this);
		    return m;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// TODO Auto-generated method stub
		 Collection<Class<? extends IFloodlightService>> l =
			        new ArrayList<Class<? extends IFloodlightService>>();
			    l.add(IFloodlightProviderService.class);
			    l.add(IRestApiService.class);
			    return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
		 floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
//		    macAddresses = new ConcurrentSkipListSet<Long>();
		    logger = LoggerFactory.getLogger(AutoFirewallDeploy.class);
		    decision_map=new HashMap<>();
//		    decision_map.put(new RulePair.Builder()
//		    					.srcIp(IPv4Address.of("10.0.0.1"))
//		    					.dstIp(IPv4Address.of("10.0.0.2"))
//		    					.build(), 
//		    					Decision.ALLOW);
//		    decision_map.put(new RulePair.Builder()
//		    					.srcIp(IPv4Address.of("10.0.0.2"))
//		    					.dstIp(IPv4Address.of("10.0.0.1"))
//		    					.build(),
//		    					Decision.ALLOW);
		    restApi = context.getServiceImpl(IRestApiService.class);
		    testList = new ArrayList<Integer>();
		    testList.add(710);
		    testList.add(823);
	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
		  floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		  restApi.addRestletRoutable(new AutoFirewallDeployRoutable());
	}

//	@Override
//	public void addRule(RulePair rule) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void removeRule(int ruleid) {
//		// TODO Auto-generated method stub
//		Iterator<RulePair> it=decision_map.keySet().iterator();
//		while(it.hasNext()){
//			RulePair rp=it.next();
//			int id = rp.getRuleid();
//			if(id==ruleid){
//				it.remove();
//				break;
//			}
//			
//		}
//	}
//
//	@Override
//	public HashMap<RulePair, Decision> getRules() {
//		// TODO Auto-generated method stub
//		return this.decision_map;
//	}
//
	@Override
	public void addNumber(int num) {
		// TODO Auto-generated method stub
		testList.add(num);
	}

	@Override
	public ArrayList<Integer> getlist() {
		// TODO Auto-generated method stub
	System.out.println("********************** "+testList);	
		return this.testList;
	}

}
