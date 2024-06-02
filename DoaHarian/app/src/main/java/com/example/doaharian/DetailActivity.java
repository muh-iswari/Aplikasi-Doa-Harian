package com.example.doaharian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String FAVORITES_KEY = "favorites";
    public static final String ACTION_FAVORITE_CHANGED = "com.example.doaharian.ACTION_FAVORITE_CHANGED";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView ayatTextView = findViewById(R.id.ayatTextView);
        TextView latinTextView = findViewById(R.id.latinTextView);
        TextView artinyaTextView = findViewById(R.id.artinyaTextView);
        LinearLayout favoriteButton = findViewById(R.id.favoriteButton);
        TextView favoriteText = findViewById(R.id.favoriteText);
        ImageView favoriteIcon = findViewById(R.id.favoriteIcon);

        sharedPreferences = getSharedPreferences("com.example.doaharian", MODE_PRIVATE);

        Intent intent = getIntent();
        String doa = intent.getStringExtra("doa");
        String ayat = intent.getStringExtra("ayat");
        String latin = intent.getStringExtra("latin");
        String artinya = intent.getStringExtra("artinya");

        titleTextView.setText(doa);
        ayatTextView.setText(ayat);
        latinTextView.setText(latin);
        artinyaTextView.setText(artinya);

        final boolean[] isFavorite = {isFavorite(doa, ayat, latin, artinya)};
        updateFavoriteButton(favoriteButton, favoriteText, favoriteIcon, isFavorite[0]);

        favoriteButton.setOnClickListener(v -> {
            if (isFavorite[0]) {
                removeFavorite(doa, ayat, latin, artinya);
                updateFavoriteButton(favoriteButton, favoriteText, favoriteIcon, false);
                sendFavoriteChangedBroadcast();
            } else {
                addFavorite(doa, ayat, latin, artinya);
                updateFavoriteButton(favoriteButton, favoriteText, favoriteIcon, true);
                sendFavoriteChangedBroadcast();
            }
            isFavorite[0] = !isFavorite[0];
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isFavorite(String doa, String ayat, String latin, String artinya) {
        Set<String> favoritesSet = sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>());
        return favoritesSet.contains(doa + "|" + ayat + "|" + latin + "|" + artinya);
    }

    private void addFavorite(String doa, String ayat, String latin, String artinya) {
        Set<String> favoritesSet = new HashSet<>(sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>()));
        favoritesSet.add(doa + "|" + ayat + "|" + latin + "|" + artinya);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITES_KEY, favoritesSet);
        editor.apply();

        Toast.makeText(this, "Berhasil ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(String doa, String ayat, String latin, String artinya) {
        Set<String> favoritesSet = new HashSet<>(sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<>()));
        favoritesSet.remove(doa + "|" + ayat + "|" + latin + "|" + artinya);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITES_KEY, favoritesSet);
        editor.apply();

        Toast.makeText(this, "Berhasil dihapus dari Favorit", Toast.LENGTH_SHORT).show();
    }

    private void updateFavoriteButton(LinearLayout favoriteButton, TextView favoriteText, ImageView favoriteIcon, boolean isFavorite) {
        if (isFavorite) {
            favoriteText.setText("Hapus dari Favorit");
            favoriteIcon.setImageResource(R.drawable.fav);
            favoriteButton.setBackgroundColor(getResources().getColor(R.color.dark_custom));
        } else {
            favoriteText.setText("Tambah ke Favorit");
            favoriteIcon.setImageResource(R.drawable.fav);
            favoriteButton.setBackgroundColor(getResources().getColor(R.color.custom));
        }
    }

    private void sendFavoriteChangedBroadcast() {
        Intent intent = new Intent(ACTION_FAVORITE_CHANGED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
