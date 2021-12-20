package com.nwithan8.easypostdevtools;

import com.easypost.EasyPost;
import com.easypost.model.Address;
import com.easypost.model.CustomsInfo;
import com.easypost.model.CustomsItem;
import com.easypost.model.Fee;
import com.easypost.model.Parcel;
import com.easypost.model.PostageLabel;
import com.easypost.model.Rate;
import com.easypost.model.Report;
import com.easypost.model.Shipment;
import com.easypost.model.Tracker;
import com.easypost.model.Webhook;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nwithan8.easypostdevtools.utils.Dates;
import io.github.cdimascio.dotenv.Dotenv;

import com.nwithan8.easypostdevtools.Constants.Addresses.COUNTRY;
import com.nwithan8.easypostdevtools.Constants.Addresses.STATE;
import com.nwithan8.easypostdevtools.utils.Random;
import com.nwithan8.easypostdevtools.utils.JSONReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyPostDevTools {

    public enum KeyType {
        TEST, PRODUCTION
    }

    public static void setupKey(String key) {
        EasyPost.apiKey = key;
    }

    public static void setupKey(String envDir, KeyType type) {
        Dotenv dotenv = Dotenv.configure().directory(envDir).load();
        switch (type) {
            case TEST:
                setupKey(dotenv.get("EASYPOST_TEST_KEY"));
                break;
            case PRODUCTION:
                setupKey(dotenv.get("EASYPOST_PROD_KEY"));
                break;
        }
    }

    private static class Mapper {

        public static JsonObject toJson(Object object) {
            Gson gson = new Gson();
            return gson.toJsonTree(object).getAsJsonObject();
        }

        public static Map<String, Object> toMap(Object object) {
            Gson gson = new Gson();
            return (Map<String, Object>) gson.fromJson(gson.toJson(object),
                    Map.class);
        }
    }

    public static class Addresses extends Mapper {

        public enum ADDRESS_RELATIONSHIP {
            SAME_STATE, DIFFERENT_STATE, SAME_COUNTRY, DIFFERENT_COUNTRY
        }

        public static Map<String, Object> getMap() {
            String addressFile = Constants.Addresses.getRandomAddressFile();
            List<Map<String, Object>> maps =
                    JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
            return maps.get(0);
        }

        public static Map<String, Object> getMap(COUNTRY country) {
            String addressFile = Constants.Addresses.getAddressFile(country);
            List<Map<String, Object>> maps =
                    JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
            return maps.get(0);
        }

        public static Map<String, Object> getMap(STATE state) {
            String addressFile = Constants.Addresses.getAddressFile(state);
            List<Map<String, Object>> maps =
                    JSONReader.getRandomMapsFromJsonFile(addressFile, 1, true);
            return maps.get(0);
        }

        public static Address get() {
            try {
                Map<String, Object> map = getMap();
                return Address.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static Address get(COUNTRY country) {
            try {
                Map<String, Object> map = getMap(country);
                return Address.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static Address get(STATE state) {
            try {
                Map<String, Object> map = getMap(state);
                return Address.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsSameState(int amount) {
            STATE state = STATE.getRandom();
            String stateAddressFile = Constants.Addresses.getAddressFile(state);
            return JSONReader.getRandomMapsFromJsonFile(stateAddressFile,
                    amount, false);
        }

        public static List<Address> getSameState(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsSameState(amount);
                List<Address> addresses = new ArrayList<Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsDifferentStates(
                int amount) {
            if (amount > STATE.getAmount()) {
                throw new IllegalArgumentException(
                        "Amount cannot be greater than " + STATE.getAmount());
            }

            List<Map<String, Object>> maps = new ArrayList<>();
            List<Object> states =
                    Random.getRandomItemsFromList(STATE.getAll(), amount,
                            false);
            for (Object state : states) {
                maps.add(getMap((STATE) state));
            }
            return maps;
        }

        public static List<Address> getDifferentStates(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsDifferentStates(amount);
                List<Address> addresses = new ArrayList<Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(Address.create(map));
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
            String countryAddressFile =
                    Constants.Addresses.getAddressFile(country);
            return JSONReader.getRandomMapsFromJsonFile(countryAddressFile,
                    amount, false);
        }

        public static List<Address> getSameCountry(int amount) {
            try {
                List<Map<String, Object>> maps = getMapsSameCountry(amount);
                List<Address> addresses = new ArrayList<Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMapsDifferentCountries(
                int amount) {
            if (amount > COUNTRY.getAmount()) {
                throw new IllegalArgumentException(
                        "Amount cannot be greater than " + COUNTRY.getAmount());
            }

            List<Map<String, Object>> maps = new ArrayList<>();
            List<Object> countries =
                    Random.getRandomItemsFromList(COUNTRY.getAll(), amount,
                            false);
            for (Object country : countries) {
                maps.add(getMap((COUNTRY) country));
            }
            return maps;
        }

        public static List<Address> getDifferentCountries(int amount) {
            try {
                List<Map<String, Object>> maps =
                        getMapsDifferentCountries(amount);
                List<Address> addresses = new ArrayList<Address>();
                for (Map<String, Object> map : maps) {
                    addresses.add(Address.create(map));
                }
                return addresses;
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Map<String, Object>> getMaps(
                ADDRESS_RELATIONSHIP relationship, int amount) {
            switch (relationship) {
                case SAME_STATE:
                    return getMapsSameState(amount);
                case DIFFERENT_STATE:
                    return getMapsDifferentStates(amount);
                case SAME_COUNTRY:
                    return getMapsSameCountry(amount);
                case DIFFERENT_COUNTRY:
                    return getMapsDifferentCountries(amount);
                default:
                    return null;
            }
        }

        public static List<Address> get(ADDRESS_RELATIONSHIP relationship,
                                        int amount) {
            switch (relationship) {
                case SAME_STATE:
                    return getSameState(amount);
                case DIFFERENT_STATE:
                    return getDifferentStates(amount);
                case SAME_COUNTRY:
                    return getSameCountry(amount);
                case DIFFERENT_COUNTRY:
                    return getDifferentCountries(amount);
                default:
                    return null;
            }
        }
    }

    public static class Parcels extends Mapper {

        public static Map<String, Object> getMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("weight", Random.getRandomFloatInRange(0, 100));
            map.put("height", Random.getRandomFloatInRange(0, 100));
            map.put("width", Random.getRandomFloatInRange(0, 100));
            map.put("length", Random.getRandomFloatInRange(0, 100));
            return map;
        }

        public static Parcel get() {
            try {
                Map<String, Object> map = getMap();
                return Parcel.create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static Parcel retrieve(String id) {
            try {
                return Parcel.retrieve(id);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Insurance {

        public static Map<String, Object> getMap(float amount) {
            Map<String, Object> insuranceMap = new HashMap<>();
            insuranceMap.put("amount", amount);
            return insuranceMap;
        }

        public static Map<String, Object> getMap() {
            return getMap(Random.getRandomFloatInRange(1, 100));
        }

        public static Shipment insure(Shipment shipment) {
            return Shipments.addInsurance(shipment);
        }

        public static Shipment insure(Shipment shipment, float amount) {
            return Shipments.addInsurance(shipment, amount);
        }
    }

    public static class Shipments extends Mapper {

        public static Map<String, Object> getMap() {
            try {
                List<Map<String, Object>> addressMaps =
                        Addresses.getMapsDifferentStates(2);
                Map<String, Object> parcelMap = Parcels.getMap();

                Map<String, Object> shipmentMap = new HashMap<>();
                shipmentMap.put("to_address", addressMaps.get(0));
                shipmentMap.put("from_address", addressMaps.get(1));
                shipmentMap.put("parcel", parcelMap);
                return shipmentMap;
            } catch (Exception e) {
                return null;
            }
        }

        public static Map<String, Object> getMap(
                Map<String, Object> toAddressMap,
                Map<String, Object> fromAddressMap,
                Map<String, Object> parcelMap) {
            try {
                Map<String, Object> shipmentMap = new HashMap<>();
                shipmentMap.put("to_address", toAddressMap);
                shipmentMap.put("from_address", fromAddressMap);
                shipmentMap.put("parcel", parcelMap);
                return shipmentMap;
            } catch (Exception e) {
                return null;
            }
        }

        public static Map<String, Object> getReturnMap() {
            try {
                Map<String, Object> map = getMap();
                map.put("is_return", true);
                return map;
            } catch (Exception e) {
                return null;
            }
        }

        public static Map<String, Object> getReturnMap(
                Map<String, Object> toAddressMap,
                Map<String, Object> fromAddressMap,
                Map<String, Object> parcelMap) {
            try {
                Map<String, Object> map =
                        getMap(toAddressMap, fromAddressMap, parcelMap);
                map.put("is_return", true);
                return map;
            } catch (Exception e) {
                return null;
            }
        }

        public static Shipment get() {
            try {
                Map<String, Object> map = getMap();
                return create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static Shipment getReturn() {
            try {
                Map<String, Object> map = getReturnMap();
                return create(map);
            } catch (Exception e) {
                return null;
            }
        }

        public static Shipment create(Map<String, Object> shipmentMap) {
            try {
                return Shipment.create(shipmentMap);
            } catch (Exception e) {
                return null;
            }
        }

        public static Shipment addInsurance(Shipment shipment) {
            try {
                Map<String, Object> insuranceMap = Insurance.getMap();
                return shipment.insure(insuranceMap);
            } catch (Exception e) {
                return null;
            }
        }

        public static Shipment addInsurance(Shipment shipment, float amount) {
            try {
                Map<String, Object> insuranceMap = Insurance.getMap(amount);
                return shipment.insure(insuranceMap);
            } catch (Exception e) {
                return null;
            }
        }

        public static Shipment refund(Shipment shipment) {
            try {
                return shipment.refund();
            } catch (Exception ignored) {
                return null;
            }
        }

        public static Map<String, Object> markForReturn(
                Map<String, Object> shipmentMap) {
            shipmentMap.put("is_return", true);
            return shipmentMap;
        }

        // waiting on ability to convert attributes to map to modify shipment for Shipment markForReturn
    }

    public static class Options {

        public static Map<String, Object> getMap() {
            List<Map<String, Object>> maps =
                    JSONReader.getRandomMapsFromJsonFile(Constants.OPTIONS_JSON,
                            1, true);
            return maps.get(0);
        }
    }

    public static class Rates extends Mapper {

        public static List<Rate> get() {
            try {
                Shipment shipment = Shipments.get();
                return get(shipment);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Rate> get(Map<String, Object> shipmentMap) {
            try {
                Shipment shipment = Shipment.create(shipmentMap);
                return get(shipment);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Rate> get(Shipment shipment) {
            try {
                return shipment.getRates();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Smartrates extends Mapper {

        public static List<Rate> get() {
            try {
                Shipment shipment = Shipments.get();
                return get(shipment);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Rate> get(Map<String, Object> shipmentMap) {
            try {
                Shipment shipment = Shipments.create(shipmentMap);
                return get(shipment);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Rate> get(Shipment shipment) {
            try {
                return shipment.getSmartrates();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class TaxIdentifiers extends Mapper {

        /*
        public static List<TaxIdentifier> getRandomTaxIdentifiers() {
            try {
                Shipment shipment = Shipment.get();
                assert shipment != null;
                return shipment.getTaxIdentifiers();
            } catch (Exception e) {
                return null;
            }
        }
         */
    }

    public static class Trackers extends Mapper {

        public static Map<String, Object> getMap() {
            try {
                List<Map<String, Object>> maps =
                        JSONReader.getRandomMapsFromJsonFile(
                                Constants.TRACKERS_JSON, 1, true);
                return maps.get(0);
            } catch (Exception e) {
                return null;
            }
        }

        public static Tracker get() {
            try {
                Map<String, Object> map = getMap();
                return Tracker.create(map);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Batch {}

    public static class CustomsInfos extends Mapper {

        public static Map<String, Object> getMap(int itemsAmount,
                                                 boolean allowDuplicateItems) {
            List<Map<String, Object>> maps =
                    JSONReader.getRandomMapsFromJsonFile(
                            Constants.CUSTOMS_INFO_JSON, 1, true);
            Map<String, Object> map = maps.get(0);
            map.put("customs_items",
                    CustomsItems.getRandomCustomsItemMaps(itemsAmount,
                            allowDuplicateItems));
            return map;
        }

        public static CustomsInfo get(int itemsAmount,
                                      boolean allowDuplicateItems) {
            try {
                Map<String, Object> map =
                        getMap(itemsAmount, allowDuplicateItems);
                return CustomsInfo.create(map);
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class CustomsItems extends Mapper {

        public static List<Map<String, Object>> getRandomCustomsItemMaps(
                int amount, boolean allowDuplicates) {
            return JSONReader.getRandomMapsFromJsonFile(
                    Constants.CUSTOMS_ITEMS_JSON, amount, allowDuplicates);
        }

        public static List<CustomsItem> get(int amount,
                                            boolean allowDuplicates) {
            try {
                List<Map<String, Object>> maps =
                        getRandomCustomsItemMaps(amount, allowDuplicates);
                List<CustomsItem> customsItems = new ArrayList<CustomsItem>();
                for (Map<String, Object> map : maps) {
                    customsItems.add(CustomsItem.create(map));
                }
                return customsItems;
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class Events {}

    public static class Fees extends Mapper {

        public static List<Fee> get() {
            try {
                Shipment shipment = Shipments.get();
                return get(shipment);
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Fee> get(Shipment shipment) {
            try {
                return shipment.getFees();
            } catch (Exception e) {
                return null;
            }
        }

        public static List<Fee> get(Map<String, Object> shipmentMap) {
            try {
                Shipment shipment = Shipments.create(shipmentMap);
                return get(shipment);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Orders {}

    public static class Pickups {

        public static Map<String, Object> getMap() {
            try {
                List<Map<String, Object>> maps =
                        JSONReader.getRandomMapsFromJsonFile(
                                Constants.PICKUPS_JSON, 1, true);
                Map<String, Object> map = maps.get(0);

                Map<String, Object> toAddressMap = Addresses.getMap();
                Map<String, Object> fromAddressMap = Addresses.getMap();
                map.put("address", toAddressMap);

                Map<String, Object> parcelMap = Parcels.getMap();
                Map<String, Object> shipmentMap =
                        Shipments.getMap(toAddressMap, fromAddressMap,
                                parcelMap);
                map.put("shipment", shipmentMap);

                // List<Map<String, Object>> carrierAccountsMaps = CarrierAccounts.getMaps(1, true);

                List<Date> dates = Dates.getFutureDates(2);
                map.put("min_datetime", dates.get(0).toString());
                map.put("max_datetime", dates.get(1).toString());

                return map;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Reports extends Mapper {

        public static Map<String, Object> getMap() {
            try {
                Map<String, Object> dateMap = new HashMap<String, Object>();
                List<Date> dates = Dates.getPastDates(2);
                dateMap.put("start_date", dates.get(1).toString());
                dateMap.put("end_date", dates.get(0).toString());

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("shipment", dateMap);
                return map;
            } catch (Exception e) {
                return null;
            }
        }

        public static Report get() {
            try {
                Map<String, Object> map = getMap();
                return Report.create(map);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class ScanForms {}

    public static class Webhooks extends Mapper {

        public static Map<String, Object> getMap() {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("url", "http://www.google.com");
                return map;
            } catch (Exception e) {
                return null;
            }
        }

        public static Webhook get() {
            try {
                Map<String, Object> map = getMap();
                return Webhook.create(map);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Users {}

    public static class Carriers {

        public static List<String> get(int amount) {
            List<String> carriersStrings = new ArrayList<>();
            List<Object> carriers = JSONReader.getRandomItemsFromJsonFile(
                    Constants.CARRIERS_JSON, amount, false);
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

    public static class Labels {

        public static Map<String, Object> getRandomLabelOptions() {
            List<Map<String, Object>> maps =
                    JSONReader.getRandomMapsFromJsonFile(
                            Constants.LABEL_OPTIONS_JSON, 1, true);
            return maps.get(0);
        }
    }

    public static class PostageLabels extends Mapper {

        public static PostageLabel get() {
            try {
                return Shipments.get().getPostageLabel();
            } catch (Exception e) {
                return null;
            }
        }

        public static PostageLabel get(Map<String, Object> shipmentMap) {
            try {
                return Shipment.create(shipmentMap).getPostageLabel();
            } catch (Exception e) {
                return null;
            }
        }

        public static PostageLabel get(Shipment shipment) {
            try {
                return shipment.getPostageLabel();
            } catch (Exception e) {
                return null;
            }
        }

    }

}
