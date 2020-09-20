package com.kloso.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kloso.capstoneproject.data.model.Transaction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RemoteWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory(this.getApplicationContext());
    }

    class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context context;
        List<Transaction> collection = new ArrayList<>();
        String currencySymbol;

        public RemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            initData();
        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return collection.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.owing_list_item);
            Transaction transaction = collection.get(position);
            views.setTextViewText(R.id.tv_payer_name, transaction.getPayer().getName());
            views.setTextViewText(R.id.tv_receiver_name, transaction.getReceiver().getName());
            views.setTextViewText(R.id.tv_owing_amount, transaction.getBalanceBigDecimal().abs().toString() + " " + currencySymbol);

            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.tv_payer_name, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void initData() {
            collection.clear();
            Gson gson = new Gson();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String json = preferences.getString("transactions", "");
            this.currencySymbol = preferences.getString("currencySymbol", "");
            Type type = new TypeToken<ArrayList<Transaction>>() {
            }.getType();
            collection = gson.fromJson(json, type);
        }
    }
}


