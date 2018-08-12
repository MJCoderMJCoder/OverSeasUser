package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.main.tab.fragment.activity.NewAudioImageActivity;
import com.ltt.overseasuser.model.AttachmentFileBean;

import java.util.List;

/**
 * Reusable adapters
 */
public abstract class ReusableAdapter<T> extends BaseAdapter {
    private List<T> listData;
    private int listItemResource; // id

    public ReusableAdapter(List<T> listData, int listItemResource) {
        this.listData = listData;
        this.listItemResource = listItemResource;
    }

    public int getCount() {
        // If listDara is not empty, then listdata.size is returned;Otherwise return 0
        return (listData != null) ? listData.size() : 0;
    }

    public Object getItem(int position) {
        return listData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * View AnimalAdapter.getView(int position, View convertView, ViewGroup
     * parent) So the convertView is actually the cache object that the system gives us for our View to take
     * <p>
     * You call getView as many times as you have columns.
     */
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView,
                parent, listItemResource, position);
        bindView(holder, (T) getItem(position));
        return holder.getItemView();
    }

    /**
     * Define an abstract method to bind the ViewHolder to the relevant dataset
     * <p>
     * Implement this method when we create a new BaseAdapter, and don't forget to change our custom BaseAdapter to abstact abstract!
     */
    public abstract void bindView(ViewHolder holder, T obj);

    /**
     * ViewHolder Function：
     * <p>
     * 1. findViewById，Set control state;
     * <p>
     * 2. Define a method to find the control. The idea is to expose the public method, pass the control id when calling the method, and set the content, such as TextView setting text:
     * public ViewHolder setText(int id, CharSequence text){The text is set}
     * <p>
     * 3. Bring the part of convertView reuse to here, and you'll need to pass a context object, and we'll write everything we need to get to the constructor!
     * <p>
     * 4. Write a set of Settings (public), such as text size color, image background and so on!
     */
    public static class ViewHolder {
        // Stores the View in the item of the ListView
        private SparseArray<View> viewsOflistViewItem;
        private View storeConvertView; // Stores convertView
        private int position; // location
        private Context context; // Context

        // Constructor to complete correlation initialization
        private ViewHolder(Context context, ViewGroup parent,
                           int listItemResource) {
            // Stores the View in the item of the ListView
            viewsOflistViewItem = new SparseArray<View>();
            this.context = context;
            // View android.view.LayoutInflater.inflate(int resource, ViewGroup
            // root, boolean attachToRoot)【LayoutInflater】
            View convertView = LayoutInflater.from(context).inflate(
                    listItemResource, parent, false);
            convertView.setTag(this);
            storeConvertView = convertView; // Stores convertView
        }

        // bind ViewHolder and item
        public static ViewHolder bind(Context context, View convertView,
                                      ViewGroup parent, int listItemResource, int position) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(context, parent, listItemResource);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.storeConvertView = convertView;
            }
            holder.position = position;
            return holder;
        }

        // Gets the control saved in the collection according to id
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            T t = (T) viewsOflistViewItem.get(id);
            if (t == null) {
                t = (T) storeConvertView.findViewById(id);
                viewsOflistViewItem.put(id, t);
            }
            return t;
        }

        // Then we define a bunch of exposed methods
        // Get current entry
        public View getItemView() {
            return storeConvertView;
        }

        //Get the item location
        public int getItemPosition() {
            return position;
        }

        // Set the text
        public ViewHolder setText(int id, CharSequence text) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        // Set text alignment
        public ViewHolder setTVGravity(int id, int gravity) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setGravity(gravity);
            }
            return this;
        }

        // Set the text color
        public ViewHolder setTextColor(int id, int color) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            }
            return this;
        }

        // Set the picture
        public ViewHolder setImageResource(int id, int drawableRes) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }

        // Set click listening
        public ViewHolder setOnClickListener(int id,
                                             View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
            return this;
        }

        // Set the visible
        public ViewHolder setVisibility(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        // Set up the label
        public ViewHolder setTag(int id, Object obj) {
            getView(id).setTag(obj);
            return this;
        }

        /**
         * MyRequestDetailActivity Special methods. Display images, audio, PDF documents, etc.
         *
         * @param id
         * @param activity
         * @param attachmentFileList
         * @param mlflater
         * @return
         */
        public ViewHolder showAttachment(int id, final Activity activity, List<AttachmentFileBean> attachmentFileList, LayoutInflater mlflater) {
            LinearLayout ly_iamge = (LinearLayout) getView(id);
            ly_iamge.removeAllViews();
            for (final AttachmentFileBean attachmentfile : attachmentFileList) {
                Log.v("lzf", "attachmentfile：" + attachmentfile.getFile_path());
                if (attachmentfile.getFile_type().equals("image/png") || attachmentfile.getFile_type().equals("image/jpeg")) {
                    ImageView imageView = new ImageView(activity);
                    ly_iamge.addView(imageView);
                    Glide.with(activity).load(attachmentfile.getFile_path())
                            .placeholder(R.mipmap.loading)
                            .error(R.mipmap.icon_close)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                } else if (attachmentfile.getFile_type().equals("application/pdf")) {
                    View pdfView = mlflater.inflate(R.layout.detailpdflayout, null);
                    pdfView.setPadding(10, 0, 10, 0);
                    TextView tv_fileName = pdfView.findViewById(R.id.tv_title);
                    tv_fileName.setText(attachmentfile.getFile_name());
                    ly_iamge.addView(pdfView);
                    ImageView downPfd = pdfView.findViewById(R.id.download);
                    downPfd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(attachmentfile.getFile_path())));
                        }
                    });
                } else if (attachmentfile.getFile_type().equals("audio/mp3") || attachmentfile.getFile_type().equals("audio/wav")
                        || attachmentfile.getFile_type().equals("application/octet-stream")) {
                    View voiceView = mlflater.inflate(R.layout.detailvoicelayout, null);
                    voiceView.setPadding(10, 0, 10, 0);
                    NewAudioImageActivity audioObject = new NewAudioImageActivity(voiceView, attachmentfile.getFile_path(), activity);
                    TextView tv_tittle = voiceView.findViewById(R.id.tv_title);
                    tv_tittle.setText(attachmentfile.getFile_name());
                    ly_iamge.addView(voiceView);
                }
            }
            return this;
        }

        // Other methods can be extended by themselves
    }

    /**
     * <p>
     * Delete all data
     * <p>
     * public void clear() {
     * <p>
     * if (listData != null) {
     * <p>
     * listData.clear();
     * <p>
     * }
     * <p>
     * notifyDataSetChanged();
     * <p>
     * }
     */

    public void add(T data) {
        listData.add(data);
        notifyDataSetChanged();
    }

    public void delete(T data) {
        listData.remove(data);
        notifyDataSetChanged();
    }
}
