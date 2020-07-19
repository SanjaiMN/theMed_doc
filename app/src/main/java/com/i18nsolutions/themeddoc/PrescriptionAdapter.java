package com.i18nsolutions.themeddoc;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;


public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.Viewholder> {
    List<PrescriptionDetails> list;
    Context context;

    public PrescriptionAdapter(List<PrescriptionDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Fresco.initialize(context);

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescriptionviewitem,parent,false);

        return new PrescriptionAdapter.Viewholder(view);
    }

    public PrescriptionAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ControllerListener listener = new BaseControllerListener<ImageInfo>() {

            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                //Action on final image load
                holder.progressBar2.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(String id, Throwable throwable) {
                //Action on failure
            }

        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(list.get(position).url)
                .setControllerListener(listener)
                .build();
        holder.pdfimageview.setController(controller);
        holder.doctorname.setText("sent by\n");
        holder.doctorname.append(list.get(position).doctorname);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage(list.get(position).url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    { SimpleDraweeView pdfimageview;
    TextView doctorname;
        Context context;
        //ExplosionField explosionField;
        ProgressBar progressBar2;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            pdfimageview=itemView.findViewById(R.id.pdfimageview);
            progressBar2=itemView.findViewById(R.id.progressBar2);
            doctorname=itemView.findViewById(R.id.dnameprescription);
            //explosionField=ExplosionField.attach2Window((Activity) context);
        }
    }
    public void showImage(String url) {
        Dialog builder = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new SimpleDraweeView(context);
        Glide.with(context).load(url).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
