package com.kathez.javamoney.converter;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConverter;

import net.java.javamoney.ri.core.MoneyCurrency;

import com.kathez.javamoney.domain.Scrip;

public class ScripConverterImpl implements ScripConverter{

    CurrencyConverter converter = null;
    
    public ScripConverterImpl() {
        converter = new KathezCurrencyConverter();
    }
    
    public MonetaryAmount convert(MonetaryAmount sourceCurrency, String targetCurrencyName) {
        return  converter.convert(sourceCurrency, MoneyCurrency.getInstance(targetCurrencyName));
    }

    public Scrip convertScrip(Scrip sourceScrip, String targetCurrencyName) {        
        MonetaryAmount convertedAmount;
        convertedAmount = convert(sourceScrip.getScripValue(), targetCurrencyName);
        return new Scrip(sourceScrip.getScripId(),convertedAmount);
    }
    
}