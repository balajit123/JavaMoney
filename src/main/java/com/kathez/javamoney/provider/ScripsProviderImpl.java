package com.kathez.javamoney.provider;

import com.kathez.javamoney.domain.Scrip;
import java.util.Map;
import javax.inject.Inject;

/**
 * 
 * @author Balaji Thennarangam
 */
public class ScripsProviderImpl implements ScripProvider{
    
    @Inject
    private ScripDataSource dataSource;

    public ScripsProviderImpl() {
        
    }
    
    @Inject
    public ScripsProviderImpl(ScripDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     */
    public Map<String,Scrip> getScrips() {        
        return dataSource.getScrips();
    }

    public Scrip getScrip(String scripId) {
        return getScrips().get(scripId);
    }
    
}