package com.github.sewerina.giftroom.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.sewerina.giftroom.App;
import com.github.sewerina.giftroom.R;
import com.github.sewerina.giftroom.model.Gift;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GiftAdapter mAdapter;
    private static final String EXTRA_ROOM_ID = "roomId";
    private String mRoomId;

    private ItemTouchHelper.SimpleCallback mItemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.deleteItem(position);
        }
    };

    public static Intent newIntent(Context context, String roomId) {
        Intent intent = new Intent(context, RoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        if(getIntent() != null) {
            mRoomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        }

        mSwipeRefreshLayout = findViewById(R.id.refresher_gifts);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.load(mRoomId);
            }
        });

        mRecyclerView = findViewById(R.id.rv_gifts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GiftAdapter();
        mRecyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(mItemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mViewModel = ViewModelProviders.of(this, App.getViewModelFactory()).get(RoomViewModel.class);
        mViewModel.gifts().observe(this, mAdapter);

        mViewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                mSwipeRefreshLayout.setRefreshing(isLoading);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatingGiftDialogFragment.showDialog(getSupportFragmentManager());

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewModel.load(mRoomId);
        setTitle("Gifts for " + mViewModel.roomName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Gift Room: " + mViewModel.roomName());
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Gift Room: " + mViewModel.roomName() + "\n\n" + "http://giftroom.app/" + mRoomId);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.title_send_to)));
            return true;
        }

        if (id == R.id.action_delete) {
            mViewModel.deleteRoom();
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createGift(String name) {
        mViewModel.createGift(name);
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
        private Gift mRecentlyDeletedItem;
        private int mRecentlyDeletedItemPosition;

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

        public void deleteItem(int position) {
            mRecentlyDeletedItem = mGifts.get(position);
            mRecentlyDeletedItemPosition = position;
            mGifts.remove(position);
            notifyItemRemoved(position);
            showUndoSnackbar();
        }

        private void showUndoSnackbar() {
            Snackbar snackbar = Snackbar.make(RoomActivity.this.mRecyclerView, "Remove this gift from the list", Snackbar.LENGTH_LONG);
            snackbar.setAction("Cancel the deletion", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.undoDelete();
                }
            });
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    Log.d("Callback", "onDismissed: " + event);
                    if (event != 1) {
                        mViewModel.deleteGift(mRecentlyDeletedItem);
                    }
                }
            });
            snackbar.show();
        }

        public void undoDelete() {
            mGifts.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
            notifyItemInserted(mRecentlyDeletedItemPosition);
        }
    }

}
