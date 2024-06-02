package com.example.doaharian.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doaharian.Content;
import com.example.doaharian.ContentAdapter;
import com.example.doaharian.DetailActivity;
import com.example.doaharian.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private SharedPreferences sharedPreferences;
    private static final String FAVORITES_KEY = "favorites";
    private LinearLayout llHome, llFavorite, llSetting;
    private BroadcastReceiver favoriteChangedReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_favorite_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_doa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = getActivity().getSharedPreferences("com.example.doaharian", Context.MODE_PRIVATE);

        List<Content> favorites = getFavorites();
        contentAdapter = new ContentAdapter(favorites, getContext(), this::removeFavorite);
        recyclerView.setAdapter(contentAdapter);

        llHome = view.findViewById(R.id.LL_Home);
        llFavorite = view.findViewById(R.id.LL_Favorite);
        llSetting = view.findViewById(R.id.LL_Setting);

        llHome.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, homeFragment)
                    .commit();
            updateActiveNavBar(llHome);
        });

        llSetting.setOnClickListener(v -> {
            SettingFragment settingFragment = new SettingFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, settingFragment)
                    .commit();
            updateActiveNavBar(llSetting);
        });

        updateActiveNavBar(llFavorite);

        favoriteChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DetailActivity.ACTION_FAVORITE_CHANGED)) {
                    contentAdapter.updateData(getFavorites());
                }
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(favoriteChangedReceiver,
                new IntentFilter(DetailActivity.ACTION_FAVORITE_CHANGED));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(favoriteChangedReceiver);
    }

    private List<Content> getFavorites() {
        Set<String> favoritesSet = sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>());
        List<Content> favorites = new ArrayList<>();
        for (String favorite : favoritesSet) {
            String[] parts = favorite.split("\\|");
            if (parts.length == 4) {
                favorites.add(new Content(parts[0], parts[1], parts[2], parts[3]));
            }
        }
        return favorites;
    }

    private void removeFavorite(Content content) {
        Set<String> favoritesSet = sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>());
        favoritesSet.remove(content.getDoa() + "|" + content.getAyat() + "|" + content.getLatin() + "|" + content.getArtinya());
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favoritesSet).apply();
        contentAdapter.updateData(getFavorites());
    }

    private void updateActiveNavBar(LinearLayout activeLayout) {
        llHome.setBackground(null);
        llFavorite.setBackground(null);
        llSetting.setBackground(null);

        activeLayout.setBackgroundResource(R.drawable.custom);
    }
}
