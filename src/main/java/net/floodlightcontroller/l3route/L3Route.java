package net.floodlightcontroller.l3route;

import io.netty.handler.codec.http.HttpHeaders.Names;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.ArpOpcode;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPAddress;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;
import org.python.antlr.PythonParser.return_stmt_return;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.OFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Set;

import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Data;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.UDP;
import net.floodlightcontroller.util.OFMessageDamper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//net.floodlightcontroller.l3route.L3Route

public class L3Route implements IOFMessageListener,IFloodlightModule{

	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	protected Set<IPAddress> ipAddresses;
	protected OFMessageDamper messageDamper;
	protected static int OFMESSAGE_DAMPER_CAPACITY = 10000; // TODO: find sweet spot
	protected static int OFMESSAGE_DAMPER_TIMEOUT = 250; // ms
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return L3Route.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
	     return ((type == OFType.PACKET_IN || type == OFType.FLOW_MOD)
	                && name.equals( "forwarding" ));
	}

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
	    macAddresses = new ConcurrentSkipListSet<Long>();
	    ipAddresses = new ConcurrentSkipListSet<IPAddress>();
	    messageDamper = new OFMessageDamper(OFMESSAGE_DAMPER_CAPACITY,
				EnumSet.of(OFType.FLOW_MOD),
				OFMESSAGE_DAMPER_TIMEOUT);
	    logger = LoggerFactory.getLogger(L3Route.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		// TODO Auto-generated method stub
		
		 OFPacketIn pi=(OFPacketIn)msg;

		 Ethernet eth =
	                IFloodlightProviderService.bcStore.get(cntx,
	                                            IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		 
		System.out.println(eth+" .............");
		 
		 if(eth.getEtherType()==EthType.ARP && eth.isBroadcast()){
			 MacAddress srcmac=eth.getSourceMACAddress();
			 ARP arp=(ARP) eth.getPayload();
			 IPAddress dstip=arp.getTargetProtocolAddress();
			 OFPort port=(pi.getVersion().compareTo(OFVersion.OF_12) < 0 ? pi.getInPort() : pi.getMatch().get(MatchField.IN_PORT));
			 if(dstip.equals(IPAddress.of("10.0.0.254"))){
				 

					Ethernet l2 = new Ethernet();
					l2.setSourceMACAddress(MacAddress.of("00:00:00:00:07:10"));
					l2.setDestinationMACAddress(MacAddress.of("00:00:00:00:00:01"));
					l2.setEtherType(EthType.ARP);
					
					ARP l3=new ARP();
					 l3.setHardwareType(ARP.HW_TYPE_ETHERNET);
		             l3.setProtocolType(ARP.PROTO_TYPE_IP);
		             l3.setHardwareAddressLength((byte) 6);
		             l3.setProtocolAddressLength((byte) 4);
					
					l3.setSenderProtocolAddress(IPv4Address.of("10.0.0.254"));
					l3.setTargetProtocolAddress(IPv4Address.of("10.0.0.1"));
					l3.setSenderHardwareAddress(MacAddress.of("00:00:00:00:07:10"));
					l3.setTargetHardwareAddress(MacAddress.of("00:00:00:00:00:01"));
					l3.setOpCode(ARP.OP_REPLY);
							
				
					
					l2.setPayload(l3);



					System.out.println(l2+" *-*-*- ");
					
					byte[] serializedData = l2.serialize();
					
					
					OFPacketOut po = sw.getOFFactory().buildPacketOut() /* mySwitch is some IOFSwitch object */
					    .setData(serializedData)
					    .setActions(Collections.singletonList((OFAction)sw.getOFFactory().actions().output(port, 0xffFFffFF)))
					    .setInPort(OFPort.CONTROLLER)
					    .build();
					sw.write(po);
				 
				 return Command.STOP;
				 
			 }
			 if(dstip.equals(IPAddress.of("20.0.0.254"))){
				 

					Ethernet l2 = new Ethernet();
					l2.setSourceMACAddress(MacAddress.of("00:00:00:00:08:23"));
					l2.setDestinationMACAddress(MacAddress.of("00:00:00:00:00:02"));
					l2.setEtherType(EthType.ARP);
					
					ARP l3=new ARP();
					 l3.setHardwareType(ARP.HW_TYPE_ETHERNET);
		             l3.setProtocolType(ARP.PROTO_TYPE_IP);
		             l3.setHardwareAddressLength((byte) 6);
		             l3.setProtocolAddressLength((byte) 4);
					
					l3.setSenderProtocolAddress(IPv4Address.of("20.0.0.254"));
					l3.setTargetProtocolAddress(IPv4Address.of("20.0.0.1"));
					l3.setSenderHardwareAddress(MacAddress.of("00:00:00:00:08:23"));
					l3.setTargetHardwareAddress(MacAddress.of("00:00:00:00:00:02"));
					l3.setOpCode(ARP.OP_REPLY);
							
				
					
					l2.setPayload(l3);
//					l3.setPayload(l4);
//					l4.setParent(l7);



					System.out.println(l2+" *-*-*- ");
					
					byte[] serializedData = l2.serialize();
					
					
					OFPacketOut po = sw.getOFFactory().buildPacketOut() /* mySwitch is some IOFSwitch object */
					    .setData(serializedData)
					    .setActions(Collections.singletonList((OFAction)sw.getOFFactory().actions().output(OFPort.of(2), 0xffFFffFF)))
					    .setInPort(OFPort.CONTROLLER)
					    .build();
				 
				    sw.write(po);
				 return Command.STOP;
				 
			 }

		 }
		return Command.CONTINUE;
	}
	
	public OFPacketOut generateOfPacketOut(IOFSwitch mySwitch,OFPort pt){

		Ethernet l2 = new Ethernet();
		l2.setSourceMACAddress(MacAddress.of("00:00:00:00:07:10"));
		l2.setDestinationMACAddress(MacAddress.of("00:00:00:00:00:01"));
		l2.setEtherType(EthType.ARP);
		
		ARP l3=new ARP();
		l3.setSenderProtocolAddress(IPv4Address.of("10.0.0.254"));
		l3.setTargetProtocolAddress(IPv4Address.of("10.0.0.1"));
		l3.setSenderHardwareAddress(MacAddress.of("00:00:00:00:07:10"));
		l3.setTargetHardwareAddress(MacAddress.of("00:00:00:00:00:01"));
		l3.setOpCode(ArpOpcode.REPLY);
				
	
		
		l2.setPayload(l3);
//		l3.setPayload(l4);
//		l4.setParent(l7);



		System.out.println(l2+" *-*-*- ");
		
		byte[] serializedData = l2.serialize();
		
		
		OFPacketOut po = mySwitch.getOFFactory().buildPacketOut() /* mySwitch is some IOFSwitch object */
		    .setData(serializedData)
		    .setActions(Collections.singletonList((OFAction) mySwitch.getOFFactory().actions().output(pt, 0xffFFffFF)))
		    .setInPort(pt)
		    .build();
		
		return po;
		
	}

}
