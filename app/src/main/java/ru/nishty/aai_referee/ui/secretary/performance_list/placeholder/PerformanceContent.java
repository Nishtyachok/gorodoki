package ru.nishty.aai_referee.ui.secretary.performance_list.placeholder;

import ru.nishty.aai_referee.entity.referee.Performance;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PerformanceContent {

    /**
     * An array of sample (placeholder) items.
     */


    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final List<Performance> ITEM_MAP = new ArrayList<>();



    public static void addItem(Performance performance) {

        ITEM_MAP.add(performance);
    }

    public static void fill(List<Performance> content){
        ITEM_MAP.clear();
        ITEM_MAP.addAll(content);
    }




    /**
     * A placeholder item representing a piece of content.
     */

}