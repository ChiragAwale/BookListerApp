package com.chiragawale.booklistingapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chira on 6/23/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0,books);
    }


    /*
    Sets the data for each List Item and returns the list_item for inflating the ListView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View listItemView = convertView;
        if(listItemView==null){
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item, parent, false);
            }

            Book currentBook = getItem(position);

        TextView ratingTextView = (TextView) listItemView.findViewById(R.id.rating);
        ratingTextView.setText( Double.toString(currentBook.getRating()));

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.book_name);
        nameTextView.setText(currentBook.getName());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        authorTextView.setText(currentBook.getAuthors());

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.price);
        priceTextView.setText(Double.toString(currentBook.getPrice()));

        return listItemView;
    }
}
