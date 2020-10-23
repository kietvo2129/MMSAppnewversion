package com.example.mmsapp.ui.home.Manufacturing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.ActualWO.HomeFragment;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ActualWOAdapter extends ArrayAdapter<ActualWOMaster> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public ActualWOAdapter(Context context, List<ActualWOMaster> objects) {
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        ActualWOMaster item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);

            // binding view parts to view holder
            viewHolder.name = cell.findViewById(R.id.name);
            viewHolder.date = cell.findViewById(R.id.date);
            viewHolder.defect = cell.findViewById(R.id.defect);
            viewHolder.target = cell.findViewById(R.id.target);
            viewHolder.product = cell.findViewById(R.id.product);
            viewHolder.item_vcd = cell.findViewById(R.id.item_vcd);
            viewHolder.actual = cell.findViewById(R.id.actual);
            viewHolder.title_percent = cell.findViewById(R.id.title_percent);
            viewHolder.recycview = cell.findViewById(R.id.recycview);
            viewHolder.title_percent2 = cell.findViewById(R.id.title_percent2);
            viewHolder.target2 = cell.findViewById(R.id.target2);
            viewHolder.defect2 = cell.findViewById(R.id.defect2);
            viewHolder.actual2 = cell.findViewById(R.id.actual2);
            viewHolder.name2 = cell.findViewById(R.id.name2);
            viewHolder.product2 = cell.findViewById(R.id.product2);

            viewHolder.detail = cell.findViewById(R.id.detail);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }



//        if (null == item)
//            return cell;

//        // bind data from selected element to view through view holder
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        viewHolder.name.setText(item.name);
        viewHolder.date.setText(item.date);
        viewHolder.defect.setText(formatter.format(item.defect));
        viewHolder.target.setText(formatter.format(item.target));
        viewHolder.product.setText(item.item_vcd);
       // viewHolder.item_vcd.setText(item.item_vcd);
        viewHolder.actual.setText(formatter.format(item.actual));
        viewHolder.title_percent.setText("0%");//viewHolder.title_percent.setText(item.actual / item.target +"%");
        viewHolder.name2.setText(item.name);
        viewHolder.defect2.setText(formatter.format(item.defect));
        viewHolder.target2.setText(formatter.format(item.target));
        viewHolder.actual2.setText(formatter.format(item.actual));
        viewHolder.title_percent2.setText("0%");//viewHolder.title_percent2.setText(item.actual / item.target +"%");
        viewHolder.product2.setText(item.item_vcd);



        final FoldingCell finalCell = cell;
        viewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerToggle(position);
                finalCell.toggle(true);
            }
        });

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }
    // View lookup cache
    private static class ViewHolder {
        TextView id_actual,name,date,defect,target,product,item_vcd,actual,title_percent;

        TextView title_percent2,defect2,target2,actual2,name2,product2;
        RecyclerView recycview;
        LinearLayout detail;

    }
}
