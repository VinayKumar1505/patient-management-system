package com.pm.billing_service.grpc;

import billing.BillingServiceGrpc.BillingServiceImplBase;
import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingServiceGrpc extends BillingServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(BillingServiceGrpc.class);
    // Implement your gRPC service methods here
    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {

        logger.info("Received request to create billing account for user: " + billingRequest.toString());
        // Your implementation here
        BillingResponse response = BillingResponse.newBuilder().setAccountId("123").setStatus("ACTIVE").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
