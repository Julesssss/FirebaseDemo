package julianrosser.firebasedemo.model.objects;

public class Dessert {

    private String name;
    private String id;

    public Dessert() {
    }

    public Dessert(String name) {
        this.name = name;
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
