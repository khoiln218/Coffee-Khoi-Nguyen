package com.khoinguyen.caphekhoinguyen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleSectionedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private RecyclerView.Adapter mBaseAdapter;
    private SparseArray<Section> mSections = new SparseArray<>();

    public SimpleSectionedAdapter(Context context, List<Section> sections, RecyclerView.Adapter baseAdapter) {

        mBaseAdapter = baseAdapter;
        mContext = context;
        setSections(sections);

        mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.section_item, parent, false);
            return new ViewHolder(view);
        } else {
            return mBaseAdapter.onCreateViewHolder(parent, typeView - 1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            ((ViewHolder) sectionViewHolder).title.setText(mSections.get(position).title);
        } else {
            mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position) ? SECTION_TYPE : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }

    private void setSections(List<Section> sections) {
        mSections.clear();

        Collections.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                return (o.firstPosition == o1.firstPosition) ? 0 : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position) ? Integer.MAX_VALUE - mSections.indexOfKey(position) : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
    }

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        String title;

        public Section(int firstPosition, long timeTamp) {
            this.firstPosition = firstPosition;
            this.title = getTitle(timeTamp);
        }

        private String getTitle(long timeTamp) {
            Calendar now = Calendar.getInstance();

            Calendar target = Calendar.getInstance();
            target.setTimeInMillis(timeTamp);

            if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR) && now.get(Calendar.MONTH) == target.get(Calendar.MONTH)
                    && now.get(Calendar.DAY_OF_MONTH) == target.get(Calendar.DAY_OF_MONTH))
                return "Hôm nay";
            if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR) && now.get(Calendar.MONTH) == target.get(Calendar.MONTH)
                    && now.get(Calendar.DAY_OF_MONTH) == target.get(Calendar.DAY_OF_MONTH) + 1)
                return "Hôm qua";
            return Utils.convTimestamp(timeTamp, "dd/MM/yyyy");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvDate);
        }
    }
}