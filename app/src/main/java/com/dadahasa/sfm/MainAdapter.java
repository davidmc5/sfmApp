package com.dadahasa.sfm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by David on 2/14/2018.
 */


/**
 * {@link MainAdapter} exposes a list of menu selections to the recyclerView
 * {@link android.support.v7.widget.RecyclerView}
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder> {

    private String[] mMenuLabels;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MainAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MainAdapterOnClickHandler {
        void onClick(String menuItem);
    }

    /**
     * Creates a MainAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MainAdapter(MainAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class MainAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mMenuTextView;

        public MainAdapterViewHolder(View view) {
            super(view);
            mMenuTextView = (TextView) view.findViewById(R.id.main_menu_item);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String menuItem = mMenuLabels[adapterPosition];
            mClickHandler.onClick(menuItem);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */


    @Override
    public MainAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.main_menu_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MainAdapterViewHolder(view);
    }



    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param mainAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(MainAdapterViewHolder mainAdapterViewHolder, int position) {
        String menuLabel = mMenuLabels[position];
        mainAdapterViewHolder.mMenuTextView.setText(menuLabel);
    }

    @Override
    public int getItemCount() {
        if (mMenuLabels == null) return 0;
        return mMenuLabels.length;
    }

    public void setMenuLabels(String[] menuLabels) {
        mMenuLabels = menuLabels;
        notifyDataSetChanged();

    }
}