/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kathez.javamoney.converter.spi;

import javax.money.CurrencyUnit;
import javax.money.convert.ExchangeRate;
import net.java.javamoney.ri.core.MoneyCurrency;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author thennarangamb
 */
public class KathezIMFExchangeRateProviderSpiTest {
    
    private KathezIMFExchangeRateProviderSpi provider = null;
    
    public KathezIMFExchangeRateProviderSpiTest() {
        provider = new KathezIMFExchangeRateProviderSpi("proxy.logica.com");
    }
    
    @BeforeClass
    public static void setUpClass() {        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }    

    /**
     * Test of getExchangeRate method, of class KathezIMFExchangeRateProviderSpi.
     */
    @Test
    public void testGetExchangeRate() {
        System.out.println("getExchangeRate");
        CurrencyUnit sourceCurrency = MoneyCurrency.getInstance("SGD");
        CurrencyUnit targetCurrency = MoneyCurrency.getInstance("INR");
        Long timestamp = null;
        KathezIMFExchangeRateProviderSpi instance = provider;
        ExchangeRate expResult = null;
        ExchangeRate result = instance.getExchangeRate(sourceCurrency, targetCurrency, timestamp);
        //assertEquals(expResult, result);
        assertNotNull("Exchange rate returned is null", result);
        System.out.println(result.getFactor());
        
        
    }

    /**
     * Test of getExchangeRateType method, of class KathezIMFExchangeRateProviderSpi.
     */
    @Test
    public void testGetExchangeRateType() {
        System.out.println("getExchangeRateType");
        KathezIMFExchangeRateProviderSpi instance = provider;
        String expResult = "public";
        String result = instance.getExchangeRateType().getId();
        assertEquals(expResult, result);       
    }

}
