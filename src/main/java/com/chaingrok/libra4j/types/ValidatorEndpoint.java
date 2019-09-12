package com.chaingrok.libra4j.types;

import org.libra.grpc.admission_control.AdmissionControlGrpc;
import org.libra.grpc.admission_control.AdmissionControlGrpc.AdmissionControlBlockingStub;
import org.libra.grpc.admission_control.AdmissionControlGrpc.AdmissionControlStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ValidatorEndpoint {
	
	private String dns = null;
	private int port = 0;
	
	private AdmissionControlBlockingStub blockingStub = null;
	private AdmissionControlStub asyncStub = null;
	
	public ValidatorEndpoint(String dns, int port) {
		this.dns = dns;
		this.port = port;
		ManagedChannel channel = ManagedChannelBuilder.forAddress(dns,port).usePlaintext().build();
		blockingStub = AdmissionControlGrpc.newBlockingStub(channel);
		asyncStub = AdmissionControlGrpc.newStub(channel);
	}

	public String getDns() {
		return dns;
	}

	public int getPort() {
		return port;
	}

	public AdmissionControlBlockingStub getBlockingStub() {
		return blockingStub;
	}

	public AdmissionControlStub getAsyncStub() {
		return asyncStub;
	}
	
}
