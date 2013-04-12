/**
 * 
 */
package com.kathez.javamoney.converter;

import java.util.ServiceLoader;
import javax.money.CurrencyUnit;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.ExchangeRateType;
import javax.money.convert.spi.ExchangeRateProviderSpi;
import net.java.javamoney.ri.convert.SingletonExchangeRateType;

/**
 * @author thennarangamb
 *
 */
public class ScripExchangeRateProvider implements ExchangeRateProvider {
	
	private final ExchangeRateProviderSpi exchangeRateProviderSpi;
	
	@Override
	public ExchangeRateType getExchangeRateType() {
		return SingletonExchangeRateType.of("public");
	}
	
	/**
	 * 
	 */
	public ScripExchangeRateProvider() {
		super();
		exchangeRateProviderSpi = loadService(ExchangeRateProviderSpi.class);
	}

	private <T> T loadService(
			Class<T> serviceClass) {
		ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
		T instance = null;
		for(T t  : loader){
			if(instance == null){
				instance = t;
			}
		}
		return instance;
	}

	@Override
	public boolean isAvailable(CurrencyUnit src, CurrencyUnit target) {
		return exchangeRateProviderSpi.getExchangeRate(src, target, null) != null;
	}

	@Override
	public boolean isAvailable(CurrencyUnit CurrencyUnit, CurrencyUnit target,
			Long timestamp) {
		return exchangeRateProviderSpi.getExchangeRate(CurrencyUnit, target, timestamp) != null;
	}

	@Override
	public ExchangeRate get(CurrencyUnit sourceCurrency,
			CurrencyUnit targetCurrency, Long timestamp) {
		return exchangeRateProviderSpi.getExchangeRate(sourceCurrency,targetCurrency,timestamp);
	}

	

	@Override
	public ExchangeRate get(CurrencyUnit source, CurrencyUnit target) {
		return exchangeRateProviderSpi.getExchangeRate(source, target, null);
	}

	@Override
	public boolean isLinear(CurrencyUnit sourceCurrency,
			CurrencyUnit targetCurrency) {
		return true;
	}

	@Override
	public boolean isLinear(CurrencyUnit sourceCurrency,
			CurrencyUnit targetCurrency, Long timestamp) {
		return true;
	}

	@Override
	public boolean isIdentity(CurrencyUnit sourceCurrency,
			CurrencyUnit targetCurrency) {
		return true;
	}

	@Override
	public boolean isIdentity(CurrencyUnit sourceCurrency,
			CurrencyUnit targetCurrency, Long timestamp) {
		return true;
	}

}
