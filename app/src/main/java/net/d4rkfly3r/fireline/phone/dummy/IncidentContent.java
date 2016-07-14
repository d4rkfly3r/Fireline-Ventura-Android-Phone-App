package net.d4rkfly3r.fireline.phone.dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class IncidentContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<IncidentItem> ITEMS = new ArrayList<IncidentItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, IncidentItem> ITEM_MAP = new HashMap<>();

//    private static final int COUNT = 25;

    static {
        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
    }

    public static void addItem(IncidentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.incidentNumber, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class IncidentItem {
        public final String address;
        public final String block;
        public final String city;
        public final String comment;
        public final String incidentNumber;
        public final String incidentType;
        public final double latitude;
        public final double longitude;
        public final String responseDate;
        public final String status;
        public final String[] units;

        public IncidentItem(String address, String block, String city, String comment, String incidentNumber, String incidentType, double latitude, double longitude, String responseDate, String status, String units) {
            this.address = address;
            this.block = block;
            this.city = city;
            this.comment = comment;
            this.incidentNumber = incidentNumber;
            this.incidentType = incidentType;
            this.latitude = latitude;
            this.longitude = longitude;
            this.responseDate = responseDate;
            this.status = status;
            this.units = units.split(", ");
        }

        public IncidentItem(String address, String block, String city, String comment, String incidentNumber, String incidentType, double latitude, double longitude, String responseDate, String status, String[] units) {
            this.address = address;
            this.block = block;
            this.city = city;
            this.comment = comment;
            this.incidentNumber = incidentNumber;
            this.incidentType = incidentType;
            this.latitude = latitude;
            this.longitude = longitude;
            this.responseDate = responseDate;
            this.status = status;
            this.units = units;
        }

        @Override
        public String toString() {
            return "IncidentItem{" +
                           "address='" + address + '\'' +
                           ", block='" + block + '\'' +
                           ", city='" + city + '\'' +
                           ", comment='" + comment + '\'' +
                           ", incidentNumber='" + incidentNumber + '\'' +
                           ", incidentType='" + incidentType + '\'' +
                           ", latitude=" + latitude +
                           ", longitude=" + longitude +
                           ", responseDate='" + responseDate + '\'' +
                           ", status='" + status + '\'' +
                           ", units=" + Arrays.toString(units) +
                           '}';
        }
    }
}
