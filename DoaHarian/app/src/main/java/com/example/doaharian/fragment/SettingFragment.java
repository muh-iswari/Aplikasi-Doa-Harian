package com.example.doaharian.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.doaharian.R;

public class SettingFragment extends Fragment {
    private LinearLayout ll_home, ll_favorite, ll_setting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_setting_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_home = view.findViewById(R.id.LL_Home);
        ll_favorite = view.findViewById(R.id.LL_Favorite);
        ll_setting = view.findViewById(R.id.LL_Setting);

        ll_home.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, homeFragment)
                    .addToBackStack(null)
                    .commit();
            updateActiveNavBar(ll_home);
        });

        ll_favorite.setOnClickListener(v -> {
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit();
            updateActiveNavBar(ll_favorite);
        });

        // Navigasi ke SettingFragment saat LinearLayout Setting diklik
        updateActiveNavBar(ll_setting);
    }

    private void updateActiveNavBar(LinearLayout activeLayout) {
        // Reset semua latar belakang
        ll_home.setBackgroundResource(R.color.white);
        ll_favorite.setBackgroundResource(R.color.white);
        ll_setting.setBackgroundResource(R.color.white);

        // Tetapkan latar belakang kustom jika fragmen ini yang aktif
        activeLayout.setBackgroundResource(R.drawable.custom);
    }
}
