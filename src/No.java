import java.util.Map;

/**
 * Created by jean.antunes on 06/05/2017.
 */
public class No {
    private Integer key;
    private Integer caracter;
    private No noLeft;
    private No noRight;

    public No(Integer key,Integer caracter) {
        super();
        this.key = key;
        this.caracter = caracter;
    }

    public Integer getValue() {
        return caracter;
    }

    public void setValue(Integer caracter) {
        this.caracter = caracter;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public No getNoLeft() {
        return noLeft;
    }

    public void setNoLeft(No noLeft) {
        this.noLeft = noLeft;
    }

    public No getNoRight() {
        return noRight;
    }

    public void setNoRight(No noRight) {
        this.noRight = noRight;
    }
}
