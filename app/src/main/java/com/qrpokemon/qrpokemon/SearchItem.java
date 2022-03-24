package com.qrpokemon.qrpokemon;

public class SearchItem {

        private String identifier;
        private String email;
        private String phone;


    public SearchItem(String identifier, String email, String phone) {
            this.identifier = identifier;
            this.email = email;
            this.phone = phone;
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


}
