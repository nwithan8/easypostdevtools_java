package com.nwithan8.easypostdevtools;

import com.easypost.EasyPost;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;

import com.nwithan8.easypostdevtools.Constants.Addresses.COUNTRY;
import com.nwithan8.easypostdevtools.Constants.Addresses.STATE;
import com.nwithan8.easypostdevtools.utils.Random;
import com.nwithan8.easypostdevtools.utils.JSONReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyPostDevTools {

    public enum KeyType {
        TEST,
        PRODUCTION
    }

    public static void setupKey(String key) {
        EasyPost.apiKey = key;
    }

    public static void setupKey(String envDir, KeyType type) {
        Dotenv dotenv = Dotenv.configure().directory(envDir).load();
        switch (type) {
            case TEST:
                setupKey(dotenv.get("EASYPOST_TEST_KEY"));
            case PRODUCTION:
                setupKey(dotenv.get("EASYPOST_PROD_KEY"));
        }
    }

    public static class Address {

        public enum ADDRESS_RELATIONSHIP {
            SAME_STATE,
            DIFFERENT_STATE,
            SAME_COUNTRY,
            DIFFERENT_COUNTRY
        }

        public static Map<String, Object> getMap() {
            String addressFile = Constants.Addresses.getRandomAddressFile();
            List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
            return maps.get(0);
        }

        public static Map<String, Object> getMap(COUNTRY country) {
            String addressFile = Constants.Addresses.getAddressFile(country);
            List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
            return maps.get(0);
        }

        public static Map<String, Object> getMap(STATE state) {
            String addressFile = Constants.Addresses.getAddressFile(state);
            List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
            return maps.get(0);
        }

        public static JsonObject getJsonObject() {
            Map<String, Object> map = getMap();
            return JSONReader.convertMapToJsonObject(map);
        }

        public static JsonObject getJsonObject(COUNTRY country) {
            Map<String, Object> map = getMap(country);
            return JSONReader.convertMapToJsonObject(map);
        }

        public static JsonObject getJsonObject(STATE state) {
            Map<String, Object> map = getMap(state);
            return JSONReader.convertMapToJsonObject(map);
        }

        public static com.easypost.model.Address get() {
            try {
                Map<String, Object> map = getMap();
                return com.easypost.model.Address.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static com.easypost.model.Address get(COUNTRY country) {
            try {
                Map<String, Object> map = getMap(country);
                return com.easypost.model.Address.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static com.easypost.model.Address get(STATE state) {
            try {
                Map<String, Object> map = getMap(state);
                return com.easypost.model.Address.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsSameState(int amount) {
            STATE state = STATE.getRandom();
            String stateAddressFile = Constants.Addresses.getAddressFile(state);
            return JSONReader.getRandomMapsFromJsonFile(stateAddressFile, amount, false);
        }

        public static List<JsonObject> getJsonObjectsSameState(int amount) {
            List<Map<String, Object>> maps = getMapsSameState(amount);
            List<JsonObject> jsonObjects = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                jsonObjects.add(JSONReader.convertMapToJsonObject(map));
            }
            return jsonObjects;
        }

        public static List<com.easypost.model.Address> getSameState(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsSameState(amount);
                List<com.easypost.model.Address> addresses = new ArrayList<com.easypost.model.Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(com.easypost.model.Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsDifferentStates(int amount) {
            if (amount > STATE.getAmount()) {
                throw new IllegalArgumentException("Amount cannot be greater than " + STATE.getAmount());
            }

            List<Map<String, Object>> maps = new ArrayList<>();
            List<Object> states = Random.getRandomItemsFromList(STATE.getAll(), amount, false);
            for (Object state : states) {
                maps.add(getMap((STATE) state));
            }
            return maps;
        }

        public static List<JsonObject> getJsonObjectsDifferentStates(int amount) {
            List<Map<String, Object>> maps = getMapsDifferentStates(amount);
            List<JsonObject> jsonObjects = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                jsonObjects.add(JSONReader.convertMapToJsonObject(map));
            }
            return jsonObjects;
        }

        public static List<com.easypost.model.Address> getDifferentStates(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsDifferentStates(amount);
                List<com.easypost.model.Address> addresses = new ArrayList<com.easypost.model.Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(com.easypost.model.Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsSameCountry(int amount) {
            COUNTRY country = COUNTRY.getRandom();
            if (country == COUNTRY.UNITED_STATES) {
                return getMapsDifferentStates(amount);
            }
            String countryAddressFile = Constants.Addresses.getAddressFile(country);
            return JSONReader.getRandomMapsFromJsonFile(countryAddressFile, amount, false);
        }

        public static List<JsonObject> getJsonObjectsSameCountry(int amount) {
            List<Map<String, Object>> maps = getMapsSameCountry(amount);
            List<JsonObject> jsonObjects = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                jsonObjects.add(JSONReader.convertMapToJsonObject(map));
            }
            return jsonObjects;
        }

        public static List<com.easypost.model.Address> getSameCountry(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsSameCountry(amount);
                List<com.easypost.model.Address> addresses = new ArrayList<com.easypost.model.Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(com.easypost.model.Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsDifferentCountries(int amount) {
            if (amount > COUNTRY.getAmount()) {
                throw new IllegalArgumentException("Amount cannot be greater than " + COUNTRY.getAmount());
            }

            List<Map<String, Object>> maps = new ArrayList<>();
            List<Object> countries = Random.getRandomItemsFromList(COUNTRY.getAll(), amount, false);
            for (Object country : countries) {
                maps.add(getMap((COUNTRY) country));
            }
            return maps;
        }

        public static List<JsonObject> getJsonObjectsDifferentCountries(int amount) {
            List<Map<String, Object>> maps = getMapsDifferentCountries(amount);
            List<JsonObject> jsonObjects = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                jsonObjects.add(JSONReader.convertMapToJsonObject(map));
            }
            return jsonObjects;
        }

        public static List<com.easypost.model.Address> getDifferentCountries(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsDifferentCountries(amount);
                List<com.easypost.model.Address> addresses = new ArrayList<com.easypost.model.Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(com.easypost.model.Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Parcel {

        public static Map<String, Object> getMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("weight", Random.getRandomFloatInRange(0, 100));
            map.put("height", Random.getRandomFloatInRange(0, 100));
            map.put("width", Random.getRandomFloatInRange(0, 100));
            map.put("length", Random.getRandomFloatInRange(0, 100));
            return map;
        }

        public static JsonObject getJsonObject() {
            Map<String, Object> map = getMap();
            return JSONReader.convertMapToJsonObject(map);
        }

        public static com.easypost.model.Parcel get() {
            try {
                Map<String, Object> map = getMap();
                return com.easypost.model.Parcel.create(map);
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class Shipment {

        public static Map<String, Object> getMap() {
            try {
                List<Map<String, Object>> addressMaps = Address.getMapsDifferentStates(2);
                Map<String, Object> parcelMap = Parcel.getMap();

                Map<String, Object> shipmentMap = new HashMap<>();
                shipmentMap.put("to_address", addressMaps.get(0));
                shipmentMap.put("from_address", addressMaps.get(1));
                shipmentMap.put("parcel", parcelMap);
                return shipmentMap;
            } catch (Exception e) {
                return null;
            }
        }

        public static JsonObject getJsonObject() {
            Map<String, Object> map = getMap();
            return JSONReader.convertMapToJsonObject(map);
        }

        public static com.easypost.model.Shipment get() {
            try {
                Map<String, Object> map = getMap();
                return com.easypost.model.Shipment.create(map);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class TaxIdentifier {

        /*
        public static List<TaxIdentifier> getRandomTaxIdentifiers() {
            try {
                com.easypost.model.Shipment shipment = Shipment.get();
                assert shipment != null;
                return shipment.getTaxIdentifiers();
            } catch (Exception e) {
                return null;
            }
        }
         */
    }

    public static class CustomsItem {

        public static List<Map<String, Object>> getRandomCustomsItemMaps(int amount, boolean allowDuplicates) {
            return JSONReader.getRandomMapsFromJsonFile(Constants.CUSTOMS_ITEMS_JSON, amount, allowDuplicates);
        }

        public static JsonObject getRandomCustomsItemJsonObject() {
            List<Map<String, Object>> maps = getRandomCustomsItemMaps(1, true);
            return JSONReader.convertMapToJsonObject(maps.get(0));
        }

        public static List<com.easypost.model.CustomsItem> get(int amount, boolean allowDuplicates) {
            try {
                List<Map<String, Object>> maps = getRandomCustomsItemMaps(amount, allowDuplicates);
                List<com.easypost.model.CustomsItem> customsItems = new ArrayList<com.easypost.model.CustomsItem>();
                for (Map<String, Object> map : maps) {
                    customsItems.add(com.easypost.model.CustomsItem.create(map));
                }
                return customsItems;
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class CustomsInfo {

        public static Map<String, Object> getMap(int itemsAmount, boolean allowDuplicatesItems) {
            List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(Constants.CUSTOMS_INFO_JSON, 1, true);
            Map<String, Object> map = maps.get(0);
            map.put("customs_items", CustomsItem.getRandomCustomsItemMaps(itemsAmount, allowDuplicatesItems));
            return map;
        }

        public static JsonObject getJsonObject(int itemsAmount, boolean allowDuplicatesItems) {
            Map<String, Object> map = getMap(itemsAmount, allowDuplicatesItems);
            return JSONReader.convertMapToJsonObject(map);
        }

        public static com.easypost.model.CustomsInfo get(int itemsAmount, boolean allowDuplicatesItems) {
            try {
                Map<String, Object> map = getMap(itemsAmount, allowDuplicatesItems);
                return com.easypost.model.CustomsInfo.create(map);
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class Carrier {

        public static List<String> get(int amount) {
            List<String> carriersStrings = new ArrayList<>();
            List<Object> carriers = JSONReader.getRandomItemsFromJsonFile(Constants.CARRIERS_JSON, amount, false);
            for (Object carrier : carriers) {
                carriersStrings.add(carrier.toString());
            }
            return carriersStrings;
        }

        public static String get() {
            List<String> carriers = get(1);
            return carriers.get(0);
        }

    }

    public static class Label {

        public static Map<String, Object> getRandomLabelOptions() {
            List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(Constants.LABEL_OPTIONS_JSON, 1, true);
            return maps.get(0);
        }

        public static JsonObject getRandomLabelOptionsJsonObject() {
            Map<String, Object> map = getRandomLabelOptions();
            return JSONReader.convertMapToJsonObject(map);
        }

    }

    public static class PostageLabel {

        public static com.easypost.model.PostageLabel get() {
            try {
                return Shipment.get().getPostageLabel();
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class Fee {
        public static List<com.easypost.model.Fee> get() {
            try {
                return Shipment.get().getFees();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Insurance {

        public static Map<String, Object> getInsuranceMap(float amount) {
            Map<String, Object> insuranceMap = new HashMap<>();
            insuranceMap.put("amount", amount);
            return insuranceMap;
        }

        public static Map<String, Object> getMap() {
            return getInsuranceMap(Random.getRandomFloatInRange(1, 100));
        }

    }

    public static class Tracker {

        public static Map<String, Object> getMap() {
            try {
                List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(Constants.TRACKERS_JSON, 1, true);
                return maps.get(0);
            } catch (Exception e) {
                return null;
            }
        }

        public static JsonObject getJsonObject() {
            Map<String, Object> map = getMap();
            return JSONReader.convertMapToJsonObject(map);
        }

        public static com.easypost.model.Tracker get() {
            try {
                Map<String, Object> map = getMap();
                return com.easypost.model.Tracker.create(map);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
