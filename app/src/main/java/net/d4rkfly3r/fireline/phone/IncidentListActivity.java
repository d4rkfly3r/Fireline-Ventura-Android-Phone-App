package net.d4rkfly3r.fireline.phone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.d4rkfly3r.fireline.phone.dummy.IncidentContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * An activity representing a list of Incidents. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link IncidentDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class IncidentListActivity extends AppCompatActivity {

    private static final String TAG = "Fireline Incident List";
    private final SimpleItemRecyclerViewAdapter mAdapter = new SimpleItemRecyclerViewAdapter(IncidentContent.ITEMS);
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView mRecyclerView;
    private String mLastData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadIfActiveInternet();
            }
        });

        View recyclerView = findViewById(R.id.incident_list);
        assert recyclerView != null;
        mRecyclerView = (RecyclerView) recyclerView;
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            int mEvenBackground = Color.rgb(255, 255, 255);
            int mOddBackground = Color.rgb(250, 250, 250);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
//                view.setBackgroundResource(position % 2 == 0 ? mEvenBackground : mOddBackground);
                view.setBackgroundColor(position % 2 == 0 ? mEvenBackground : mOddBackground);
            }
        });

        setupRecyclerView(mRecyclerView);

        if (findViewById(R.id.incident_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mAdapter);
    }

    public void downloadIfActiveInternet() {
        if (isNetworkAvailable()) {
            System.out.println("Building connector...");
            final ConnectorUtil connectorUtil = new ConnectorUtil("http://fireline.ventura.org/data/fireline.json");
            connectorUtil.setFinishedCallback(new Runnable() {
                @Override
                public void run() {
                    IncidentListActivity.this.mLastData = connectorUtil.getReturnData().trim();
                    System.out.println("Downloaded...");

                    try {
                        System.out.println("Starting JSON Parse");
                        JSONArray incidentArray = new JSONArray(getLastData());
                        for (int index = 0; index < incidentArray.length(); index++) {
                            final JSONObject incident = incidentArray.getJSONObject(index);
                            System.out.println("Parsing: " + incident);
                            final String address = incident.getString("Address");
                            final String block = incident.getString("Block");
                            final String city = incident.getString("City");
                            final String comment = incident.getString("Comment");
                            final String incidentNumber = incident.getString("IncidentNumber");
                            final String incidentType = incident.getString("IncidentType");
                            final double latitude = incident.getDouble("Latitude");
                            final double longitude = incident.getDouble("Longitude");
                            final String responseDate = incident.getString("ResponseDate");
                            final String status = incident.getString("Status");
                            final String units = incident.getString("Units");
                            final IncidentContent.IncidentItem incidentItem = new IncidentContent.IncidentItem(address, block, city, comment, incidentNumber, incidentType, latitude, longitude, responseDate, status, units);
                            System.out.println(incidentItem);
                            IncidentContent.addItem(incidentItem);
                            mAdapter.notifyDataSetChanged();
//                            mAdapter.addItem(incidentItem);
//                            double latitude = incident.getDouble("Latitude");
//                            double longitude = incident.getDouble("Longitude");
//                            System.out.println(latitude + " | " + longitude);
                        }
                    } catch (JSONException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }

                }
            });
            connectorUtil.execute();
        } else {
            Log.d(TAG, "No network available!");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public String getLastData() {
        return mLastData;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<IncidentContent.IncidentItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<IncidentContent.IncidentItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.incident_list_content, parent, false);
            return new ViewHolder(view);
        }

        public void addItem(IncidentContent.IncidentItem incidentItem) {
            mValues.add(incidentItem);
            notifyItemInserted(mValues.indexOf(incidentItem));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final IncidentContent.IncidentItem incidentItem = mValues.get(position);
            holder.mItem = incidentItem;
            holder.mIdView.setText(incidentItem.incidentNumber);
            holder.mContentView.setText(String.format("%s, %s", incidentItem.address, incidentItem.city));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(IncidentDetailFragment.ARG_ITEM_ID, holder.mItem.incidentNumber);
                        IncidentDetailFragment fragment = new IncidentDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.incident_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, IncidentDetailActivity.class);
                        intent.putExtra(IncidentDetailFragment.ARG_ITEM_ID, holder.mItem.incidentNumber);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public IncidentContent.IncidentItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
