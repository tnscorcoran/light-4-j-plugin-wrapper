package io.swagger;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import io.swagger.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
            .add(Methods.GET, "/apib/data10", new Data10GetHandler())
            .add(Methods.GET, "/apib/data11", new Data11GetHandler())
            .add(Methods.GET, "/apib/data12", new Data12GetHandler())
            .add(Methods.GET, "/apib/data13", new Data13GetHandler())
            .add(Methods.GET, "/apib/data14", new Data14GetHandler())
            .add(Methods.GET, "/apib/data15", new Data15GetHandler())
            .add(Methods.GET, "/apib/data16", new Data16GetHandler())
            .add(Methods.GET, "/apib/data17", new Data17GetHandler())
            .add(Methods.GET, "/apib/data18", new Data18GetHandler())
            .add(Methods.GET, "/apib/data19", new Data19GetHandler())
            .add(Methods.GET, "/apib/data1", new Data1GetHandler())
            .add(Methods.GET, "/apib/data20", new Data20GetHandler())
            .add(Methods.GET, "/apib/data21", new Data21GetHandler())
            .add(Methods.GET, "/apib/data22", new Data22GetHandler())
            .add(Methods.GET, "/apib/data23", new Data23GetHandler())
            .add(Methods.GET, "/apib/data24", new Data24GetHandler())
            .add(Methods.GET, "/apib/data25", new Data25GetHandler())
            .add(Methods.GET, "/apib/data2", new Data2GetHandler())
            .add(Methods.GET, "/apib/data3", new Data3GetHandler())
            .add(Methods.GET, "/apib/data4", new Data4GetHandler())
            .add(Methods.GET, "/apib/data5", new Data5GetHandler())
            .add(Methods.GET, "/apib/data6", new Data6GetHandler())
            .add(Methods.GET, "/apib/data7", new Data7GetHandler())
            .add(Methods.GET, "/apib/data8", new Data8GetHandler())
            .add(Methods.GET, "/apib/data9", new Data9GetHandler())
            .add(Methods.GET, "/apib/server/info", new ServerInfoGetHandler())
        ;
    }
}

