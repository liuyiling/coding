package com.liuyiling.grpc.service;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import liuyiling.grpc.pb.GreeterGrpc;
import liuyiling.grpc.pb.HelloReply;
import liuyiling.grpc.pb.HelloRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyl on 2016/11/30.
 */
public class HelloWorldServer {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServer.class);

    private Server server;

    private void start() throws Exception {

        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();

        logger.info("Sever started,listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                HelloWorldServer.this.stop();
                System.err.println("*** server shut down");
            }
        });

    }

    private void stop(){
        if(server != null){
            server.shutdown();
        }
    }

    private void blockUtinlShutdonw() throws InterruptedException {
        if(server != null){
            //服务端阻塞直到退出
            server.awaitTermination();
        }
    }

    public static void main(String[] agrs) throws Exception {
        final HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUtinlShutdonw();
    }


    //server端实现类-拓展原有的接口
    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello:" + request.getName()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

}
