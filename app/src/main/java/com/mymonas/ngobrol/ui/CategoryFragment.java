package com.mymonas.ngobrol.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.CategoryCallback;
import com.mymonas.ngobrol.model.CategoryItem;
import com.mymonas.ngobrol.adapter.CategoryAdapter;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huteri on 10/21/2014.
 */
public class CategoryFragment extends Fragment {
    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        final ArrayList<CategoryItem> categoryList = new ArrayList<CategoryItem>();

        final CategoryAdapter adapter = new CategoryAdapter(getActivity(), categoryList);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.pBar);
        pBar.setVisibility(View.VISIBLE);
        RestClient.get().getCategories(new Callback<CategoryCallback>() {
            @Override
            public void success(CategoryCallback categoryCallback, Response response) {
                pBar.setVisibility(View.GONE);
                if(categoryCallback.getSuccess() == 1) {
                    categoryList.addAll(categoryCallback.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pBar.setVisibility(View.GONE);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CategoryThreadActivity.class);
                intent.putExtra(CategoryThreadActivity.KEY_EXTRA_CATEGORY_DATA, categoryList.get(i));
                startActivity(intent);
            }
        });
        return view;

    }
}
