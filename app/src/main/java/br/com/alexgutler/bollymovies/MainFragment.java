package br.com.alexgutler.bollymovies;

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

import java.util.ArrayList;

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

        arrayList.add(new Filme("Homem Aranha", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "18/03/2000", 3.5f));
        arrayList.add(new Filme("Homem Cobra", "Filme de herói sobre o cobra", "19/05/2000", 4));
        arrayList.add(new Filme("Homem Javali", "Filme de herói sobre o javali", "25/06/2000", 2));
        arrayList.add(new Filme("Homem Passáro", "Filme de herói sobre o passáro", "03/07/2000", 1));
        arrayList.add(new Filme("Homem Galinha", "Filme de herói sobre o galinha", "14/08/2000", 5));
        arrayList.add(new Filme("Homem Cachorro", "Filme de herói sobre o cachorro", "21/09/2000", 4.5f));

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
                Toast.makeText(getContext(), "Atualizando os filmes...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

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