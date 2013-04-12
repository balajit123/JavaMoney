/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kathez.javamoney.provider;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author thennarangamb
 */
public class ScripsProviderImplTest extends TestCase {
    
    private ScripProvider scripsProvider;
    
    public ScripsProviderImplTest(String testName) {
        super(testName);
    }
    
    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();
        scripsProvider = new ScripsProviderImpl(new YahooDataSource("proxy.logica.com"));
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        scripsProvider = null;
    }
    
    @Test
    public void testGetCurrency(){
        System.out.println(scripsProvider.getScrips());
        assert(scripsProvider.getScrips() != null && !scripsProvider.getScrips().isEmpty());
//        for(String scripId : currencyFromFile.getScrips().keySet()){
//            System.out.println(currencyFromFile.getScrips().get(scripId).getScrips());
//            System.out.println(currencyFromFile.getScrips().get(scripId).getScrips().getValidFrom());
//            System.out.println(currencyFromFile.getScrips().get(scripId).getScrips().getValidUntil());
//        }
    }
    
    @Test
    public void testGetCurrencyString(){
        assertNotNull(scripsProvider.getScrip("DELL"));
        System.out.println(scripsProvider.getScrip("DELL"));
    }
    
}
