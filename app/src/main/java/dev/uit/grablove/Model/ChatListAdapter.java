package dev.uit.grablove.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dev.uit.grablove.R;

/**
 * Created by PhiPoz on 14/11/2017.
 */

public class ChatListAdapter extends BaseAdapter {

    private ArrayList<Message> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");

    public ChatListAdapter(ArrayList<Message> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;

    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
            Message message = chatMessages.get(position);
            final ViewHolder1 holder1;
            final ViewHolder2 holder2;

            if (message.getUserType() == UserType.SELF) {
                if (convertView == null) {
                    v = LayoutInflater.from(context).inflate(R.layout.item_tab3_message_self, null, false);
                    holder1 = new ViewHolder1();
                    holder1.messageTextView = (TextView) v.findViewById(R.id.txtTab3MessageUser2);
                    holder1.tvTime = (TextView) v.findViewById(R.id.tvTimeMessageUser2);
                    holder1.tvTime.setText(SIMPLE_DATE_FORMAT.format(message.getTime()));
                    holder1.messageTextView.setText(message.getMsg());

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder1.tvTime.getVisibility() == View.GONE){
                                holder1.tvTime.setVisibility(View.VISIBLE);
                            }else
                                holder1.tvTime.setVisibility(View.GONE);
                        }
                    });

                    v.setTag(holder1);
                } else {
                    v = convertView;
                    holder1 = (ViewHolder1) v.getTag();
                    holder1.messageTextView.setText(message.getMsg());
                    holder1.tvTime.setText(SIMPLE_DATE_FORMAT.format(message.getTime()));
                }



        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.item_tab3_message_other, null, false);
                holder2 = new ViewHolder2();
              holder2.messageTextView = (TextView) v.findViewById(R.id.txtTab3MessageUser1);
                holder2.messageTextView.setText(message.getMsg());
                holder2.tvTime = (TextView) v.findViewById(R.id.tvTimeMessageUser1);
                holder2.tvTime.setText(SIMPLE_DATE_FORMAT.format(message.getTime()));

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder2.tvTime.getVisibility() == View.GONE){
                            holder2.tvTime.setVisibility(View.VISIBLE);
                        }else
                            holder2.tvTime.setVisibility(View.GONE);
                    }
                });

                v.setTag(holder2);

            } else {
                v = convertView;

                holder2 = (ViewHolder2) v.getTag();
                holder2.messageTextView.setText(message.getMsg());
                holder2.tvTime.setText(SIMPLE_DATE_FORMAT.format(message.getTime()));
            }

        }

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView tvTime;

    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView tvTime;

    }
}
