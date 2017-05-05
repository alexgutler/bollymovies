package br.com.alexgutler.bollymovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class FilmeAdapter extends ArrayAdapter<Filme>
{
    private static final int VIEW_TYPE_DESTAQUE = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private boolean useFilmeDestaque = false;

    public FilmeAdapter(Context context, ArrayList<Filme> filmes) {
        super(context, 0, filmes); // o '0' (zero) passado como parâmetro abaixo é do layout pois iremos utilizar um customizado
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        int viewType = getItemViewType(position);
        View itemView = convertView;
        Filme filme = getItem(position);

        switch (viewType) {
            case VIEW_TYPE_DESTAQUE: {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_filme_destaque, parent, false);

                TextView titulo = (TextView) itemView.findViewById(R.id.item_titulo);
                titulo.setText(filme.getTitulo());

                RatingBar avaliacao = (RatingBar) itemView.findViewById(R.id.item_rating);
                avaliacao.setRating(filme.getAvaliacao());

                // recupera o ImageView da capa
                ImageView capa = (ImageView) itemView.findViewById(R.id.item_capa);
                // fazer o download da imagem e setar no ImageView passando o path como parâmetro para execução
                new DownloadImageTask(capa).execute(filme.getCapaPath());

                break;
            }
            case VIEW_TYPE_ITEM: {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_filme, parent, false);

                FilmeHolder holder;
                if (itemView.getTag() == null) { // verifica se ainda não foi criado um ViewHolder no itemView para então o criar
                    holder = new FilmeHolder(itemView); // passa a itemView para buscar os atributos
                    itemView.setTag(holder); // seta a tag do holder
                } else { // recupera o ViewHolder caso já estivesse setado
                    holder = (FilmeHolder) itemView.getTag(); // recupera a tag transformando em holder
                }

                holder.titulo.setText(filme.getTitulo());
                holder.descricao.setText(filme.getDescricao());
                holder.data.setText(filme.getDataLancamento());
                holder.avaliacao.setRating(filme.getAvaliacao());

                // fazer o download da imagem e setar no ImageView passando o path como parâmetro para execução
                new DownloadImageTask(holder.poster).execute(filme.getPosterPath());

                break;
            }
        }

        return itemView;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && useFilmeDestaque ? VIEW_TYPE_DESTAQUE : VIEW_TYPE_ITEM);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public static class FilmeHolder {
        TextView titulo;
        TextView descricao;
        TextView data;
        RatingBar avaliacao;
        ImageView poster;

        public FilmeHolder(View view) {
            titulo = (TextView) view.findViewById(R.id.item_titulo);
            descricao = (TextView) view.findViewById(R.id.item_descricao);
            data = (TextView) view.findViewById(R.id.item_data);
            avaliacao = (RatingBar) view.findViewById(R.id.item_rating);
            poster = (ImageView) view.findViewById(R.id.item_poster);
        }
    }

    public void setUseFilmeDestaque(boolean useFilmeDestaque) {
        this.useFilmeDestaque = useFilmeDestaque;
    }
}