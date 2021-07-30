package com.arb.movieguideapp.listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.models.Movie;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private ClickListener clickListener;
    private GestureDetector gestureDetector;
    private Movie movie;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

        this.clickListener = clickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if(child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {

            clickListener.onClick(child, recyclerView.getChildPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    public interface ClickListener {

        void onClick(View view, int position);
    }
}
