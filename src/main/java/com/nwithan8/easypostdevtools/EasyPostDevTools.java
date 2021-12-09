package com.nwithan8.easypostdevtools;

import com.easypost.EasyPost;
import com.easypost.model.Address;
import com.easypost.model.CustomsInfo;
import com.easypost.model.CustomsItem;
import com.easypost.model.Fee;
import com.easypost.model.Parcel;
import com.easypost.model.PostageLabel;
import com.easypost.model.Shipment;
import com.easypost.model.Tracker;
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

    private final Dotenv dotenv;

    public EasyPostDevTools(String envDir) {
        dotenv = Dotenv.configure().directory(envDir).load();
    }

    public enum KeyType {
        TEST,
        PRODUCTION
    }

    public void setupKey(KeyType type) {
        switch (type) {
            case TEST:
                setupKey(dotenv.get("EASYPOST_TEST_KEY"));
            case PRODUCTION:
                setupKey(dotenv.get("EASYPOST_PROD_KEY"));
        }
    }

    private static JsonObject convertMapToJsonObject(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return new Gson().toJsonTree(map).getAsJsonObject();
    }

    public static void setupKey(String key) {
        EasyPost.apiKey = key;
    }

    // addresses

    public enum ADDRESS_RELATIONSHIP {
        SAME_STATE,
        DIFFERENT_STATE,
        SAME_COUNTRY,
        DIFFERENT_COUNTRY
    }

    public static Map<String, Object> getRandomAddressMap(COUNTRY country) {
        String addressFile = Constants.Addresses.getAddressFile(country);
        List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
        return maps.get(0);
    }

    public static Map<String, Object> getRandomAddressMap(STATE state) {
        String addressFile = Constants.Addresses.getAddressFile(state);
        List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
        return maps.get(0);
    }

    public static Map<String, Object> getRandomAddressMap() {
        String addressFile = Constants.Addresses.getRandomAddressFile();
        List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
        return maps.get(0);
    }

    public static JsonObject getRandomAddressJsonObject() {
        Map<String, Object> map = getRandomAddressMap();
        return convertMapToJsonObject(map);
    }

    public static Address getRandomAddress() {
        try {
            Map<String, Object> map = getRandomAddressMap();
            return Address.create(map);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, Object>> getRandomAddressMapsSameState(int amount) {
        STATE state = STATE.getRandom();
        String stateAddressFile = Constants.Addresses.getAddressFile(state);
        return JSONReader.getRandomMapsFromJsonFile(stateAddressFile, amount, false);
    }

    public static List<JsonObject> getRandomAddressJsonObjectsSameState(int amount) {
        List<Map<String, Object>> maps = getRandomAddressMapsSameState(amount);
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            jsonObjects.add(convertMapToJsonObject(map));
        }
        return jsonObjects;
    }

    public static List<Address> getRandomAddressesSameState(int amount) {
        try {
            List<Map<String, Object>> maps = getRandomAddressMapsSameState(amount);
            List<Address> addresses = new ArrayList<Address>();
            for (Map<String, Object> map : maps) {
                addresses.add(Address.create(map));
            }
            return addresses;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, Object>> getRandomAddressMapsDifferentStates(int amount) {
        if (amount > STATE.getAmount()) {
            throw new IllegalArgumentException("Amount cannot be greater than " + STATE.getAmount());
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        List<Object> states = Random.getRandomItemsFromList(STATE.getAll(), amount, false);
        for (Object state : states) {
            maps.add(getRandomAddressMap((STATE) state));
        }
        return maps;
    }

    public static List<JsonObject> getRandomAddressJsonObjectsDifferentStates(int amount) {
        List<Map<String, Object>> maps = getRandomAddressMapsDifferentStates(amount);
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            jsonObjects.add(convertMapToJsonObject(map));
        }
        return jsonObjects;
    }

    public static List<Address> getRandomAddressesDifferentStates(int amount) {
        try {
            List<Map<String, Object>> maps = getRandomAddressMapsDifferentStates(amount);
            List<Address> addresses = new ArrayList<Address>();
            for (Map<String, Object> map : maps) {
                addresses.add(Address.create(map));
            }
            return addresses;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, Object>> getRandomAddressMapsSameCountry(int amount) {
        COUNTRY country = COUNTRY.getRandom();
        if (country == COUNTRY.UNITED_STATES) {
            return getRandomAddressMapsDifferentStates(amount);
        }
        String countryAddressFile = Constants.Addresses.getAddressFile(country);
        return JSONReader.getRandomMapsFromJsonFile(countryAddressFile, amount, false);
    }

    public static List<JsonObject> getRandomAddressJsonObjectsSameCountry(int amount) {
        List<Map<String, Object>> maps = getRandomAddressMapsSameCountry(amount);
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            jsonObjects.add(convertMapToJsonObject(map));
        }
        return jsonObjects;
    }

    public static List<Address> getRandomAddressesSameCountry(int amount) {
        try {
            List<Map<String, Object>> maps = getRandomAddressMapsSameCountry(amount);
            List<Address> addresses = new ArrayList<Address>();
            for (Map<String, Object> map : maps) {
                addresses.add(Address.create(map));
            }
            return addresses;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, Object>> getRandomAddressMapsDifferentCountries(int amount) {
        if (amount > COUNTRY.getAmount()) {
            throw new IllegalArgumentException("Amount cannot be greater than " + COUNTRY.getAmount());
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        List<Object> countries = Random.getRandomItemsFromList(COUNTRY.getAll(), amount, false);
        for (Object country : countries) {
            maps.add(getRandomAddressMap((COUNTRY) country));
        }
        return maps;
    }

    public static List<JsonObject> getRandomAddressJsonObjectsDifferentCountries(int amount) {
        List<Map<String, Object>> maps = getRandomAddressMapsDifferentCountries(amount);
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            jsonObjects.add(convertMapToJsonObject(map));
        }
        return jsonObjects;
    }

    public static List<Address> getRandomAddressesDifferentCountries(int amount) {
        try {
            List<Map<String, Object>> maps = getRandomAddressMapsDifferentCountries(amount);
            List<Address> addresses = new ArrayList<Address>();
            for (Map<String, Object> map : maps) {
                addresses.add(Address.create(map));
            }
            return addresses;
        } catch (Exception e) {
            return null;
        }
    }

    // parcels

    public static Map<String, Object> getRandomParcelMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("weight", Random.getRandomFloatInRange(0, 100));
        map.put("height", Random.getRandomFloatInRange(0, 100));
        map.put("width", Random.getRandomFloatInRange(0, 100));
        map.put("length", Random.getRandomFloatInRange(0, 100));
        return map;
    }

    public static JsonObject getRandomParcelJsonObject() {
        Map<String, Object> map = getRandomParcelMap();
        return convertMapToJsonObject(map);
    }

    public static Parcel getRandomParcel() {
        try {
            Map<String, Object> map = getRandomParcelMap();
            return Parcel.create(map);
        } catch (Exception e) {
            return null;
        }
    }

    // shipments

    public static Map<String, Object> getRandomShipmentMap() {
        try {
            List<Map<String, Object>> addressMaps = getRandomAddressMapsDifferentStates(2);
            Map<String, Object> parcelMap = getRandomParcelMap();

            Map<String, Object> shipmentMap = new HashMap<>();
            shipmentMap.put("to_address", addressMaps.get(0));
            shipmentMap.put("from_address", addressMaps.get(1));
            shipmentMap.put("parcel", parcelMap);
            return shipmentMap;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonObject getRandomShipmentJsonObject() {
        Map<String, Object> map = getRandomShipmentMap();
        return convertMapToJsonObject(map);
    }

    public static Shipment getRandomShipment() {
        try {
            Map<String, Object> map = getRandomShipmentMap();
            return Shipment.create(map);
        } catch (Exception e) {
            return null;
        }
    }

    // tax identifiers

    /*
    public static List<TaxIdentifier> getRandomTaxIdentifiers() {
        try {
            Shipment shipment = getRandomShipment();
            assert shipment != null;
            return shipment.getTaxIdentifiers();
        } catch (Exception e) {
            return null;
        }
    }
     */

    // customs items

    public static List<Map<String, Object>> getRandomCustomsItemMaps(int amount, boolean allowDuplicates) {
        return JSONReader.getRandomMapsFromJsonFile(Constants.CUSTOMS_ITEMS_JSON, amount, allowDuplicates);
    }

    public static JsonObject getRandomCustomsItemJsonObject() {
        List<Map<String, Object>> maps = getRandomCustomsItemMaps(1, true);
        return convertMapToJsonObject(maps.get(0));
    }

    public static List<CustomsItem> getRandomCustomsItems(int amount, boolean allowDuplicates) {
        try {
            List<Map<String, Object>> maps = getRandomCustomsItemMaps(amount, allowDuplicates);
            List<CustomsItem> customsItems = new ArrayList<CustomsItem>();
            for (Map<String, Object> map : maps) {
                customsItems.add(CustomsItem.create(map));
            }
            return customsItems;
        } catch (Exception e) {
            return null;
        }
    }

    // customs info

    public static Map<String, Object> getRandomCustomsInfoMap(int itemsAmount, boolean allowDuplicatesItems) {
        List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(Constants.CUSTOMS_INFO_JSON, 1, true);
        Map<String, Object> map = maps.get(0);
        map.put("customs_items", getRandomCustomsItemMaps(itemsAmount, allowDuplicatesItems));
        return map;
    }

    public static JsonObject getRandomCustomsInfoJsonObject(int itemsAmount, boolean allowDuplicatesItems) {
        Map<String, Object> map = getRandomCustomsInfoMap(itemsAmount, allowDuplicatesItems);
        return convertMapToJsonObject(map);
    }

    public static CustomsInfo getRandomCustomsInfo(int itemsAmount, boolean allowDuplicatesItems) {
        try {
            Map<String, Object> map = getRandomCustomsInfoMap(itemsAmount, allowDuplicatesItems);
            return CustomsInfo.create(map);
        } catch (Exception e) {
            return null;
        }
    }

    // carriers

    public static List<String> getRandomCarriers(int amount) {
        List<String> carriersStrings = new ArrayList<>();
        List<Object> carriers = JSONReader.getRandomItemsFromJsonFile(Constants.CARRIERS_JSON, amount, false);
        for (Object carrier : carriers) {
            carriersStrings.add(carrier.toString());
        }
        return carriersStrings;
    }

    public static String getRandomCarrier() {
        List<String> carriers = getRandomCarriers(1);
        return carriers.get(0);
    }

    // labels

    public static Map<String, Object> getRandomLabelOptions() {
        List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(Constants.LABEL_OPTIONS_JSON, 1, true);
        return maps.get(0);
    }

    public static JsonObject getRandomLabelOptionsJsonObject() {
        Map<String, Object> map = getRandomLabelOptions();
        return convertMapToJsonObject(map);
    }

    // postage labels

    public static PostageLabel getRandomPostageLabel() {
        try {
            return getRandomShipment().getPostageLabel();
        } catch (Exception e) {
            return null;
        }
    }

    // fees

    public static List<Fee> getRandomFees() {
        try {
            return getRandomShipment().getFees();
        } catch (Exception e) {
            return null;
        }
    }

    // insurance

    public static Map<String, Object> getInsuranceMap(float amount) {
        Map<String, Object> insuranceMap = new HashMap<>();
        insuranceMap.put("amount", amount);
        return insuranceMap;
    }

    public static Map<String, Object> getRandomInsuranceMap() {
        return getInsuranceMap(Random.getRandomFloatInRange(1, 100));
    }

    // trackers

    public static Map<String, Object> getRandomTrackerMap() {
        try {
            List<Map<String, Object>> maps = JSONReader.getRandomMapsFromJsonFile(Constants.TRACKERS_JSON, 1, true);
            return maps.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonObject getRandomTrackerJsonObject() {
        Map<String, Object> map = getRandomTrackerMap();
        return convertMapToJsonObject(map);
    }

    public static Tracker getRandomTracker() {
        try {
            Map<String, Object> map = getRandomTrackerMap();
            return Tracker.create(map);
        } catch (Exception e) {
            return null;
        }
    }
}
