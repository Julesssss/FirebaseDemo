package julianrosser.firebasedemo.model;

import android.content.Context;

import java.util.Random;

import julianrosser.firebasedemo.R;

public class Dessert {

    private String name;
    private String id;

    public Dessert() {
    }

    public Dessert(String name) {
        this.name = name;
    }

    public static Dessert newRandom(Context context) {
        String[] desserts = context.getResources().getStringArray(R.array.dessert_names);
        String dessertName = desserts[new Random().nextInt(desserts.length - 1)];
        return new Dessert(dessertName);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
