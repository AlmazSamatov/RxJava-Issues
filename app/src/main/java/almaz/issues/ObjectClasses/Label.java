package almaz.issues.ObjectClasses;

/**
 * Created by Almaz on 3/20/2018.
 */

public class Label{
    private String name;
    private String color;

    public Label(String name, String color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
