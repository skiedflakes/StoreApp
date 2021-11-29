package com.example.store.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.store.R;
import com.example.store.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class History_adapter extends RecyclerView.Adapter<History_adapter.UsersViewHolder> {
    private ArrayList<History_model> dataList;
    private Context context;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SessionManager session;
    String user;
    private Fragment fragment;
    public History_adapter(Context context, ArrayList<History_model> dataList){
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.history_row, parent, false);

        session = new SessionManager(context.getApplicationContext());
        HashMap<String, String> user_account = session.getUserDetails();

        user = user_account.get(SessionManager.KEY_USERID);



        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder,final int position) {
        int pos = position;
        final String details = dataList.get(position).getDetails();

        holder.btn_users.setText(details);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        Button btn_users;
        UsersViewHolder(View itemView) {
            super(itemView);
            btn_users = itemView.findViewById(R.id.btn_users);

        }
    }




}
