package com.android.navada.donit.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.navada.donit.R;
import com.android.navada.donit.activities.RequirementsEditorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddRequirementFragment extends Fragment{

    private ListView addReqListView;
    public static ArrayList<String> requirements;
    public static ArrayAdapter arrayAdapter;
    private Button addReqButton;
    private long reqCount=0,readReq=0;
    public AddRequirementFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_add_requirement, container, false);
        addReqListView = mView.findViewById(R.id.add_req_list_view);
        addReqButton = mView.findViewById(R.id.add_req_button);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requirements = new ArrayList<>();
        getFromDataBase();

    }

    public void getFromDataBase(){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Requirements").child(FirebaseAuth.getInstance().getUid());
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reqCount = dataSnapshot.getChildrenCount();
                for(DataSnapshot db : dataSnapshot.getChildren()){
                    requirements.add(db.getValue().toString());
                    readReq++;
                }
                if(readReq == reqCount){
                    enableList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void enableList(){
        readReq = 0;
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,requirements);
        addReqListView.setAdapter(arrayAdapter);
        addReqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),RequirementsEditorActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });
        addReqListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure ?")
                        .setMessage("Do you want to Delete this ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requirements.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                FirebaseDatabase.getInstance().getReference().child("Requirements").child(FirebaseAuth.getInstance().getUid()).setValue(requirements);

                                // save the whole list here
                                // need updating
                            }
                        }).setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
        addReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RequirementsEditorActivity.class);
                startActivity(intent);
            }
        });

    }


}
