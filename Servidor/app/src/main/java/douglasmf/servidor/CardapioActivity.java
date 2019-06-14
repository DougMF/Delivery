package douglasmf.servidor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

public class CardapioActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        final FloatingActionButton fabNovoItem = findViewById(R.id.fabNovoItem);
        final ImageButton iBConfig = findViewById(R.id.iBConfig);

        context = this;

        SendHttpRequestTask ta = new SendHttpRequestTask();
        ta.execute();

        //Adaptador para retornar o fragmento utilizado
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Fragment f = mSectionsPagerAdapter.getItem(1);

        final ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fabNovoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDadosItem();
            }
        });

        iBConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, iBConfig);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_dados:
                                Intent iDados = new Intent(getApplicationContext(),
                                        DadosActivity.class);
                                startActivity(iDados);
                                finish();
                                return true;
                            case R.id.action_cardapio:
                                Intent iPedidos = new Intent(getApplicationContext(),
                                        PedidosActivity.class);
                                startActivity(iPedidos);
                                finish();
                                return true;
                            case R.id.action_clientes:
                                Intent iClientes = new Intent(getApplicationContext(),
                                        ClientesActivity.class);
                                startActivity(iClientes);
                                finish();
                                return true;
                            case R.id.action_relatorio:
                                Intent iRelatorio = new Intent(getApplicationContext(),
                                        RelatorioActivity.class);
                                startActivity(iRelatorio);
                                finish();
                                return true;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //Popup "popup_dados_item"
    public void popupDadosItem() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_dados_item);
        dialog.show();

        final Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button bAdicionar = dialog.findViewById(R.id.bAdicionar);
        Button bCancelar = dialog.findViewById(R.id.bCancelar);

        //Listener do botão "Adicionar"
        bAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText eTNome = dialog.findViewById(R.id.eTNome);
                EditText eTDescricao = dialog.findViewById(R.id.eTDescricao);
                EditText eTPreco = dialog.findViewById(R.id.eTPreco);

                //TODO: Alterar dados
            }
        });

        //Listener do botão "Cancelar"
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static class PlaceholderFragment extends Fragment {
        //Representa o número do fragmento
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;

        public PlaceholderFragment() {
        }

        //Retorna uma instância do fragmento especificado por "sectionNumber"
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                //Aba "Lanches"
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_cardapio, container, false);
                    recyclerView = rootView.findViewById(R.id.my_recycler_view);
                    layoutManager = new LinearLayoutManager(getContext());
                    RecyclerView.Adapter cardapioAdapter = new CardAdapterCardapio(AppRequest.getInstance().cardapio);

                    //Espera preencher o array com imagens
                   //while(AppRequest.getInstance().imagens.isEmpty()) ;

                        ProgressBar progresso = rootView.findViewById(R.id.progresso);
                        progresso.setVisibility(ProgressBar.VISIBLE);
                        new AcessoSQL().atualizarCardapio(cardapioAdapter, progresso);


                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(cardapioAdapter);

                    break;
                //Aba "Bebidas"
                case 2:
                    /*rootView = inflater.inflate(R.layout.fragment_bebidas, container, false);
                    recyclerView = rootView.findViewById(R.id.my_recycler_view);

                    layoutManager = new LinearLayoutManager(getContext());
                    //mAdapter = new CardAdapterCardapio(AppRequest.getInstance().getCardapio(), mAdapter);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(mAdapter);
*/
                    break;
            }

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            //2 páginas
            return 2;
        }
    }
}
