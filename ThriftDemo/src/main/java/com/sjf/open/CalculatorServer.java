package com.sjf.open;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 * Created by xiaosi on 16-9-20.
 */
public class CalculatorServer {

    private static int port = 9090;
    private static CalculatorHandler handler;
    private static CalculatorService.Processor processor;

    /**
     * 启动服务器
     * @param processor
     */
    public static void start(CalculatorService.Processor processor){
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        handler = new CalculatorHandler();
        processor = new CalculatorService.Processor(handler);
        start(processor);
    }
}
