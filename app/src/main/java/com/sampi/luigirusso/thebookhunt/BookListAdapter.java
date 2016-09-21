package com.sampi.luigirusso.thebookhunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sampi.luigirusso.thebookhunt.entities.Book;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luigirusso on 17/03/16.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private String dataFromActivity;

    public void setDataFromActivity(String dataFromActivity) {
        this.dataFromActivity = dataFromActivity;
    }
    // Provide a direct reference to each of the views within a data item

    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView bookCover;
        public TextView bookTitle;
        public TextView bookAuthor;
        public Book selectedBook;
        //public Button messageButton;      /* TODO: insert other list here, the list of books */

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView, final String dataFromActivity) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            bookCover = (ImageView) itemView.findViewById(R.id.bookImage);
            bookTitle = (TextView) itemView.findViewById(R.id.bookTitle);
            bookAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);

            itemView.setOnClickListener(new View.OnClickListener(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    v.setTransitionName("mainToolbarImg");
                    Intent intent = new Intent(v.getContext(), BookDetail2Activity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity)v.getContext(),bookCover,"mainToolbarImg");
                    intent.putExtra("titleSelected",bookTitle.getText().toString());
                    intent.putExtra("authorSelected",bookAuthor.getText().toString());
                    intent.putExtra("coverSelected",bookCover.getTag().toString());
                    intent.putExtra("bookToDisplay",selectedBook.toString());
                    intent.putExtra("user",dataFromActivity);
                    /*TODO: for now it's fine this two putextra. Maybe in future you can use a complex object, stringify it and send*/
                    v.getContext().startActivity(intent,options.toBundle());
                    //v.getContext().startActivity(intent);

                }
            });
        }
    }

    private List <BookListItem> tableList;
    private ArrayList<Book> tableListBooks;
    private Context currentContext;

    // Pass in the book array into the constructor
    public BookListAdapter(List<BookListItem> tableItems) {
        tableList = tableItems;
    }

    public BookListAdapter(ArrayList<Book> books){ tableListBooks = books; }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        currentContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.book_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, dataFromActivity);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(BookListAdapter.ViewHolder viewHolder, int position) {
        /* Get the data model based on position
        BookListItem item = tableList.get(position);

        // Set item views based on the data model
        TextView textTitle = viewHolder.bookTitle;
        textTitle.setText(item.getTitle());
        TextView textAuthor = viewHolder.bookAuthor;
        textAuthor.setText(item.getAuthor()); OLD */

        //SYNCRONOUS EXECUTION
        // Get the data model based on position
        Book item = tableListBooks.get(position);

        // Set item views based on the data model
        TextView textTitle = viewHolder.bookTitle;
        textTitle.setText(item.getTitle());
        TextView textAuthor = viewHolder.bookAuthor;
        textAuthor.setText(item.getAuthor());
        ImageView imageBook = viewHolder.bookCover;
        //imageBook.setImageResource(item.getImage());
        imageBook.setTag(item.getImage());
        viewHolder.selectedBook = item;

        //ASYNC

        loadBitmap(item.getImage(),imageBook, currentContext);


    }

    public void loadBitmap(int resId, ImageView imageView, Context context) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView,context);
        task.execute(resId);
    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return tableListBooks.size();
    }


    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView, Context context) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }


        public  int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        public  Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                             int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }
        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(currentContext.getResources(), data, 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
