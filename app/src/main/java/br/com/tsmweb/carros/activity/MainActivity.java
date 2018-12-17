package br.com.tsmweb.carros.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.NavDrawerHeaderBinding;
import br.com.tsmweb.carros.fragments.AboutDialog;
import br.com.tsmweb.carros.fragments.CarrosFragment;
import br.com.tsmweb.carros.fragments.SiteLivroFragment;
import br.com.tsmweb.carros.utils.ImageUtils;
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

        // Inicializa o layout principal com o fragment dos carros
        replaceFragment(CarrosFragment.newInstance(R.string.todos));
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

    // Trata os eventos de click do menu lateral (NavigationDrawer)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_item_carros_todos:
                replaceFragment(CarrosFragment.newInstance(R.string.todos));
                break;
            case R.id.nav_item_carros_classicos:
                replaceFragment(CarrosFragment.newInstance(R.string.classicos));
                break;
            case R.id.nav_item_carros_esportivos:
                replaceFragment(CarrosFragment.newInstance(R.string.esportivos));
                break;
            case R.id.nav_item_carros_luxo:
                replaceFragment(CarrosFragment.newInstance(R.string.luxo));
                break;
            case R.id.nav_item_site_livro:
                replaceFragment(new SiteLivroFragment());
                break;
            case R.id.nav_item_settings:
                toast("Clicou em configurações");
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
