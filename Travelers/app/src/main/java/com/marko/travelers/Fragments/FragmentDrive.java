package com.marko.travelers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marko.travelers.Activities.TravelDriverActivity;
import com.marko.travelers.Adapters.RecyclerViewAdapter;
import com.marko.travelers.Model.Travel;
import com.marko.travelers.Model.User;
import com.marko.travelers.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrive extends Fragment implements RecyclerViewAdapter.OnItemClickListener{

    View v;
    private List<Travel> travelList;
    private User a = new User("Marko","Poposki", "MarkoOh",
            "Mirceacev27", R.drawable.slika, "markOh@mgmail.com", "072248680");
    public FragmentDrive () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.my_driver_travels_fragment, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.drive_recyclerview);
        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(getContext(), travelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewAdapter);
        viewAdapter.setOnItemClickListener(FragmentDrive.this);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        travelList = new ArrayList<>();
        travelList.add(new Travel(a,"Skopje","Ohrid","28/8/2018","19:00","3", 300, "mkd"));
        travelList.add(new Travel(a,"Viena","Skopje","28/8/2018","19:00","3", 1300, "mkd"));


    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), TravelDriverActivity.class);
        Travel clickedItem = travelList.get(position);

        intent.putExtra("Image", clickedItem.getDriver().getPhoto());
        intent.putExtra("UserName", clickedItem.getDriver().getUserName());
        intent.putExtra("From", clickedItem.getFtom());
        intent.putExtra("To", clickedItem.get_To());
        intent.putExtra("Date", clickedItem.get_Date());
        intent.putExtra("Time", clickedItem.getTime());
        intent.putExtra("FreeSeats", clickedItem.getFreeSeats());
        intent.putExtra("Phone", clickedItem.getDriver().getPhone());

        startActivity(intent);
    }

}
