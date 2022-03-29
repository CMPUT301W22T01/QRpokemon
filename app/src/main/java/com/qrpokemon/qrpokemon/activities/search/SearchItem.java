package com.qrpokemon.qrpokemon.activities.search;

import java.util.ArrayList;

public class SearchItem {

        private String identifier;
        private String email;
        private String phone;
        private ArrayList<String> qrList;


    public SearchItem(String identifier, String email, String phone, ArrayList<String> qrList) {
            this.identifier = identifier;
            this.email = email;
            this.phone = phone;
            this.qrList = qrList;
        }


        public String getIdentifier() {
            return identifier;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public ArrayList<String> getQrList() {
        return qrList;
    }

        public void setQrList(ArrayList<String> qrList) {
            this.qrList = qrList;
    }


}
