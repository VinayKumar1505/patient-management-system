package com.pm.patient_service.grpc;


import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import billing.BillingServiceGrpc;
import billing.BillingRequest;
import billing.BillingResponse;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger Log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub; //lets you call methods on that server.

    public BillingServiceGrpcClient(
        @Value("${billing.service.grpc.address:localhost}") String serverAddress,
        @Value("${billing.service.grpc.port:9001}") int serverPort
        ) {
        Log.info("Initializing BillingServiceGrpcClient with server address: {} and port: {}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();// establishes communication with the gRPC server.
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
        }

    public BillingResponse createAccount(String patientId, String name, String email){
        
        Log.info("Creating billing account for patientId: {}, name: {}, email: {}", patientId, name, email);
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();
        BillingResponse response = blockingStub.createBillingAccount(request);
        Log.info("Billing account created response: {}", response);
        return response;
    }

}