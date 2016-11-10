package net.floodlightcontroller.l3route;

import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.TransportPort;

public class Connection {
	IPv4Address  IP_SRC;
	TransportPort PORT_SRC;
	IPv4Address  IP_DST;
	TransportPort PORT_DST;
	String OTHER_INFO;
	
	public Connection(IPv4Address iP_SRC, TransportPort pORT_SRC, IPv4Address iP_DST,
			TransportPort pORT_DST, String oTHER_INFO) {
		IP_SRC = iP_SRC;
		PORT_SRC = pORT_SRC;
		IP_DST = iP_DST;
		PORT_DST = pORT_DST;
		OTHER_INFO = oTHER_INFO;
	}
	public IPv4Address getIP_SRC() {
		return IP_SRC;
	}
	public void setIP_SRC(IPv4Address iP_SRC) {
		IP_SRC = iP_SRC;
	}
	public TransportPort getPORT_SRC() {
		return PORT_SRC;
	}
	public void setPORT_SRC(TransportPort pORT_SRC) {
		PORT_SRC = pORT_SRC;
	}
	public IPv4Address getIP_DST() {
		return IP_DST;
	}
	public void setIP_DST(IPv4Address iP_DST) {
		IP_DST = iP_DST;
	}
	public TransportPort getPORT_DST() {
		return PORT_DST;
	}
	public void setPORT_DST(TransportPort pORT_DST) {
		PORT_DST = pORT_DST;
	}
	public String getOTHER_INFO() {
		return OTHER_INFO;
	}
	public void setOTHER_INFO(String oTHER_INFO) {
		OTHER_INFO = oTHER_INFO;
	}
}
