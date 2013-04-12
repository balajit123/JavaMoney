/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kathez.javamoney.provider;

import com.kathez.javamoney.domain.Scrip;
import java.util.Map;

/**
 * 
 * @author Balaji Thennarangam
 */
public interface ScripDataSource {
    
    /**
     * Gives the collection of share values for all the scrip
     * @return 
     */
    Map<String,Scrip> getScrips();
    
    /**
     * Gives the share value for the given scrip id
     * @param scripId
     * @return Share value for the given scrip id
     */
    Scrip getScrip(String scripId);   
    
}
