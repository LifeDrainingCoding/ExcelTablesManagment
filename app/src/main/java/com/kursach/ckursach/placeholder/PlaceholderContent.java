package com.kursach.ckursach.placeholder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {
   static FirebaseUserMetadata metadata = FirebaseAuth.getInstance().getCurrentUser().getMetadata();

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();

    private static final int COUNT =2;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceholderItem createPlaceholderItem(int position) {
        long creationDateLong = metadata.getCreationTimestamp();
        long lastLoginLong = metadata.getLastSignInTimestamp();
        Date creationDate = new Date(creationDateLong);
        Date lastLogin = new Date(lastLoginLong);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        String creationDateString, lastLoginString;
        creationDateString  = sdf.format(creationDate);
        lastLoginString  = sdf.format(lastLogin);

        if (position == 1){


        return new PlaceholderItem(String.valueOf(position), "Creation date: "+creationDateString, makeDetails(position));}

        else {
            return new PlaceholderItem(String.valueOf(position), "Last login: " + lastLoginString, makeDetails(position));
        }
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {

        public final String id;
        public final String content;
        public final String details;


        public PlaceholderItem(String id, String content, String details) {
            this.id = id;

            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}