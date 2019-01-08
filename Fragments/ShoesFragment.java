package be.ehb.dig_x.ricardo.werkstuk_android.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import be.ehb.dig_x.ricardo.werkstuk_android.Adapters.ShoeAdapter;
import be.ehb.dig_x.ricardo.werkstuk_android.Database.AppDatabase;
import be.ehb.dig_x.ricardo.werkstuk_android.Database.ShoeEntry;
import be.ehb.dig_x.ricardo.werkstuk_android.Model.Shoe;
import be.ehb.dig_x.ricardo.werkstuk_android.R;


public class ShoesFragment extends Fragment {

    private AppDatabase mDb;
    private RecyclerView recyclerView;
    private ShoeAdapter shoeAdapter;
    private List<Shoe> shoeList;

    EditText searchbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_shoe, container, false);

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchbar = view.findViewById(R.id.search_bar);
        searchbar.setVisibility(View.GONE);

        shoeList = new ArrayList<>();
        shoeAdapter = new ShoeAdapter(getContext(),shoeList);
        recyclerView.setAdapter(shoeAdapter);
        
        readShoes();
/*

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchShoes(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

        return view;
    }

    /*private void searchShoes(String s) {
        shoeList.clear();
        shoeList = ((App)getContext().getApplicationContext()).getDaoSession().getShoeDao()
                .queryBuilder()
                .
        ;


    }*/

    private void readShoes() {

        System.out.println("In ReadShoes function");

        List<ShoeEntry> shoeEntryList = mDb.ShoeDao().loadAllShoes();
        shoeList.clear();
        for (ShoeEntry shoeEntry : shoeEntryList){
            System.out.println("SHOE: "+shoeEntry.getModelNr()+", "+shoeEntry.getModelName()+", "+shoeEntry.getRetailPrice());
            Shoe shoe = new Shoe(shoeEntry.getModelNr(),shoeEntry.getBrand(), shoeEntry.getModelName(), shoeEntry.getColorway(), shoeEntry.getRetailPrice(), shoeEntry.getImageUrl());

            shoeList.add(shoe);
        }

        shoeAdapter.notifyDataSetChanged();

    }

}
