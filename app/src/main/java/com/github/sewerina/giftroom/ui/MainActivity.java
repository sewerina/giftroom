package com.github.sewerina.giftroom.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.sewerina.giftroom.App;
import com.github.sewerina.giftroom.R;
import com.github.sewerina.giftroom.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RoomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_rooms);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RoomAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this, App.getViewModelFactory()).get(MainViewModel.class);
        mViewModel.rooms().observe(this, mAdapter);

        mViewModel.load();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatingRoomDialogFragment.showDialog(getSupportFragmentManager());

//                Snackbar.make(view, "Новая комната добавлена!", Snackbar.LENGTH_SHORT)
//                        .setAction(null, null).show();
            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            Uri data = intent.getData();
            if (data != null) {
                final String id = data.getPath().substring(1);
                mViewModel.joinRoom(id, new Room.JoinRoomCallback() {
                    @Override
                    public void call() {
                        startActivity(RoomActivity.newIntent(MainActivity.this, id));
                    }
                });
                Toast.makeText(this, action +": "+ id, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, action, Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createRoom(String name) {
        mViewModel.createRoom(name);
    }

    class RoomHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;
        private Room mRoom;

        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(RoomActivity.newIntent(v.getContext(), mRoom.id()));
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

        public void bind(Room room) {
            mRoom = room;
            mNameTv.setText(room.name());
        }
    }

    class RoomAdapter extends RecyclerView.Adapter<RoomHolder> implements Observer<Iterable<Room>> {
        private final List<Room> mRooms = new ArrayList<>();

        @NonNull
        @Override
        public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            return new RoomHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
            Room room = mRooms.get(position);
            holder.bind(room);
        }

        @Override
        public int getItemCount() {
            return mRooms.size();
        }

        @Override
        public void onChanged(Iterable<Room> rooms) {
            mRooms.clear();
            mRooms.addAll((Collection<? extends Room>) rooms);
            notifyDataSetChanged();
        }
    }


}
