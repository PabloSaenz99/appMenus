package ucm.appmenus;

import org.json.JSONException;
import org.json.JSONObject;

public class Foto {

    private String dir;
    private int height;
    private int width;

    public Foto(String dir, int h, int w){
        this.dir = dir;
        this.height = h;
        this.width = w;
    }

    public Foto(JSONObject jFoto) throws JSONException {
        dir = jFoto.getString("dir");
        height = jFoto.getInt("height");
        width = jFoto.getInt("width");
    }

    public JSONObject getJSONObject() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("dir", dir);
        res.put("height", height);
        res.put("width", width);
        return res;
    }
}
