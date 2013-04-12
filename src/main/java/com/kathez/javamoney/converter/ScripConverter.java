/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kathez.javamoney.converter;

import com.kathez.javamoney.domain.Scrip;
import javax.money.MonetaryAmount;

/**
 *
 * @author Balaji Thennarangam
 */
public interface ScripConverter {
    
    /**
     * Converts the given source currency into the target currency
     * @param sourceCurrency
     * @param targetCurrencyName
     * @return 
     */
    MonetaryAmount convert(MonetaryAmount sourceCurrency, String targetCurrencyName);
    
    /**
     * Converts the source scrip into the target scrip 
     * @param sourceScrip
     * @param targetCurrencyName
     * @return Target scrip with converted values
     */
    Scrip convertScrip(Scrip sourceScrip, String targetCurrencyName);

}
