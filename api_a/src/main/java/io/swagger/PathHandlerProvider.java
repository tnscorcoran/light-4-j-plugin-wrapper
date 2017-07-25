package io.swagger;

import com.networknt.info.ServerInfoGetHandler;
import com.networknt.server.HandlerProvider;

import io.swagger.handler.Data10GetHandler;
import io.swagger.handler.Data11GetHandler;
import io.swagger.handler.Data12GetHandler;
import io.swagger.handler.Data13GetHandler;
import io.swagger.handler.Data14GetHandler;
import io.swagger.handler.Data15GetHandler;
import io.swagger.handler.Data16GetHandler;
import io.swagger.handler.Data17GetHandler;
import io.swagger.handler.Data18GetHandler;
import io.swagger.handler.Data19GetHandler;
import io.swagger.handler.Data1GetHandler;
import io.swagger.handler.Data20GetHandler;
import io.swagger.handler.Data21GetHandler;
import io.swagger.handler.Data22GetHandler;
import io.swagger.handler.Data23GetHandler;
import io.swagger.handler.Data24GetHandler;
import io.swagger.handler.Data25GetHandler;
import io.swagger.handler.Data2GetHandler;
import io.swagger.handler.Data3GetHandler;
import io.swagger.handler.Data4GetHandler;
import io.swagger.handler.Data5GetHandler;
import io.swagger.handler.Data6GetHandler;
import io.swagger.handler.Data7GetHandler;
import io.swagger.handler.Data8GetHandler;
import io.swagger.handler.Data9GetHandler;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
            .add(Methods.GET, "/apia/data10", new Data10GetHandler())
            .add(Methods.GET, "/apia/data11", new Data11GetHandler())
            .add(Methods.GET, "/apia/data12", new Data12GetHandler())
            .add(Methods.GET, "/apia/data13", new Data13GetHandler())
            .add(Methods.GET, "/apia/data14", new Data14GetHandler())
            .add(Methods.GET, "/apia/data15", new Data15GetHandler())
            .add(Methods.GET, "/apia/data16", new Data16GetHandler())
            .add(Methods.GET, "/apia/data17", new Data17GetHandler())
            .add(Methods.GET, "/apia/data18", new Data18GetHandler())
            .add(Methods.GET, "/apia/data19", new Data19GetHandler())
            .add(Methods.GET, "/apia/data1", new Data1GetHandler())
            .add(Methods.GET, "/apia/data20", new Data20GetHandler())
            .add(Methods.GET, "/apia/data21", new Data21GetHandler())
            .add(Methods.GET, "/apia/data22", new Data22GetHandler())
            .add(Methods.GET, "/apia/data23", new Data23GetHandler())
            .add(Methods.GET, "/apia/data24", new Data24GetHandler())
            .add(Methods.GET, "/apia/data25", new Data25GetHandler())
            .add(Methods.GET, "/apia/data2", new Data2GetHandler())
            .add(Methods.GET, "/apia/data3", new Data3GetHandler())
            .add(Methods.GET, "/apia/data4", new Data4GetHandler())
            .add(Methods.GET, "/apia/data5", new Data5GetHandler())
            .add(Methods.GET, "/apia/data6", new Data6GetHandler())
            .add(Methods.GET, "/apia/data7", new Data7GetHandler())
            .add(Methods.GET, "/apia/data8", new Data8GetHandler())
            .add(Methods.GET, "/apia/data9", new Data9GetHandler())
            .add(Methods.GET, "/apia/server/info", new ServerInfoGetHandler())
        ;
    }
}

