package com.example.ehospital;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final String TAG = "ChatMessageAdapter";
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;
    MediaPlayer mediaPlayer;
    private List<MessageModel> mMessages;
    private Context mContext;
    FirebaseAuth firebaseAuth;

    public MessageAdapter(List<MessageModel> mMessages, Context mContext)
    {
        this.mMessages = mMessages;
        this.mContext = mContext;
        setHasStableIds(true);
    }
    @Override
    public long getItemId(int position) {


        return position;
    }
    @Override
    public int getItemViewType(int position) {
        MessageModel item = mMessages.get(position);
        if (item.sentby.equals("doctor")) {
            return MY_MESSAGE;
        }
        else
            return OTHER_MESSAGE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        System.out.println(i);
        if (i == MY_MESSAGE) {
            Fresco.initialize(mContext);
            ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.messageviewright, viewGroup, false));
            viewHolder.setIsRecyclable(false);
            return viewHolder ;
        } else {
            Fresco.initialize(mContext);
            ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.messageviewleft, viewGroup, false));
            viewHolder.setIsRecyclable(false);
            return viewHolder ;
        }
    }
    public void add(MessageModel message) {
        mMessages.add(message);
        notifyItemInserted(mMessages.size() - 1);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        MessageModel chatMessage = mMessages.get(i);
        if (chatMessage.type.equals("text")) {
            viewHolder.tvMessage.setText(mMessages.get(i).messagetext);
//            viewHolder.ivImage.setVisibility(View.VISIBLE);
//            viewHolder.tvMessage.setVisibility(View.GONE);
        } else if (chatMessage.type.equals("audio"))
        {
            mediaPlayer = MediaPlayer.create(viewHolder.itemView.getContext(), Uri.parse(mMessages.get(i).messagetext));
            viewHolder.tvMessage.setText("Audio Sent");
            viewHolder.playbutton.setVisibility(View.VISIBLE);
            //            viewHolder.ivImage.setVisibility(View.GONE);
//            viewHolder.tvMessage.setVisibility(View.VISIBLE);
//
//            viewHolder.tvMessage.setText(chatMessage.getContent());

            viewHolder.playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if(mediaPlayer.isPlaying())
                    {
                        viewHolder.playbutton.setImageResource(R.drawable.play);
                        mediaPlayer.pause();

                    }else {
                        viewHolder.playbutton.setImageResource(R.drawable.pause);
                        mediaPlayer.start();
                        mediaPlayer.setScreenOnWhilePlaying(true);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                viewHolder.playbutton.setImageResource(R.drawable.play);
                            }

                        });




                    }

                }

            });
        }
        else if (chatMessage.type.equals("photo"))
        {
            viewHolder.chatimage.setVisibility(View.VISIBLE);
            viewHolder.tvMessage.setVisibility(View.INVISIBLE);
            viewHolder.chatimage.setImageURI(mMessages.get(i).messagetext);

        }

        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        viewHolder.tvTime.setText(mMessages.get(i).timestamp);
        viewHolder.chatMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatMessage.type.equals("photo"))
                {
                    showImage(mMessages.get(i).messagetext);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessage, tvTime;
        SimpleDraweeView chatimage;
        ChatMessageView chatMessageView;
        ImageButton playbutton;
        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessageView = (ChatMessageView) itemView.findViewById(R.id.chatMessageView);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            chatimage =  itemView.findViewById(R.id.chatimage);
            playbutton=itemView.findViewById(R.id.playbutton);


        }
    }
    public void showImage(String url) {
        Dialog builder = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.R.style.Theme_Light));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new SimpleDraweeView(mContext);
        Glide.with(mContext).load(url).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mediaPlayer = null;
        }
    };
}
