package com.example.mmsapp.ui.home.Manufacturing;

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

public class ActualdetailAdapter extends RecyclerView.Adapter<ActualdetailAdapter.NoteVH> {
    private List<ActualWOdetailMaster> mNoteList;



    public ActualdetailAdapter(List<ActualWOdetailMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actual_wo_detail,
                parent, false);
        NoteVH evh = new NoteVH(v);
        return evh;
    }




    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(ActualWOdetailMaster note) {
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

        public TextView count_table2, bb_no, mt_cd, gr_qty,no;

        public NoteVH(View itemView) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            count_table2 = itemView.findViewById(R.id.count_table2);
            bb_no = itemView.findViewById(R.id.bb_no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            gr_qty = itemView.findViewById(R.id.gr_qty);
            no = itemView.findViewById(R.id.no);

        }


        public void bindData(ActualWOdetailMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            count_table2.setText(formatter.format(note.count_table2));
            bb_no.setText(note.bb_no);
            mt_cd.setText(note.mt_cd);
            no.setText(formatter.format(note.no));
            gr_qty.setText(formatter.format(note.gr_qty));
        }

        public void filterList(ArrayList<ActualWOdetailMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
