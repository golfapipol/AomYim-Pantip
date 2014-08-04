package com.nantaphop.pantipfanapp.utils;

import android.app.ActionBar;
import android.util.Log;
import android.widget.AbsListView;
import com.nantaphop.pantipfanapp.event.ForumScrollDownEvent;
import com.nantaphop.pantipfanapp.event.ForumScrollUpEvent;

/**
 * Created by nantaphop on 03-Aug-14.
 */
public class ScrollDirectionListener implements AbsListView.OnScrollListener {

    private int currentFVI = 0;
    private boolean isDown = false;
    private boolean isStop = true;

    private OnScrollUp onScrollUp;
    private OnScrollDown onScrollDown;
    private int startItem;

    public ScrollDirectionListener(OnScrollUp onScrollUp, OnScrollDown onScrollDown) {
        this.onScrollUp = onScrollUp;
        this.onScrollDown = onScrollDown;
    }

    public ScrollDirectionListener(int startItem, OnScrollUp onScrollUp, OnScrollDown onScrollDown) {
        Log.d("scroll", "init ScrollDirectionListener at "+ startItem);
        this.startItem = startItem;
        this.onScrollUp = onScrollUp;
        this.onScrollDown = onScrollDown;
        currentFVI = startItem;
    }

    public void resetScrollDirectionState() {
        isDown = false;
        currentFVI = 0;
    }

    public void setOnScrollUp(OnScrollUp onScrollUp) {
        this.onScrollUp = onScrollUp;
    }

    public void setOnScrollDown(OnScrollDown onScrollDown) {
        this.onScrollDown = onScrollDown;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int state) {
        if (state == SCROLL_STATE_IDLE) {
            isStop = true;

        }

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Avoid reinitial
        if( (currentFVI == startItem && firstVisibleItem ==0) ||
                (currentFVI == 0 && firstVisibleItem ==startItem)){
            return;
        }
        if (Math.abs(currentFVI - firstVisibleItem) >= 1) {
            if (currentFVI > firstVisibleItem) {
                if (isStop || isDown) {
                    Log.d("scroll", "scrollUp : " +currentFVI+" -> "+firstVisibleItem );
                    onScrollUp.onScrollUp();
                    isDown = false;
                }
            } else {
                if (isStop || !isDown) {
                    Log.d("scroll", "scrollDown : " +currentFVI+" -> "+firstVisibleItem );
                    onScrollDown.onScrollDown();
                    isDown = true;
                }
            }
            currentFVI = firstVisibleItem;
        }
        if (currentFVI != firstVisibleItem)
            isStop = false;
    }

    public interface OnScrollUp {
        public void onScrollUp();
    }

    public interface OnScrollDown {
        public void onScrollDown();
    }
}
