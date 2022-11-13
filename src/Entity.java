import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
    public String key;
    public Map<Character, List<Entity>> map;

    public Entity(String key) {
        this.key = key;
        this.map = new HashMap();
    }

    @Override
    public String toString() {
        return key;
    }
}