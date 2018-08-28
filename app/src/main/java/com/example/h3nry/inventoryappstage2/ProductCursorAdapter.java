package com.example.h3nry.inventoryappstage2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h3nry.inventoryappstage2.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.product_quantity);
        Button saleButton = (Button) view.findViewById(R.id.product_sale);

        final int productIdColumnIndex = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);

        productNameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        saleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri productUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productIdColumnIndex);
                adjustProductQuantity(context, productUri, Integer.parseInt(productQuantity));

            }

        });

    }

    private void adjustProductQuantity(Context context, Uri productUri, int currentQuantity) {

        int newQuantity = (currentQuantity >= 1) ? currentQuantity - 1 : 0;

        if (currentQuantity == 0) {

            Toast.makeText(context.getApplicationContext(), R.string.cannot_subtract, Toast.LENGTH_SHORT).show();

        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);

        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);
        if (numRowsUpdated > 0) {

            Log.i(LOG_TAG, context.getString(R.string.editor_update_product_successful));

        } else {

            Toast.makeText(context.getApplicationContext(), R.string.cannot_subtract, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, context.getString(R.string.editor_update_product_failed));

        }

    }

}
