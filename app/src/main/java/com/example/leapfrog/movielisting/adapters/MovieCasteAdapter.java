package com.example.leapfrog.movielisting.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leapfrog.movielisting.R;
import com.example.leapfrog.movielisting.helper.RetrofitClient;
import com.example.leapfrog.movielisting.objects.Cast;


import java.util.ArrayList;
import java.util.List;


public class MovieCasteAdapter extends RecyclerView.Adapter<MovieCasteAdapter.ItemHolder> {


    private List<Cast> movies;
    private MovieCasteAdapter.OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    public Context _mcontext;

    public MovieCasteAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        movies = new ArrayList<>();
        this._mcontext = context;
    }

    public MovieCasteAdapter(Context context, List<Cast> movies) {
        layoutInflater = LayoutInflater.from(context);
        movies.clear();
        this.movies = movies;
    }

    @Override
    public MovieCasteAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemCardView = (View) layoutInflater.inflate(R.layout.caste, parent, false);
        return new MovieCasteAdapter.ItemHolder(itemCardView, this);
    }

    @Override
    public void onBindViewHolder(MovieCasteAdapter.ItemHolder holder, int position) {

        Cast tbl = movies.get(position);
        holder.populateView(tbl, _mcontext);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnItemClickListener(MovieCasteAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public MovieCasteAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(MovieCasteAdapter.ItemHolder item, int position);
    }

    public void add(int location, Cast iName) {
        movies.add(location, iName);
        notifyItemInserted(location);
    }

    public void addAll(List<Cast> tbllist) {
        movies.clear();
        movies = tbllist;
        notifyDataSetChanged();
    }

    public void remove(int location) {
        if (location >= movies.size())
            return;

        movies.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private MovieCasteAdapter parent;
        ImageView ivCasteProfilePic;
        TextView tvCasteName;

        public View view;


        public ItemHolder(View cView, MovieCasteAdapter parent) {
            super(cView);
            view = cView;
            view.setOnClickListener(this);
            this.parent = parent;

            ivCasteProfilePic = (ImageView) view.findViewById(R.id.ivCasteProfilePic);
            tvCasteName = (TextView) view.findViewById(R.id.tvCasteName);


        }

        public void populateView(Cast moviecaste, Context context) {

            tvCasteName.setText(moviecaste.getName());


            Glide.with(context)
                    .load(RetrofitClient.IMG_URL + moviecaste.getProfilePath()) // Image URL
                    .centerCrop() // Image scale type
                    .crossFade()
//                    .override(800, 500) // Resize image
                    .placeholder(R.mipmap.ic_launcher) // Place holder image
                    .error(R.mipmap.ic_launcher) // On error image
                    .into(ivCasteProfilePic); // ImageView to display image


        }


        @Override
        public void onClick(View v) {
            final MovieCasteAdapter.OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getPosition());
            }
        }
    }


    public void update(List<Cast> movieList) {
        movies.clear();
        for (Cast model : movieList) {
            movies.add(model);
        }
        notifyDataSetChanged();
    }

    public Cast getMovie(int posiiton) {
        return movies.get(posiiton);
    }


}
