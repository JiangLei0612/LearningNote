package com.lei.learningnote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView = findViewById(R.id.iv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DataAdapter());
    }

    private class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {
        @Override
        public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TestActivity.this).inflate(R.layout.item,parent,false);
            return new DataViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DataViewHolder holder, int position) {
            holder.textView.setText(position+"");
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DataViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item);
        }
    }
}
