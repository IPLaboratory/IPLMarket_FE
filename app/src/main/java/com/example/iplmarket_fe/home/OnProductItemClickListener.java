package com.example.iplmarket_fe.home;

import android.view.View;

public interface OnProductItemClickListener {
    public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position);
}