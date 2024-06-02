package com.example.doaharian.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doaharian.ApiService;
import com.example.doaharian.Content;
import com.example.doaharian.ContentAdapter;
import com.example.doaharian.R;
import com.example.doaharian.RetrofitClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private LinearLayout ll_home, ll_favorite, ll_setting, ll_no_internet;
    private ProgressBar progressBar;
    private ExecutorService executorService;
    private Handler mainHandler;
    private Button retryButton;
    private ConnectivityManager connectivityManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_home = view.findViewById(R.id.LL_Home);
        ll_favorite = view.findViewById(R.id.LL_Favorite);
        ll_setting = view.findViewById(R.id.LL_Setting);
        progressBar = view.findViewById(R.id.progress_bar);
        retryButton = view.findViewById(R.id.retry_button);
        ll_no_internet = view.findViewById(R.id.LL_No_Internet);

        // Inisialisasi views dan apiService di sini
        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerView = view.findViewById(R.id.rv_doa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi ExecutorService dan Handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Inisialisasi ConnectivityManager
        connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Tampilkan atau sembunyikan ImageView dan Button berdasarkan status koneksi internet
        if (!isConnected()) {
            showNoInternetUI();
        } else {
            fetchContents();
        }

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

        ll_setting.setOnClickListener(v -> {
            SettingFragment settingFragment = new SettingFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, settingFragment)
                    .addToBackStack(null)
                    .commit();
            updateActiveNavBar(ll_setting);
        });

        // Tambahkan EditText untuk pencarian
        EditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contentAdapter != null) {
                    contentAdapter.filterByDoaPrefix(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateActiveNavBar(ll_home);
    }

    private boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void fetchContents() {
        if (apiService != null && recyclerView != null) {
            progressBar.setVisibility(View.VISIBLE);
            executorService.execute(() -> {
                Call<List<Content>> call = apiService.getContent();
                call.enqueue(new Callback<List<Content>>() {
                    @Override
                    public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                        mainHandler.post(() -> progressBar.setVisibility(View.GONE));
                        if (response.isSuccessful() && response.body() != null) {
                            List<Content> contents = response.body();
                            mainHandler.post(() -> {
                                contentAdapter = new ContentAdapter(contents, getContext(), null);
                                recyclerView.setAdapter(contentAdapter);
                            });
                        } else {
                            mainHandler.post(() -> showError("Error: " + response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Content>> call, Throwable t) {
                        mainHandler.post(() -> {
                            progressBar.setVisibility(View.GONE);
                            showError("Failure: " + t.getMessage());
                            showNoInternetUI(); // Tampilkan UI jika gagal mengambil data
                        });
                    }
                });
            });
        } else {
            showError("ApiService atau RecyclerView belum diinisialisasi");
        }
    }

    private void showNoInternetUI() {
        ll_no_internet.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        retryButton.setOnClickListener(v -> {
            if (isConnected()) {
                ll_no_internet.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                fetchContents();
            } else {
                Toast.makeText(getContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateActiveNavBar(LinearLayout activeLayout) {
        ll_home.setBackground(null); // Menghapus background yang sebelumnya diatur
        ll_favorite.setBackground(null);
        ll_setting.setBackground(null);

        // Set background custom jika fragment ini yang aktif
        if (activeLayout == ll_home) {
            ll_home.setBackgroundResource(R.drawable.custom);
        } else if (activeLayout == ll_favorite) {
            ll_favorite.setBackgroundResource(R.drawable.custom);
        } else if (activeLayout == ll_setting) {
            ll_setting.setBackgroundResource(R.drawable.custom);
        }
    }
}
