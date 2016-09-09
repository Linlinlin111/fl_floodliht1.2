package net.floodlightcontroller.autofirewalldeploy;


import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;

public class RulePair {
	private IPv4Address ipsrc;
	private IPv4Address ipdst;
	private MacAddress macsrc;
	private MacAddress macdst;
	private OFPort port;
	
	public static class Builder{
		private IPv4Address ipsrc=IPv4Address.of("1.2.3.4");
		private IPv4Address ipdst=IPv4Address.of("1.2.3.4");
		private MacAddress macsrc=MacAddress.of("00:01:6C:06:A6:29");
		private MacAddress macdst=MacAddress.of("00:01:6C:06:A6:29");
		private OFPort port=OFPort.of(-1);
		
		public Builder(){
			
		}
		
		public Builder srcIp(IPv4Address ip){
			this.ipsrc=ip;
			return this;		
		}
		public Builder dstIp(IPv4Address ip){
			this.ipdst=ip;
			return this;		
		}
		public Builder srcMac(MacAddress mac){
			this.macsrc=mac;
			return this;		
		}
		public Builder dstMac(MacAddress mac){
			this.macsrc=mac;
			return this;		
		}
		public Builder port(OFPort port){
			this.port=port;
			return this;		
		}
		
		public RulePair build(){
			return new RulePair(this);
		}
	}
	
	private RulePair(Builder builder){
		ipsrc=builder.ipsrc;
		ipdst=builder.ipdst;
		macsrc=builder.macsrc;
		macdst=builder.macdst;
		port=builder.port;
	}
	
	
	public boolean equals(RulePair  pair){
		
		return this.ipsrc.equals(pair.ipsrc)
			&& this.ipdst.equals(pair.ipdst)
			&& this.macsrc.equals(pair.macsrc)
			&& this.macdst.equals(pair.macdst)
			&& this.port.equals(pair.port);
	}

}
