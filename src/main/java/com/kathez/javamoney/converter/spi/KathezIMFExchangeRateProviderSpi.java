package com.kathez.javamoney.converter.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateType;
import javax.money.convert.spi.ExchangeRateProviderSpi;
import net.java.javamoney.ri.convert.SingletonExchangeRateType;
import net.java.javamoney.ri.core.MoneyCurrency;

public final class KathezIMFExchangeRateProviderSpi implements ExchangeRateProviderSpi {

        private static final Logger LOGGER = Logger.getLogger("IMFExchangeRateProvider.class");
        private static final String FEED_URL = "http://www.imf.org/external/np/fin/data/rms_five.aspx?tsvflag=Y";
	private Map<CurrencyUnit, ExchangeRate> currencyToSDRMap = new HashMap<CurrencyUnit, ExchangeRate>();
	private Map<CurrencyUnit, ExchangeRate> sDRToCurrency = new HashMap<CurrencyUnit, ExchangeRate>();
	private static final Properties CURRENCY_CODES = new Properties();
	private static final Properties CURRENCY_NAMES = new Properties();

        static{
		try {
			CURRENCY_CODES.load(KathezIMFExchangeRateProviderSpi.class.getResourceAsStream("/META-INF/rates/currencycodes.properties"));
			for(Object currencCode : CURRENCY_CODES.keySet()){
				CURRENCY_NAMES.put(((String)CURRENCY_CODES.get(((String)currencCode))).trim().toLowerCase(), ((String)currencCode).trim().toLowerCase());
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,"Error", e);
			e.printStackTrace();
		}
	}
        
	/**
	 * 
	 */
        @Inject
	public KathezIMFExchangeRateProviderSpi(String proxyHostName) {
		super();
		loadRates(proxyHostName);
	}
     
        private void loadRates(String proxyHostName) {
		InputStream is = null;
		try {			
			URL url = new URL(FEED_URL);
			URLConnection connection = url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostName, 80)));
                        is = connection.getInputStream();
			loadRates(is);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,"Error", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING,"Error closing input stream.", e);
				}
			}
		}
	}

	private void loadRates(InputStream inputStream) throws IOException {
		CurrencyUnit sdr = getCurrency("XDR");
		NumberFormat f = new DecimalFormat("#0.0000000000");
		f.setGroupingUsed(false);
		BufferedReader pr = new BufferedReader(new InputStreamReader(
				inputStream));
		String line = pr.readLine();
		int sdrToCurrency = 0;
		boolean valueEncountered = false;
		Long[] timestamps = null;
		while (line != null) {
			valueEncountered = false;						
			if (line.startsWith("SDRs per Currency unit")) {
				sdrToCurrency = 1;
			} else if (line.startsWith("Currency units per SDR")) {
				sdrToCurrency = 2;
			} else if (line.startsWith("Currency")) {
				timestamps = readTimestamps(line);
			} else if (line.startsWith("(1) Exchange rates are published daily except on IMF holidays")) {
				scanCompleted = true;
			} else{
				if(!lineToSkip(line,sdrToCurrency)){
					valueEncountered = true;
				}				
			}			
			if(valueEncountered){
				String[] parts = line.split("\\t");				
				CurrencyUnit currency = getCurrency(getCurrencyCode(parts[0]));
				Double[] values = parseValues(f, parts, 1);
				 for(int i=0;i<values.length;i++){
				 if(values[i]!=null){
				 if(sdrToCurrency==1){ // Currency -> SDR
				 currencyToSDRMap.put(currency, new ExchangeRateImpl(currency,
				 sdr, values[i],timestamps[i]));
				 }
				 else if(sdrToCurrency==2){ // SDR -> Currency
				 sDRToCurrency.put(currency, new ExchangeRateImpl(sdr, currency,
				 values[i],timestamps[i]));
				 }
				 }
				 }
			}
			
			// SDRs per Currency unit (2)
			//
			// Currency January 31, 2013 January 30, 2013 January 29, 2013
			// January 28, 2013 January 25, 2013
			// Euro 0.8791080000 0.8789170000 0.8742470000 0.8752180000
			// 0.8768020000

			// Currency units per SDR(3)
			//
			// Currency January 31, 2013 January 30, 2013 January 29, 2013
			// January 28, 2013 January 25, 2013
			// Euro 1.137520 1.137760 1.143840 1.142570 1.140510

			line = pr.readLine();
		}

	}

	private String getCurrencyCode(String currencyName) {
		return CURRENCY_NAMES.getProperty(currencyName.trim().toLowerCase()).trim().toUpperCase();
	}	
	
	private static boolean scanCompleted = false;
	private boolean lineToSkip(String line, int sdrToCurrency) {
		if(line.startsWith("last five days"))
			return true;
		if(line.trim().equals(""))
			return true;
		if(scanCompleted)
			return true;
		
		return false;
	}

	private Double[] parseValues(NumberFormat f, String[] parts, int i) {
		Double[] values = new Double[5];
		int j = 0;
		for(String value : parts){
			try{
				values[j] = (Double.parseDouble(value));
			}catch(NumberFormatException e){
				if(value.trim().equals("")){
					values[j] = null;
					j++;
				}
				continue;
			}
			j++;
		}		
		return values;
	}

	private Long[] readTimestamps(String line) {
		String[] parts = line.split("\\t");
		Long[] timeStamps = new Long[5];
		DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
		int i = 0;
		for(String part : parts){
			if(part.trim().startsWith("Currency")){
				continue;
			}
			try {
				timeStamps[i] = dateFormat.parse(part).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		return timeStamps;
	}

	private CurrencyUnit getCurrency(String isoCurrency) {
		return MoneyCurrency.getInstance(isoCurrency);		
	}

	@Override
	public ExchangeRate getExchangeRate(CurrencyUnit sourceCurrency,
			CurrencyUnit targetCurrency, Long timestamp) {
		ExchangeRate exchangeRate = null;
		ExchangeRate sourceToSDRRate = getSourceToSDRExchangeRate(sourceCurrency,timestamp);
		System.out.println();
		ExchangeRate sdrToTargetRate = getSDRToTargetExchangeRate(targetCurrency, timestamp);
		exchangeRate = new ExchangeRateImpl(sourceCurrency, targetCurrency, getTargetRateFactor(sourceToSDRRate,sdrToTargetRate),timestamp);
		return exchangeRate;
	}
	
	private ExchangeRate getSDRToTargetExchangeRate(CurrencyUnit targetCurrency, Long timestamp) {
		return sDRToCurrency.get(targetCurrency);
	}

	private ExchangeRate getSourceToSDRExchangeRate(
			CurrencyUnit sourceCurrency, Long timestamp) {
		return currencyToSDRMap.get(sourceCurrency);
	}

	private Double getTargetRateFactor(ExchangeRate sourceToSDRRate,
			ExchangeRate sdrToTargetRate) {
		return sourceToSDRRate.getFactor().doubleValue() *  sdrToTargetRate.getFactor().doubleValue();
	}

	@Override
	public ExchangeRateType getExchangeRateType() {
		return SingletonExchangeRateType.of("public");
	}
        
        final class ExchangeRateImpl implements ExchangeRate {
		
		private CurrencyUnit source,target;
		private Number factor;
		private Long timestamp;

		public ExchangeRateImpl(CurrencyUnit source, CurrencyUnit target,
				Double factor) {
			this.source = source;
			this.target = target;
			this.factor = factor;
			
		}

		public ExchangeRateImpl(CurrencyUnit source, CurrencyUnit target,
				Double factor, Long timestamp) {
			this(source,target,factor);
			this.timestamp = timestamp;
		}

		@Override
		public ExchangeRateType getExchangeRateType() {
			return SingletonExchangeRateType.of("public");
		}

		@Override
		public CurrencyUnit getSource() {
			return this.source;
		}

		@Override
		public CurrencyUnit getTarget() {
			return this.target;
		}

		@Override
		public Number getFactor() {
			return this.factor;
		}

		@Override
		public Long getTimestamp() {
			return this.timestamp;
		}

		@Override
		public Long getValidUntil() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isValid() {
			return this.source != null && this.target != null && this.factor != null && this.timestamp != null;
		}

		@Override
		public String getLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getDataProvider() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ExchangeRate[] getExchangeRateChain() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isDerived() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <T> T getAttribute(String key, Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<String> getAttributeKeys() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<?> getAttributeType(String key) {
			// TODO Auto-generated method stub
			return null;
		}

	}	
}
