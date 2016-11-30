package com.liuyiling.grpc.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import liuyiling.grpc.pb.GreeterGrpc;
import liuyiling.grpc.pb.HelloReply;
import liuyiling.grpc.pb.HelloRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuyl on 2016/11/30.
 */
public class HelloWorldClient {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServer.class);

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public HelloWorldClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
    }


    public HelloWorldClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name){
        logger.info("will try to greet " + name + "...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try{
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e){
            logger.warn("RPC faileds: {0}");
            return;
        }

        System.out.println(("Greeting:" + response.getMessage()));
    }

    public static void main(String[] agrs) throws InterruptedException {

        HelloWorldClient client = new HelloWorldClient("localhost", 50051);
        try{
            String user ="610";
            client.greet(user);
        }finally {
            client.shutdown();
        }

    }
}
