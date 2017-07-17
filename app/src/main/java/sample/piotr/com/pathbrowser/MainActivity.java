package sample.piotr.com.pathbrowser;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

import dagger.android.AndroidInjection;
import sample.piotr.com.pathbrowser.list.FragmentList;
import sample.piotr.com.pathbrowser.map.FragmentMap;
import sample.piotr.com.pathbrowser.util.DetailsTransition;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (!isTablet()) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, new FragmentList()).commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.list_container, new FragmentList()).commitAllowingStateLoss();
                getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, new FragmentMap()).commitAllowingStateLoss();
            }
        }
    }

    public void replaceFragmentWithTransition(Fragment fragment, View view, String transitionName) {

        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragments_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            current.setExitTransition(new Fade());
            fragment.setAllowEnterTransitionOverlap(false);
            fragment.setAllowReturnTransitionOverlap(true);
            fragment.setSharedElementReturnTransition(new DetailsTransition());
        }
        getSupportFragmentManager().beginTransaction().addSharedElement(view, transitionName).replace(R.id.fragments_container, fragment).addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment, boolean backstack) {

        if (backstack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack(null).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commitAllowingStateLoss();
        }
    }

    public boolean isTablet() {

        return getResources().getBoolean(R.bool.isTablet);
    }
}
