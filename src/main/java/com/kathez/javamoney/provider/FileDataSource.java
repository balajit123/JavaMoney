package com.kathez.javamoney.provider;

import com.kathez.javamoney.domain.Scrip;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import net.java.javamoney.ri.core.Money;
import net.java.javamoney.ri.core.MoneyCurrency;

public class FileDataSource implements ScripDataSource{
        
    private static final Properties CURRENCY_DATA = new Properties();
    private static final Map<String,Scrip> CACHED = new HashMap<String,Scrip>();

    public FileDataSource() {
        populateScrips();
    }
        
    public Map<String,Scrip> getScrips() {
        Map<String,Scrip> scripMap = new HashMap<String,Scrip>();
        scripMap.putAll(CACHED);
        return scripMap;        
    }
    
    public Scrip getScrip(String scripId) {
        return getScrips().get(scripId);
    }

    private void populateScrips() {
        try {
                //ResourceBundle bundle = ResourceBundle.getBundle("currencydata.properties");
                CURRENCY_DATA.load(FileDataSource.class.getResourceAsStream("/META-INF/config/currencydata.properties"));
                
                CurrencyUnit currencyUnit = MoneyCurrency.getInstance(CURRENCY_DATA.getProperty("CURRENCYUNIT"));
                for(Object key : CURRENCY_DATA.keySet()){
                    try{
                        CACHED.put((String)key,new Scrip((String)key, new Money(currencyUnit,Double.valueOf(CURRENCY_DATA.getProperty((String)key)))) );
                    }catch(NumberFormatException nfe){
                        continue;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(FileDataSource.class.getName()).log(Level.SEVERE, "Error while accessing currencydata.properties file", ex);
            }
    }
}