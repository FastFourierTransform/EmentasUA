package com.icm.tiago.ementasua.dataAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds information for daily menus at the UA's canteens; it  may contain one or several days
 */
public class UAMenus {

    static final public int COURSE_ORDER_SOUP = 0;
    static final public int COURSE_ORDER_MEAT_NRM = 1;
    static final public int COURSE_ORDER_FISH_NRM = 2;

    // the list of options, per day and per canteen
    private List<DailyOption> dailyMenus = new ArrayList<>();


    public void add(DailyOption dailyOption) {
        this.getDailyMenus().add(dailyOption);
    }


    /**
     * dumps the content of the object into a string for logging/dubbuging
     * @return the contents as String
     */
    @Override
    public String toString() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder builder = new StringBuilder();
        for (DailyOption option : this.getDailyMenus()) {
            builder.append("Day: ");
            builder.append(dateFormater.format(option.getDate()));
            builder.append("\tCanteen: ");
            builder.append(option.getCanteenSite());
            builder.append("\tMeal type: ");
            builder.append(option.getDailyMeal());
            builder.append("\tIs open? ");
            builder.append(option.isAvailable());
            builder.append("\n");
            for (MealCourse mealOption: option.getMealCourseList() ) {
                builder.append("\tCourse: ");   builder.append(mealOption.getMealCourse());
                builder.append("\t meal: ");   builder.append(mealOption.getFoodOption());
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public List<DailyOption> getDailyMenus() {
        return dailyMenus;
    }
}
