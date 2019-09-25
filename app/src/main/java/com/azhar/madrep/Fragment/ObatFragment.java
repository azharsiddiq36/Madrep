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
import com.azhar.madrep.Activity.TambahObatActivity;
import com.azhar.madrep.Adapter.ObatAdapter;
import com.azhar.madrep.Model.ResponseObat;
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


public class ObatFragment extends Fragment {
    @BindView(R.id.rv_History)
    RecyclerView rvHistory;
    SessionManager sessionManager;
    MadrepInterface madrepInterface;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    ProgressDialog progressDialog;
    RecyclerView.LayoutManager layoutManager;
    ObatAdapter ObatAdapter;
    ArrayList Obat = new ArrayList<>();
    @BindView(R.id.btnAddObat)
    Button btnAddObat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obat, container, false);
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
                    if(ObatAdapter!=null){
                        ObatAdapter.getFilter().filter(newText);
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
        assert actionBar != null;actionBar.setTitle("Obat");
        sessionManager = new SessionManager(getActivity());
        final HashMap<String, String> map = sessionManager.getDetailsLoggin();
        progressDialog = new ProgressDialog(getActivity());
        madrepInterface = CombineApi.getApiService();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(layoutManager);
        if (map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES).equals("madrep")){
            btnAddObat.setVisibility(View.INVISIBLE);
        }
        btnAddObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),TambahObatActivity.class);
                startActivity(i);
            }
        });
        progressDialog.setMessage("Sedang Memuat Data ...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<ResponseObat> data = madrepInterface.getListObat();
        data.enqueue(new Callback<ResponseObat>() {
            @Override
            public void onResponse(Call<ResponseObat> call, Response<ResponseObat> response) {
                progressDialog.hide();
                ObatAdapter = null;
                Obat = response.body().getData();
                ObatAdapter = new ObatAdapter(getActivity(),Obat);
                ObatAdapter.getFilter().filter("");
                rvHistory.setAdapter(ObatAdapter);
                ObatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseObat> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
