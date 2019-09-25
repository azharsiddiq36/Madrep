package com.azhar.madrep.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.azhar.madrep.Activity.MainActivity;
import com.azhar.madrep.Activity.TambahKunjunganActivity;
import com.azhar.madrep.Adapter.KunjunganAdapter;
import com.azhar.madrep.Model.ResponseKunjungan;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class KunjunganFragment extends Fragment {
    @BindView(R.id.rv_History)
    RecyclerView rvHistory;
    SessionManager sessionManager;
    MadrepInterface madrepInterface;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    ProgressDialog progressDialog;
    RecyclerView.LayoutManager layoutManager;
    KunjunganAdapter kunjunganAdapter;
    ArrayList Kunjungan = new ArrayList<>();
    Context mContext;
    @BindView(R.id.btnAddKunjungan)
    Button btnAddKunjungan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kunjungan, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem !=null){
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView!=null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(kunjunganAdapter!=null){
                        kunjunganAdapter.getFilter().filter(newText);
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu,menuInflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android.support.v7.app.ActionBar actionBar =
                ((MainActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;actionBar.setTitle("Kunjungan");
        sessionManager = new SessionManager(getActivity());
        final HashMap<String, String> map = sessionManager.getDetailsLoggin();
        progressDialog = new ProgressDialog(getActivity());
        madrepInterface = CombineApi.getApiService();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(layoutManager);
        progressDialog.setMessage("Sedang Memuat Data ...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        if (map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES).equals("dokter")){
            btnAddKunjungan.setVisibility(View.INVISIBLE);

            Call<ResponseKunjungan> data = madrepInterface.getListKunjunganDokter(map.get(sessionManager.KEY_PENGGUNA_ID));
            data.enqueue(new Callback<ResponseKunjungan>() {
                @Override
                public void onResponse(Call<ResponseKunjungan> call, Response<ResponseKunjungan> response) {
                    progressDialog.hide();
                    if (response.body().getStatus() == 200){
                        kunjunganAdapter = null;
                        Kunjungan = response.body().getData();
                        kunjunganAdapter = new KunjunganAdapter(getActivity(),Kunjungan);
                        kunjunganAdapter.getFilter().filter("");
                        rvHistory.setAdapter(kunjunganAdapter);
                        kunjunganAdapter.notifyDataSetChanged();
                    }
                    else{
                        Log.d("kambing", "onResponse: "+map.get(sessionManager.KEY_PENGGUNA_ID));
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseKunjungan> call, Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES).equals("spv")){
            btnAddKunjungan.setVisibility(View.INVISIBLE);
            Call<ResponseKunjungan> data = madrepInterface.getListAllKunjungan();
            data.enqueue(new Callback<ResponseKunjungan>() {
                @Override
                public void onResponse(Call<ResponseKunjungan> call, Response<ResponseKunjungan> response) {
                    progressDialog.hide();
                    if (response.body().getStatus() == 200){
                        kunjunganAdapter = null;
                        Kunjungan = response.body().getData();
                        kunjunganAdapter = new KunjunganAdapter(getActivity(),Kunjungan);
                        kunjunganAdapter.getFilter().filter("");
                        rvHistory.setAdapter(kunjunganAdapter);
                        kunjunganAdapter.notifyDataSetChanged();
                    }
                    else{
                        Log.d("kambing", "onResponse: "+map.get(sessionManager.KEY_PENGGUNA_ID));
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseKunjungan> call, Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Call<ResponseKunjungan> data = madrepInterface.getListKunjungan(map.get(sessionManager.KEY_PENGGUNA_ID));
            data.enqueue(new Callback<ResponseKunjungan>() {
                @Override
                public void onResponse(Call<ResponseKunjungan> call, Response<ResponseKunjungan> response) {
                    progressDialog.hide();
                    if (response.body().getStatus() == 200){
                        kunjunganAdapter = null;
                        Kunjungan = response.body().getData();
                        kunjunganAdapter = new KunjunganAdapter(getActivity(),Kunjungan);
                        kunjunganAdapter.getFilter().filter("");
                        rvHistory.setAdapter(kunjunganAdapter);
                        kunjunganAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(getContext(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseKunjungan> call, Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btnAddKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),TambahKunjunganActivity.class);
                startActivity(i);
            }
        });

    }
}
