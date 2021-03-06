package net.floodlightcontroller.autofirewalldeploy;


import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;

public class RulePair {
	private int ruleid;
	private IPv4Address ipsrc;
	private IPv4Address ipdst;
	private MacAddress macsrc;
	private MacAddress macdst;
	private OFPort port;
	

	public static class Builder{
		private int ruleid = 0;
		private IPv4Address ipsrc=IPv4Address.of("1.2.3.4");
		private IPv4Address ipdst=IPv4Address.of("1.2.3.4");
		private MacAddress macsrc=MacAddress.of("00:01:6C:06:A6:29");
		private MacAddress macdst=MacAddress.of("00:01:6C:06:A6:29");
		private OFPort port=OFPort.of(-1);
		
		public Builder(){
			
		}
		
		
		public Builder ruleid(int ruleid){
			this.ruleid=ruleid;
			return this;
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
		
		public RulePair  build(){
			return new RulePair(this);
		}
	}
	
	private RulePair (Builder builder){
		ipsrc=builder.ipsrc;
		ipdst=builder.ipdst;
		macsrc=builder.macsrc;
		macdst=builder.macdst;
		port=builder.port;
	}
	
	
	public boolean equals(RulePair   pair){
		
		return this.ipsrc.equals(pair.ipsrc)
			&& this.ipdst.equals(pair.ipdst)
			&& this.macsrc.equals(pair.macsrc)
			&& this.macdst.equals(pair.macdst)
			&& this.port.equals(pair.port);
	}
	
	public String toString(){
		String result="[ ";
		if(ruleid!=0){
			result+=" ruleid:"+ruleid;
		}
		if(!ipsrc.equals(IPv4Address.of("1.2.3.4"))){
			result+=" ip_src:"+ipsrc;
		}
		if(!ipdst.equals(IPv4Address.of("1.2.3.4"))){
			result+=" ip_dst:"+ipdst;
		}
		if(!macsrc.equals(MacAddress.of("00:01:6C:06:A6:29"))){
			result+=" mac_src:"+macsrc;
		}
		if(!macdst.equals(MacAddress.of("00:01:6C:06:A6:29"))){
			result+=" mac_dst:"+macdst;
		}
		result+=" ]";
		return result;
	}
	public int getRuleid(){
		return this.ruleid;
	}

}
