package com.redlian.demo.service;

import java.util.List;
import java.util.Map;

public class CoinResponse {
    private Times time;

    public static class Times {
        String updated;
        String updatedISO;
        String updateduk;

        public String getUpdated() {
            return this.updated;
        }

        public void setUpdated(final String updated) {
            this.updated = updated;
        }

        public String getUpdatedISO() {
            return this.updatedISO;
        }

        public void setUpdatedISO(final String updatedISO) {
            this.updatedISO = updatedISO;
        }

        public String getUpdateduk() {
            return this.updateduk;
        }

        public void setUpdateduk(final String updateduk) {
            this.updateduk = updateduk;
        }
    }

    private String disclaimer;

    private String chartName;

    private Map<String, CoinInfo> bpi;

    public static class CoinInfo {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private Object rate_float;

        public String getCode() {
            return this.code;
        }

        public void setCode(final String code) {
            this.code = code;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public void setSymbol(final String symbol) {
            this.symbol = symbol;
        }

        public String getRate() {
            return this.rate;
        }

        public void setRate(final String rate) {
            this.rate = rate;
        }

        public Object getRate_float() {
            return this.rate_float;
        }

        public void setRate_float(final Object rate_float) {
            this.rate_float = rate_float;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

    }

    public Times getTime() {
        return this.time;
    }

    public void setTime(final Times time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return this.disclaimer;
    }

    public void setDisclaimer(final String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return this.chartName;
    }

    public void setChartName(final String chartName) {
        this.chartName = chartName;
    }

    public  Map<String, CoinInfo> getBpi() {
        return this.bpi;
    }

    public void setBpi(final  Map<String, CoinInfo> bpi) {
        this.bpi = bpi;
    }

}
