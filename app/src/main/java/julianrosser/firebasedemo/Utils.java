package julianrosser.firebasedemo;

import android.content.res.Resources;

import java.util.Random;

public class Utils {

    public static String getRandomDessert(Resources resources) {
        String[] desserts = resources.getStringArray(R.array.dessert_names);
        return desserts[new Random().nextInt(desserts.length -1)];
    }

}
