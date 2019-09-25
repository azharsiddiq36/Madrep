package com.azhar.madrep.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azhar.madrep.Activity.MainActivity;
import com.azhar.madrep.Adapter.DokterAdapter;
import com.azhar.madrep.Model.Dokter;
import com.azhar.madrep.Model.ResponseDokter;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DokterFragment extends Fragment {
    @BindView(R.id.rv_History)
    RecyclerView rvHistory;
    SessionManager sessionManager;
    MadrepInterface madrepInterface;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    ProgressDialog progressDialog;
    RecyclerView.LayoutManager layoutManager;
    DokterAdapter dokterAdapter;
    ArrayList dokter = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dokter, container, false);
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
                    if(dokterAdapter != null){
                        dokterAdapter.getFilter().filter(newText);
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
        assert actionBar != null;actionBar.setTitle("Dokter");
        sessionManager = new SessionManager(getActivity());
        final HashMap<String, String> map = sessionManager.getDetailsLoggin();
        progressDialog = new ProgressDialog(getActivity());
        madrepInterface = CombineApi.getApiService();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(layoutManager);
        progressDialog.setMessage("Sedang Memuat Data ...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<ResponseDokter> data = madrepInterface.getListDokter();
        data.enqueue(new Callback<ResponseDokter>() {
            @Override
            public void onResponse(Call<ResponseDokter> call, Response<ResponseDokter> response) {

                progressDialog.hide();
                dokterAdapter = null;
                dokter = response.body().getData();
                dokterAdapter = new DokterAdapter(getActivity(),dokter);
                dokterAdapter.getFilter().filter("");
                rvHistory.setAdapter(dokterAdapter);
                dokterAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseDokter> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
