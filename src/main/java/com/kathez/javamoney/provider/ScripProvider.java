/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kathez.javamoney.provider;

import com.kathez.javamoney.domain.Scrip;
import java.util.Map;
import javax.money.MonetaryAmount;

/**
 *
 * @author Balaji Thennarangam
 */
public interface ScripProvider {
    
    /**
     * Provides the currency from a currency data source like flat file,
     * RSS feed etc
     * @return Map of the share values for all the scrip
     */
    Map<String,Scrip> getScrips();
    
    /**
     * Gives the Share value for the given scrip id
     * @param sripId
     * @return Share value of the scrip
     */
    Scrip getScrip(String sripId);
}
