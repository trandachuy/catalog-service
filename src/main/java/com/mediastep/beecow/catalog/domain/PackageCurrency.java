package com.mediastep.beecow.catalog.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A PackageCurrency.
 */
@Entity
@Table(name = "package_currency")
public class PackageCurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "exchange_rate_vn")
    private BigDecimal exchangeRateVN;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public PackageCurrency currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public PackageCurrency symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getExchangeRateVN() {
        return exchangeRateVN;
    }

    public PackageCurrency exchangeRateVN(BigDecimal exchangeRateVN) {
        this.exchangeRateVN = exchangeRateVN;
        return this;
    }

    public void setExchangeRateVN(BigDecimal exchangeRateVN) {
        this.exchangeRateVN = exchangeRateVN;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageCurrency)) {
            return false;
        }
        return id != null && id.equals(((PackageCurrency) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PackageCurrency{" +
            "id=" + getId() +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", exchangeRateVN=" + getExchangeRateVN() +
            "}";
    }
}
