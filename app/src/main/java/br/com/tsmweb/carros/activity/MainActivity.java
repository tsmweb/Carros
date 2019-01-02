package br.com.tsmweb.carros.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.adapter.TabsAdapter;
import br.com.tsmweb.carros.databinding.NavDrawerHeaderBinding;
import br.com.tsmweb.carros.fragments.dialog.AboutDialog;
import br.com.tsmweb.carros.utils.ImageUtils;
import br.com.tsmweb.carros.utils.Prefs;
import br.com.tsmweb.carros.viewModel.NavHeaderViewModel;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavHeaderViewModel navHeaderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura a Toolbar
        Toolbar toolbar = setUpToolbar();

        // Configura o NavDrawer
        setupNavDrawer(toolbar);

        // Carrega informações da conta no NavigationView
        updateNavViewValues("Tiago Martins", "tiago.tsmweb@gmail.com", R.drawable.ic_logo_user);

        setViewPagerTabs();

        // Inicializa o layout principal com o fragment dos carros
        //replaceFragment(new CarrosTabFragment());

        // FAB
        findViewById(R.id.fab).setOnClickListener(v -> {
            snack(v, "Exemplo de FAB Button.");
        });
    }

    private void setupNavDrawer(Toolbar toolbar) {
        // Configura o Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configura o Navigation View
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configura o viewModel no NavDrawerHeader
        navHeaderViewModel = ViewModelProviders.of(this).get(NavHeaderViewModel.class);

        DataBindingUtil.bind(navigationView.getHeaderView(0));
        NavDrawerHeaderBinding navHeaderBinding = DataBindingUtil.getBinding(navigationView.getHeaderView(0));
        navHeaderBinding.setViewModel(navHeaderViewModel);
    }

    // Atualiza os dados do header do Navigation View
    public void updateNavViewValues(String nome, String email, int foto) {
        navHeaderViewModel.nome.set(nome);
        navHeaderViewModel.email.set(email);
        navHeaderViewModel.foto.set(ImageUtils.getUriToDrawable(this, foto));
    }

    // Configura as Tabs + ViewPager
    private void setViewPagerTabs() {
        // ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new TabsAdapter(getContext(), getSupportFragmentManager()));

        // Tabs
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Cria as tabs com o mesmo adapter utilizado pelo ViewPager
        tabLayout.setupWithViewPager(viewPager);

        int cor = ContextCompat.getColor(getContext(), R.color.white);
        // Cor branca no texto (o fundo azul foi definido no layout)
        tabLayout.setTabTextColors(cor, cor);

        // Lê o índice da última tab utilizada no aplicativo
        int tabIdx = Prefs.getInteger(getContext(), "tabIdx");
        viewPager.setCurrentItem(tabIdx);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                // Salva o índice da página/tab selecionada
                Prefs.setInteger(getContext(), "tabIdx", viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // Trata os eventos de click do menu lateral (NavigationDrawer)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_item_carros_todos:
                // Nada aqui, pois somente a MainActivity tem menu lateral
                break;
            case R.id.nav_item_carros_classicos:
                Intent intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.classicos);
                startActivity(intent);
                break;
            case R.id.nav_item_carros_esportivos:
                intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.esportivos);
                startActivity(intent);
                break;
            case R.id.nav_item_carros_luxo:
                intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.luxo);
                startActivity(intent);
                break;
            case R.id.nav_item_site_livro:
                startActivity(new Intent(getContext(), SiteLivroActivity.class));
                break;
            case R.id.nav_item_settings:
                startActivity(new Intent(getContext(), ConfiguracoesActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            AboutDialog.showAbout(getSupportFragmentManager());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
