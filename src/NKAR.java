import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NKAR {
    List<Entity> value;
    public Map<Character, NKAR> map;

    public NKAR(List<Entity> value) {
        this.value = value;
        this.map = new HashMap<>();
    }

    @Override
    public String toString() {
        String out = new String();
        for (Entity ent : value) {
            out += ent.key;
        }
        return out;
    }
}
