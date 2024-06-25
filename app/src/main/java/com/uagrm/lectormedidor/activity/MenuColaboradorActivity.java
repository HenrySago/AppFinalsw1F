package com.uagrm.lectormedidor.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.fragment.AsignacionFragment;
import com.uagrm.lectormedidor.fragment.PerfilFragment;
import com.uagrm.lectormedidor.pojo.RegistrarDispositivoClientePojo;
import com.uagrm.lectormedidor.pojo.RegistrarDispositivoColaboradorPojo;
import com.uagrm.lectormedidor.retrofit.BaseUrl;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.CustomTypefaceSpan;
import com.uagrm.lectormedidor.util.Preferencia;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuColaboradorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Preferencia preferencia;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Typeface typeFaceSemiBold,typeFaceBold;
    private int MENU_ITEMS = 0;
    private ArrayList<View> mMenuItems;
    private TextView tv_nombre,tv_bar_titulo;
    private ImageView iv_foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_colaborador);
        preferencia = new Preferencia(this);
        typeFaceSemiBold = Typeface.createFromAsset(getAssets(), "fonts/MulishSemiBold.ttf");
        typeFaceBold = Typeface.createFromAsset(getAssets(), "fonts/MulishBold.ttf");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        changeToolbarFont(toolbar);
        tv_bar_titulo = findViewById(R.id.tv_bar_titulo);
        tv_bar_titulo.setTypeface(typeFaceBold);
        tv_bar_titulo.setText("Mis asignaciones");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_logo_nav_icon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        final Menu navMenu = navigationView.getMenu();
        MENU_ITEMS = navMenu.size();
        mMenuItems = new ArrayList<>(MENU_ITEMS);
        for (int i=0;i<navMenu.size();i++) {
            MenuItem mi = navMenu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }


        View headerLayout = navigationView.getHeaderView(0);
        iv_foto = headerLayout.findViewById(R.id.iv_foto);
        tv_nombre = headerLayout.findViewById(R.id.tv_nombre);
        tv_nombre.setTypeface(typeFaceBold);
        tv_nombre.setText(preferencia.getNombres()+" "+preferencia.getApellidos());


        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        registroDispositivo();
    }

    public void changeToolbarFont(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTypeface(typeFaceBold);
                    break;
                }
            }
        }
    }


    private void registroDispositivo() {
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("tokenMain", "" + token);

        if (!token.equals("")) {
            RegistrarDispositivoColaboradorPojo registrarDispositivoColaboradorPojo = new RegistrarDispositivoColaboradorPojo();
            registrarDispositivoColaboradorPojo.setIdColaborador(preferencia.getColaboradorId());
            registrarDispositivoColaboradorPojo.setTokenFirebase(token);
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<String>> call = service.registrarDispositivoColaborador(registrarDispositivoColaboradorPojo);
            call.enqueue(new Callback<Respuesta<String>>() {
                @Override
                public void onResponse(Call<Respuesta<String>> call, Response<Respuesta<String>> response) {
                    if (response.isSuccessful()) {
                        Log.i("registroDC", "" + token);

                    }
                }

                @Override
                public void onFailure(Call<Respuesta<String>> call, Throwable t) {

                }
            });
        }
    }

    private void cargarFotoPerfil() {
        Glide.with(getApplicationContext())
                .load(BaseUrl.baseUrlRecursos+preferencia.getFotoPerfil())
                .centerCrop().dontAnimate()
                .placeholder(R.drawable.ic_logo).into(iv_foto);
    }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int nFragment = getSupportFragmentManager().getBackStackEntryCount();
            Log.v("onBackPressed",nFragment+"");

            if(nFragment>1){
                getSupportFragmentManager().popBackStack();
            }else{
                new DialogDefault.Builder(this)
                        .setTitle("Salir de la aplicación")
                        .setMessage("¿Estás seguro de salir de la aplicación?")
                        .setNegativeBtnText("Cancelar")
                        .setPositiveBtnText("Aceptar")
                        .setAnimation(Animacion.POP)
                        .isCancellable(false)
                        .OnNegativeClicked(new DialogDefaultInterface() {
                            @Override
                            public void OnClick() {

                            }
                        })
                        .OnPositiveClicked(new DialogDefaultInterface() {
                            @Override
                            public void OnClick() {
                                finish();
                            }
                        })
                        .build();
            }
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_mis_asignaciones:
                tv_bar_titulo.setText("Mis asignaciones");
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                goFragment(new AsignacionFragment(), "asignacion");
                break;
            case R.id.nav_perfil:
                tv_bar_titulo.setText("Perfil");
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                goFragment(new PerfilFragment(), "perfil");
                break;
            case R.id.nav_cerrar_sesion:
                new DialogDefault.Builder(this)
                        .setTitle("¿Estás seguro de cerrar sesión?")
                        .setIcon(R.drawable.ic_alerta, Icon.Visible)
                        .setNegativeBtnText("Cancelar")
                        .setPositiveBtnText("Aceptar")
                        .setAnimation(Animacion.POP)
                        .isCancellable(false)
                        .OnNegativeClicked(new DialogDefaultInterface() {
                            @Override
                            public void OnClick() {

                            }
                        })
                        .OnPositiveClicked(new DialogDefaultInterface() {
                            @Override
                            public void OnClick() {
                                cerrarSesion();
                            }
                        })
                        .build();
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion() {
        preferencia.setLogeado(false);
        preferencia.clearDatosLogin();
        Intent ventana = new Intent(MenuColaboradorActivity.this, LoginActivity.class);
        startActivity(ventana);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        MenuColaboradorActivity.this.finish();
    }


    private void goFragment(Fragment fragment, String nombre) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.content, fragment, "fragmento").addToBackStack(nombre)
                .commit();

    }

    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , typeFaceBold), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    @Override
    protected void onStart() {
        super.onStart();
        cargarFotoPerfil();
    }


}
