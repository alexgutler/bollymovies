package br.com.alexgutler.bollymovies;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/*{
        "poster_path": "/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg",
        "adult": false,
        "overview": "Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever.",
        "release_date": "2016-10-19",
        "genre_ids": [
        53,
        28,
        80,
        18,
        9648
        ],
        "id": 343611,
        "original_title": "Jack Reacher: Never Go Back",
        "original_language": "en",
        "title": "Jack Reacher: Never Go Back",
        "backdrop_path": "/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg",
        "popularity": 33.793474,
        "vote_count": 304,
        "video": false,
        "vote_average": 4.38
}*/

public class Filme implements Serializable
{
    private long id;
    private String titulo;
    private String descricao;
    private String dataLancamento;
    private String posterPath;
    private String capaPath;
    private float avaliacao;

    public Filme(long id, String titulo, String descricao, String dataLancamento, String posterPath, String capaPath, float avaliacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataLancamento = dataLancamento;
        this.posterPath = posterPath;
        this.capaPath = capaPath;
        this.avaliacao = avaliacao;
    }

    public Filme(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.titulo = jsonObject.getString("title");
        this.descricao = jsonObject.getString("overview");
        this.dataLancamento = jsonObject.getString("release_date");
        this.posterPath = jsonObject.getString("poster_path");
        this.capaPath = jsonObject.getString("backdrop_path");
        this.avaliacao = (float) jsonObject.getDouble("vote_average");
    }

    // https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
    private String buildPath(String width, String path)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("https://image.tmdb.org/t/p/")
                .append(width)
                .append(path);
        return builder.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getPosterPath() {
        return buildPath("w500", posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getCapaPath() {
        return buildPath("w780", capaPath);
    }

    public void setCapaPath(String capaPath) {
        this.capaPath = capaPath;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }
}
