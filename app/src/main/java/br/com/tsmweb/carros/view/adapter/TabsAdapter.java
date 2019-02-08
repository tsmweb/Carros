package br.com.tsmweb.carros.view.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.view.fragments.CarrosFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.classicos);
        } else if (position == 1) {
            return context.getString(R.string.esportivos);
        }

        return context.getString(R.string.luxo);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;

        if (position == 0) {
            f = CarrosFragment.newInstance(R.string.classicos);
        } else if (position == 1) {
            f = CarrosFragment.newInstance(R.string.esportivos);
        } else {
            f = CarrosFragment.newInstance(R.string.luxo);
        }

        return f;
    }

}
