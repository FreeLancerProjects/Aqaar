package com.creative.share.apps.aqaar.models;

import java.io.Serializable;
import java.util.List;

public class BankDataModel implements Serializable {

    private List<BankModel> data;

    public List<BankModel> getData() {
        return data;
    }

    public class BankModel implements Serializable
    {
          private int account_id;
                  private String account_name;
        private String account_IBAN;
        private String account_bank_name;
        private String account_number;

        public int getAccount_id() {
            return account_id;
        }

        public String getAccount_name() {
            return account_name;
        }

        public String getAccount_IBAN() {
            return account_IBAN;
        }

        public String getAccount_bank_name() {
            return account_bank_name;
        }

        public String getAccount_number() {
            return account_number;
        }
    }
}
