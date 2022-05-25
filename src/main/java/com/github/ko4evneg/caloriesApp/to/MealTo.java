package com.github.ko4evneg.caloriesApp.to;

import java.time.LocalDateTime;

public class MealTo {
    private Integer id;

    private LocalDateTime dateTime;

    private String description;

    private int calories;

    private Integer userId;

    private boolean excess;

    //TODO: migrate to lombok
    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess, Integer userId) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.userId = userId;
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, Integer userId) {
        this(id, dateTime, description, calories, false, userId);
    }

    public MealTo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isExcess() {
        return excess;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MealTo mealTo = (MealTo) o;

        if (calories != mealTo.calories) return false;
        if (excess != mealTo.excess) return false;
        if (id != null ? !id.equals(mealTo.id) : mealTo.id != null) return false;
        if (dateTime != null ? !dateTime.equals(mealTo.dateTime) : mealTo.dateTime != null) return false;
        if (description != null ? !description.equals(mealTo.description) : mealTo.description != null) return false;
        return userId != null ? userId.equals(mealTo.userId) : mealTo.userId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + calories;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (excess ? 1 : 0);
        return result;
    }
}
