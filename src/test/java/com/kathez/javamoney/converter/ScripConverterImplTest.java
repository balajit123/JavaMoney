/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kathez.javamoney.converter;


import javax.money.MonetaryAmount;

import net.java.javamoney.ri.core.Money;
import net.java.javamoney.ri.core.MoneyCurrency;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kathez.javamoney.domain.Scrip;
import com.kathez.javamoney.provider.ScripProvider;
import com.kathez.javamoney.provider.ScripsProviderImpl;
import com.kathez.javamoney.provider.YahooDataSource;

/**
 *
 * @author thennarangamb
 */
public class ScripConverterImplTest {
    
    private static final String SOURCE_CURRENCY_NAME = "USD";
    private static final String TARGET_CURRENCY_NAME = "INR";
    ScripProvider scripProvider = null;
    
    public ScripConverterImplTest() { 
        scripProvider = new ScripsProviderImpl(new YahooDataSource("proxy.logica.com"));
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
     * Test of convert method, of class ScripConverterImpl.
     */
    @Test
    public void testConvert() {
        System.out.println("convert");
        MonetaryAmount sourceCurrency = new Money(MoneyCurrency.getInstance(SOURCE_CURRENCY_NAME), 1000);
        String targetCurrencyName = TARGET_CURRENCY_NAME;
        ScripConverterImpl instance = new ScripConverterImpl();
        //MonetaryAmount expResult = null;
        MonetaryAmount result = instance.convert(sourceCurrency, targetCurrencyName);
        //assertEquals(expResult, result);
        System.out.println(result);
        
    }

    /**
     * Test of convertScrip method, of class ScripConverterImpl.
     */
    @Test
    public void testConvertScrip() {
        System.out.println("convertScrip");
        Scrip sourceScrip = scripProvider.getScrip("GOOG");
        String targetCurrencyName = "INR";
        ScripConverterImpl instance = new ScripConverterImpl();
        Scrip expResult = null;
        Scrip result = instance.convertScrip(sourceScrip, targetCurrencyName);
        //assertEquals(expResult, result);
        System.out.println(result.getScripId()+"" +result.getScripValue());
    }
}
