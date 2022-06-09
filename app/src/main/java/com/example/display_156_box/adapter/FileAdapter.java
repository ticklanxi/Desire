package com.example.display_156_box.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.display_156_box.R;

import java.io.File;
import java.util.List;

public
/**
 * @author ...
 * @date 2021-08-13 12:57
 * description：
 */
class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private List<File> files;
    private OnItemClickListener listener;


    public FileAdapter(List<File> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = files.get(position);
        holder.fileName.setText(file.getName());
//        holder.bb.setOnClickListener(new View.OnClickListener() {
//                                         @Override
//                                         public void onClick(View v) {
//
//                                         }
//                                     }
//
//        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!= null) {
                    listener.onItemClick(file);
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener!= null) {
                    listener.onItemLongClick(file);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return files != null ? files.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        Button bb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.tv_fileName);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(File file);
        void onItemLongClick(File file);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
