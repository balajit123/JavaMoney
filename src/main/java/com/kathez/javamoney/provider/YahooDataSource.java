package com.kathez.javamoney.provider;

import com.kathez.javamoney.domain.Scrip;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.money.MonetaryAmount;
import net.java.javamoney.ri.core.Money;
import net.java.javamoney.ri.core.MoneyCurrency;

public class YahooDataSource implements ScripDataSource{

    private static final Map<String,Scrip> CACHED = new HashMap<String,Scrip>();
    private URL feedUrl = null;  
    
    @Inject
    public YahooDataSource(String httpProxyName) {
        InputStream inStream = null;
        try {
            feedUrl = new URL("http://finance.yahoo.com/d/quotes.csv?s=DELL+MSFT+OCLCF+GOOG+YHOO&f=snd1l1yr");            
            URLConnection urlConnection = null;
            if(httpProxyName != null && !httpProxyName.trim().equals("")){
                urlConnection = feedUrl.openConnection(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(httpProxyName, 80)));
            }else{
                urlConnection = feedUrl.openConnection();
            }            
            inStream = urlConnection.getInputStream();
            loadScripData(inStream);
        } catch (MalformedURLException ex) {
            Logger.getLogger(YahooDataSource.class.getName()).log(Level.SEVERE, "Feed URL is malformed", ex);
        } catch (IOException ex) {
            Logger.getLogger(YahooDataSource.class.getName()).log(Level.SEVERE, "Could not open the Feed URL Stream", ex);
        } finally {
            try {
                if(inStream != null){
                    inStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(YahooDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Map<String, Scrip> getScrips() {
        Map<String,Scrip> scrips = new HashMap<String, Scrip>();
        scrips.putAll(CACHED);
        return scrips;
    }

    public Scrip getScrip(String scripId) {
        return getScrips().get(scripId);
    }

    private void loadScripData(InputStream inStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String line = null;
        String [] parts;
        try {
            line = reader.readLine();
            while( line != null){
                //System.out.println(line);
                parts = line.split(",");                
                CACHED.put(parts[0].replace("\"", "").trim(), new Scrip(parts[0].replace("\"", "").trim(),new Money(MoneyCurrency.getInstance("USD"), Double.valueOf(parts[3]))));
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(YahooDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}