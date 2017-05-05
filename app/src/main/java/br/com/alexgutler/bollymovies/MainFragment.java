package br.com.alexgutler.bollymovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
// https://api.themoviedb.org/3/movie/popular?api_key=a2e06666ad01fc50e901c9ca1dbf8637&language=pt-BR
// https://developers.themoviedb.org/3/getting-started

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment
{
    private int posicaoItem = ListView.INVALID_POSITION;
    private static final String KEY_POSITION = "SELECIONADO";
    private ListView list;
    private boolean useFilmeDestaque = false;
    private FilmeAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // flag para avisar a fragment que o menu deverá aparecer
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        list = (ListView) view.findViewById(R.id.list_filmes);

        final ArrayList<Filme> arrayList = new ArrayList<>();

        adapter = new FilmeAdapter(getContext(), arrayList);
        adapter.setUseFilmeDestaque(useFilmeDestaque);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Filme filme = arrayList.get(position);

                // Interface implementada no MainActivity para distinguir ação entre tablet e smartphone
                Callback callback = (Callback) getActivity();
                callback.onItemSelected(filme);
                posicaoItem = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_POSITION)) {
            posicaoItem = savedInstanceState.getInt(KEY_POSITION);
        }

        new FilmesAsyncTask().execute();

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) { // salvar o item selecionado quando for salvar o estado para recuperar depois
        if (posicaoItem != ListView.INVALID_POSITION) {
            outState.putInt(KEY_POSITION, posicaoItem);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (posicaoItem != ListView.INVALID_POSITION && list != null) {
            list.smoothScrollToPosition(posicaoItem); // setar a posição na lista
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_atualizar:
                new FilmesAsyncTask().execute();
                Toast.makeText(getContext(), "Atualizando os filmes...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public class FilmesAsyncTask extends AsyncTask<Void, Void, List<Filme>> {

        @Override
        protected List<Filme> doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String urlBase = "https://api.themoviedb.org/3/movie/popular?";
                String apiKey = "api_key";
                String language = "language";

                Uri uriApi = Uri.parse(urlBase).buildUpon()
                        .appendQueryParameter(apiKey, BuildConfig.TMDB_API_KEY)
                        .appendQueryParameter(language, "pt-BR")
                        .build();

                URL url = new URL(uriApi.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String linha;
                StringBuffer buffer = new StringBuffer();

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n"); // pular para próxima
                }

                return JsonUtil.fromJsonToList(buffer.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Filme> filmes) {
            adapter.clear();
            adapter.addAll(filmes);
            adapter.notifyDataSetChanged();
        }
    }


    public interface Callback {
        void onItemSelected(Filme filme);
    }


    public void setUseFilmeDestaque(boolean useFilmeDestaque) {
        this.useFilmeDestaque = useFilmeDestaque;

        if (adapter != null) {
            adapter.setUseFilmeDestaque(useFilmeDestaque);
        }
    }
}