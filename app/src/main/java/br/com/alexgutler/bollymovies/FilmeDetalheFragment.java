package br.com.alexgutler.bollymovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmeDetalheFragment extends Fragment
{
    private Filme filme;

    public FilmeDetalheFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            filme = (Filme)getArguments().getSerializable(MainActivity.KEY_FILME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filme_detalhe, container, false);

        if (filme == null) {
            return view;
        }

        TextView titulo = (TextView) view.findViewById(R.id.item_titulo);
        titulo.setText(filme.getTitulo());

        TextView descricao = (TextView) view.findViewById(R.id.item_descricao);
        descricao.setText(filme.getDescricao());

        TextView data = (TextView) view.findViewById(R.id.item_data);
        data.setText(filme.getDataLancamento());

        RatingBar rating = (RatingBar) view.findViewById(R.id.item_rating);
        rating.setRating(filme.getAvaliacao());

        return  view;
    }

}
