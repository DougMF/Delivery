package douglasmf.servidor;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class PedidosActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        final ImageButton iBConfig = findViewById(R.id.iBConfig);

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
                                Intent iCardapio = new Intent(getApplicationContext(),
                                        CardapioActivity.class);
                                startActivity(iCardapio);
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

        context = this;
        AppRequest.getInstance().context = context;

        //Adaptador para retornar o fragmento utilizado
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Fragment f = mSectionsPagerAdapter.getItem(1);

        final ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    public static class PlaceholderFragment extends Fragment {
        //Representa o número do fragmento
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recyclerView;
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
                //Aba "Em espera"
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_espera, container, false);
                    recyclerView = rootView.findViewById(R.id.my_recycler_view);
                    layoutManager = new LinearLayoutManager(getContext());
                    AppRequest.getInstance().emEsperaAdapter = new CardAdapterPedidoEspera(AppRequest.getInstance().pedidosEspera);

                    //Inicia a thread para pegar pedidos se ela já não estiver rodando
                    if(!AppRequest.getInstance().threadGetPedidos) {
                        new AcessoSQL().getPedidos();
                        AppRequest.getInstance().threadGetPedidos = true;
                    }

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(AppRequest.getInstance().emEsperaAdapter);

                    break;
                //Aba "Em preparo"
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_preparo, container, false);
                    recyclerView = rootView.findViewById(R.id.my_recycler_view);
                    layoutManager = new LinearLayoutManager(getContext());
                    AppRequest.getInstance().emPreparoAdapter = new CardAdapterPedidoPreparo(AppRequest.getInstance().pedidosPreparo);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(AppRequest.getInstance().emPreparoAdapter);

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
