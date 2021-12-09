package com.nwithan8.easypostdevtools;

import com.nwithan8.easypostdevtools.utils.Random;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String CUSTOMS_ITEMS_JSON = "easypostdevtools_java/json/customs_items.json";
    public static final String CUSTOMS_INFO_JSON = "easypostdevtools_java/json/customs_info.json";
    public static final String CARRIERS_JSON = "easypostdevtools_java/json/carriers.json";
    public static final String LABEL_OPTIONS_JSON = "easypostdevtools_java/json/label_options.json";
    public static final String TRACKERS_JSON = "easypostdevtools_java/json/trackers.json";

    public static class Addresses {
        public enum COUNTRY {
            UNITED_STATES("US", "united-states"),
            CANADA("BC", "canada"),
            CHINA("BJ", "china"),
            HONG_KONG("HK", "china"),
            UNITED_KINGDOM("UK", "europe"),
            GERMANY("DE", "europe"),
            SPAIN("ES", "europe"),
            MEXICO("MX", "mexico"),
            AUSTRALIA("VT", "australia");

            private final String abbreviation;
            private final String parentFolder;

            COUNTRY(String abbreviation, String parentFolder) {
                this.abbreviation = abbreviation;
                this.parentFolder = parentFolder;
            }

            public String getJSONPath() {
                return parentFolder + "/" + abbreviation.toLowerCase() + "-addresses.min.json";
            }

            private static COUNTRY[] getValues() {
                return COUNTRY.class.getEnumConstants();
            }

            public static int getAmount() {
                return getValues().length;
            }

            public static COUNTRY getRandom() {
                COUNTRY[] values = getValues();
                return values[Random.getRandomIntInRange(0, values.length - 1)];
            }

            public static List<COUNTRY> getAll() {
                return Arrays.asList(getValues());
            }
        }

        public enum STATE {
            ARIZONA("AZ", "united-states"),
            CALIFORNIA("CA", "united-states"),
            IDAHO("ID", "united-states"),
            KANSAS("KS", "united-states"),
            NEVADA("NV", "united-states"),
            NEW_YORK("NY", "united-states"),
            OREGON("OR", "united-states"),
            TEXAS("TX", "united-states"),
            UTAH("UT", "united-states"),
            WASHINGTON("WA", "united-states");

            private final String abbreviation;
            private final String parentFolder;

            STATE(String abbreviation, String parentFolder) {
                this.abbreviation = abbreviation;
                this.parentFolder = parentFolder;
            }

            public String getJSONPath() {
                return parentFolder + "/" + abbreviation.toLowerCase() + "-addresses.min.json";
            }

            private static STATE[] getValues() {
                return STATE.class.getEnumConstants();
            }

            public static int getAmount() {
                return getValues().length;
            }

            public static STATE getRandom() {
                STATE[] values = getValues();
                return values[Random.getRandomIntInRange(0, values.length - 1)];
            }

            public static List<STATE> getAll() {
                return Arrays.asList(getValues());
            }
        }

        private static String getAddressFile(String path) {
            return "easypostdevtools_java/json/addresses/" + path;
        }

        public static String getAddressFile(STATE state) {
            return getAddressFile(state.getJSONPath());
        }

        public static String getAddressFile(COUNTRY country) {
            if (country == COUNTRY.UNITED_STATES) {
                return getRandomStateAddressFile();
            }

            return getAddressFile(country.getJSONPath());

        }

        public static String getRandomStateAddressFile() {
            STATE state = STATE.getRandom();
            return getAddressFile(state);
        }

        public static String getRandomCountryAddressFile() {
            COUNTRY country = COUNTRY.getRandom();
            return getAddressFile(country);
        }

        public static String getRandomAddressFile() {
            if (Random.getRandomTrueFalse()) {
                return getRandomCountryAddressFile();
            } else {
                return getRandomStateAddressFile();
            }
        }
    }
}
