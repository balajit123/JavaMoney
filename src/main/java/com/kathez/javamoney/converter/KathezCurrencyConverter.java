/**
 * 
 */
package com.kathez.javamoney.converter;

import javax.money.convert.ExchangeRateType;

import net.java.javamoney.ri.convert.provider.DefaultCurrencyConverter;

/**
 * @author thennarangamb
 *
 */
public class KathezCurrencyConverter extends DefaultCurrencyConverter {
	
	/**
	 * 
	 */
	public KathezCurrencyConverter() {		
		this(new ExchangeRateType() {
			
			@Override
			public String getId() {
				return "public";
			}
		});
	}

	/**
	 * @param exchangeRateType
	 */
	public KathezCurrencyConverter(ExchangeRateType exchangeRateType) {
		super();
		this.exchangeRateType = exchangeRateType;
	}

	private ExchangeRateType exchangeRateType;

	/* (non-Javadoc)
	 * @see javax.money.convert.CurrencyConverter#getExchangeRateType()
	 */
	@Override
	public ExchangeRateType getExchangeRateType() {
		return exchangeRateType;
	}
}
