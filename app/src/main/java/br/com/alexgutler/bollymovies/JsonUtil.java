package br.com.alexgutler.bollymovies;

/*{
        "page": 1,
        "results": [],
        "total_results": 19640,
        "total_pages": 982
}*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static List<Filme> fromJsonToList(String json) {
        List<Filme> list = new ArrayList<>();

        try {
            JSONObject jsonBase = new JSONObject(json);
            JSONArray results = jsonBase.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject filmeObject = results.getJSONObject(i);
                Filme itemFilme = new Filme(filmeObject);
                list.add(itemFilme);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
