package tellh.com.gitclub.presentation.view.fragment.home;

import android.os.Bundle;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.LazyFragment;

public class PersonalPageFragment extends LazyFragment {

    public PersonalPageFragment() {
        // Required empty public constructor
    }

    public static PersonalPageFragment newInstance() {
        PersonalPageFragment fragment = new PersonalPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_personal_page;
    }

    @Override
    public void initView() {

    }
}
