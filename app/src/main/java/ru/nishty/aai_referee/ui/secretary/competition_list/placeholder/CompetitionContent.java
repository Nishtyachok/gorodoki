package ru.nishty.aai_referee.ui.secretary.competition_list.placeholder;

import ru.nishty.aai_referee.entity.referee.Competition;
import ru.nishty.aai_referee.listeners.ScanListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CompetitionContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<Competition> ITEMS = new ArrayList<>();

    /**
     * A map of sample (placeholder) items, by ID.
     */

    public static ScanListener scanListener;

    public static ScanListener clickListener;

    public static void fill(List<Competition> competitions){
        ITEMS.clear();
        ITEMS.addAll(competitions);
    }


    public static void setScanListener(ScanListener listener){
        CompetitionContent.scanListener = listener;
    }

    public static void setClickListener(ScanListener listener){
        CompetitionContent.clickListener = listener;
    }

    public static void onScan(){
        if (scanListener!=null)
            scanListener.onScan();
    }

    public static void onClick(){
        if (clickListener!=null)
            clickListener.onScan();
    }

    private static void addItem(Competition item) {
        ITEMS.add(item);

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




}