package tellh.com.gitclub.presentation.view.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class EasySpinnerAdapter<T extends EasySpinnerAdapter.SpinnerEntity> implements SpinnerAdapter {

    private List<T> mList;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public EasySpinnerAdapter(Spinner spinner, onItemSelectedListener<T> onItemSelectedListener) {
        this(spinner, onItemSelectedListener, new ArrayList<T>());
    }

    public EasySpinnerAdapter(Spinner spinner, onItemSelectedListener<T> onItemSelectedListener, List<T> mList) {
        this.mList = mList;
        spinner.setOnItemSelectedListener(new OnSpinnerEntitySelectedListener(onItemSelectedListener));
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mList.get(position).createEntityView(position, parent);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mList.get(position).createEntityView(position, parent);
        return convertView;
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void addEntity(T entity) {
        mList.add(entity);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    public void addAll(List<T> entities) {
        mList.addAll(entities);
        notifyDataSetChanged();
    }

    public void refresh(List<T> entities) {
        mList.clear();
        mList.addAll(entities);
        notifyDataSetChanged();
    }

    public interface SpinnerEntity {
        View createEntityView(int position, ViewGroup parent);
    }

    public interface onItemSelectedListener<T extends SpinnerEntity> {
        void onItemSelected(T entity, int position);
    }

    public class OnSpinnerEntitySelectedListener implements AdapterView.OnItemSelectedListener {
        onItemSelectedListener<T> listener;

        protected OnSpinnerEntitySelectedListener(EasySpinnerAdapter.onItemSelectedListener<T> onItemSelectedListener) {
            this.listener = onItemSelectedListener;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            listener.onItemSelected(mList.get(position), position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}