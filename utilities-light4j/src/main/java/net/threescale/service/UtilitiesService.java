package net.threescale.service;

import java.io.IOException;

import net.threescale.model.GenericResult;
import threescale.v3.api.ServerError;

public interface UtilitiesService {
    public String PROPERTIES_FILE = "/props.properties";			

    public GenericResult createMethods() throws ServerError, IOException;
    public GenericResult buildCSVfiles() throws ServerError, IOException;
    public GenericResult writeMethodSystemNames() throws ServerError, IOException;
    public GenericResult writeAPIKeys() throws ServerError, IOException;
    public GenericResult createAccountsAndApplications() throws ServerError, IOException;
}
