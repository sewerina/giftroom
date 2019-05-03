package com.github.sewerina.giftroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoomActivity extends AppCompatActivity {
    private RoomViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private GiftAdapter mAdapter;
    private static final String EXTRA_ROOM_ID = "roomId";
    private String mRoomId;

    public static Intent newIntent(Context context, String roomId) {
        Intent intent = new Intent(context, RoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        setTitle("Gifts");

        if(getIntent() != null) {
            mRoomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        }

        mRecyclerView = findViewById(R.id.rv_gifts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GiftAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this, App.getViewModelFactory()).get(RoomViewModel.class);
        mViewModel.gifts().observe(this, mAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewModel.load(mRoomId);
    }

    class GiftHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;

        public GiftHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mNameTv = (TextView) itemView;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            float scale = getResources().getDisplayMetrics().density;
            // convert the DP into pixel
            int pixel =  (int)(8 * scale + 0.5f);
            params.bottomMargin = pixel;
            mNameTv.setLayoutParams(params);
        }

        public void bind(Gift gift) {
            mNameTv.setText(gift.name());
        }
    }

    class GiftAdapter extends RecyclerView.Adapter<GiftHolder> implements Observer<Iterable<Gift>> {
        private final List<Gift> mGifts = new ArrayList<>();

        @NonNull
        @Override
        public GiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            return new GiftHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull GiftHolder holder, int position) {
            Gift gift = mGifts.get(position);
            holder.bind(gift);
        }

        @Override
        public int getItemCount() {
            return mGifts.size();
        }

        @Override
        public void onChanged(Iterable<Gift> gifts) {
            mGifts.clear();
            mGifts.addAll((Collection<? extends Gift>) gifts);
            notifyDataSetChanged();
        }
    }

}
