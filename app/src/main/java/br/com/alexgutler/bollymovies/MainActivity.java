package br.com.alexgutler.bollymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback
{
    public static final String KEY_FILME = "filme";
    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // faz a verificação para saber se está utilizando o layout para tablet
        if (findViewById(R.id.fragment_filme_detalhe) != null) {

            if (savedInstanceState == null) { // se o layout ainda não foi criado
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_filme_detalhe, new FilmeDetalheFragment())
                        .commit();
            }

            isTablet = true;
        } else {
            isTablet = false;
        }

        // recuperar o fragment para marcar para não utilizar item destaque quando for tablet
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        fragment.setUseFilmeDestaque(!isTablet);
    }


    @Override
    public void onItemSelected(Filme filme) {
        if (isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FilmeDetalheFragment detalheFragment = new FilmeDetalheFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(MainActivity.KEY_FILME, filme);
            detalheFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.fragment_filme_detalhe, detalheFragment);
            fragmentTransaction.commit();

        } else {
            Intent intent = new Intent(this, FilmeDetalheActivity.class);
            intent.putExtra(MainActivity.KEY_FILME, filme);
            startActivity(intent);
        }
    }
}