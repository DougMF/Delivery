package douglasmf.testedelivery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Pedidos extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //Inicializa o TextView "Total" global
        AppRequest.getInstance().bFinalizar = findViewById(R.id.bFinalizar);
        AppRequest.getInstance().tVTotal = findViewById(R.id.tVTotal);
        AppRequest.getInstance().atualizarTotal();

        final Button bFinalizar = findViewById(R.id.bFinalizar);
        final ImageButton iBConfig = findViewById(R.id.iBConfig);

        AppRequest.getInstance().atualizarTotal();

        context = this;

        //Adaptador para retornar o fragmento utilizado
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Fragment f = mSectionsPagerAdapter.getItem(2);

        final ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        bFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bFinalizar.setEnabled(false);
                AcessoSQL acessoSQL = new AcessoSQL();
                acessoSQL.finalizarPedido(context, bFinalizar);
            }
        });

        iBConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, iBConfig);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_meu_pedido, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_config:
                                Intent i = new Intent(getApplicationContext(),
                                        DadosActivity.class);
                                startActivity(i);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meu_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_config) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                //Aba "Meu pedido"
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_meu_pedido, container, false);
                    recyclerView = rootView.findViewById(R.id.my_recycler_view);
                    layoutManager = new LinearLayoutManager(getContext());
                    RecyclerView.Adapter meuPedidoAdapter = new CardAdapterPedido(AppRequest.getInstance().getMeusPedidos());
                    AppRequest.getInstance().meuPedidoAdapter = meuPedidoAdapter;
                    AppRequest.getInstance().recyclerView = recyclerView;
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(meuPedidoAdapter);

                    break;
                //Aba "Cardápio"
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_cardapio, container, false);
                    recyclerView = rootView.findViewById(R.id.my_recycler_view);

                    layoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new CardAdapterCardapio(AppRequest.getInstance().getCardapio(), mAdapter);

                    //Preenche o array "cardapio" se este estiver vazio
                    if(AppRequest.getInstance().getCardapio().isEmpty()) {
                        ProgressBar progresso = rootView.findViewById(R.id.progresso);
                        progresso.setVisibility(ProgressBar.VISIBLE);
                        AcessoSQL acesso = new AcessoSQL();
                        acesso.atualizarCardapio(mAdapter, progresso);
                    }

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(mAdapter);

                    break;
                //Aba "Bebidas"
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_bebidas, container, false);
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
            //3 páginas
            return 3;
        }
    }
}
