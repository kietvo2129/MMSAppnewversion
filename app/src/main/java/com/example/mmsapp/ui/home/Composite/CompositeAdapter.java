package com.example.mmsapp.ui.home.Composite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CompositeAdapter extends RecyclerView.Adapter<CompositeAdapter.NoteVH> {
    private List<CompositeMaster> mNoteList;


    private CompositeAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(CompositeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public CompositeAdapter(List<CompositeMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_composite,
                parent, false);
        NoteVH evh = new NoteVH(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(CompositeMaster note) {
        if (!mNoteList.contains(note)) {
            mNoteList.add(note);
            notifyItemInserted(mNoteList.size());
        }
    }
    @Override
    public int getItemCount() {
        return mNoteList != null ? mNoteList.size() : 0;
    }
    class NoteVH extends RecyclerView.ViewHolder {

        TextView code,start_dt,end_dt,type,no,name;

        public NoteVH(View itemView, final CompositeAdapter.OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            code = itemView.findViewById(R.id.code);
            start_dt = itemView.findViewById(R.id.start_dt);
            end_dt = itemView.findViewById(R.id.end_dt);
            type = itemView.findViewById(R.id.type);
            no= itemView.findViewById(R.id.no);
            name= itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v,position);
                        }
                    }
                }
            });
        }


        public void bindData(CompositeMaster note) {
            code.setText(note.code);
            start_dt.setText(note.start_dt);
            end_dt.setText(note.end_dt);
            type.setText(note.type);
            no.setText(note.no);
            name.setText(note.name);
        }

        public void filterList(ArrayList<CompositeMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
