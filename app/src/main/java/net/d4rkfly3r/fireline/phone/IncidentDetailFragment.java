package net.d4rkfly3r.fireline.phone;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.d4rkfly3r.fireline.phone.dummy.IncidentContent;

/**
 * A fragment representing a single Incident detail screen.
 * This fragment is either contained in a {@link IncidentListActivity}
 * in two-pane mode (on tablets) or a {@link IncidentDetailActivity}
 * on handsets.
 */
public class IncidentDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private IncidentContent.IncidentItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IncidentDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            System.out.println("SHIZZLE: " + getArguments().getString(ARG_ITEM_ID));
            mItem = IncidentContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            System.out.println("MITEM: " + mItem);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.address + ", " + mItem.city);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.incident_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.incident_detail)).setText(String.format("%s\n%s\n%s", mItem.incidentType, mItem.responseDate, mItem.status));
        }

        return rootView;
    }
}
