package com.icm.tiago.ementasua.dataAPI;

/**
 * Created by ico on 2015-10-07.
 */
public class MealCourse {
    private int courseOrder;
    private String foodOption;

    public int getMealCourse() {
        return courseOrder;
    }

    public String getFoodOption() {
        return foodOption;
    }

    public MealCourse(int mealCourseOrder, String foodOption) {
        this.courseOrder = mealCourseOrder;
        this.foodOption = foodOption;
    }

    @Override
    public String toString() {
        return "MealCourse{" +
                "courseOrder=" + courseOrder +
                ", foodOption='" + foodOption + '\'' +
                '}';
    }
}
