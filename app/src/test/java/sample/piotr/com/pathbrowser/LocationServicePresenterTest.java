package sample.piotr.com.pathbrowser;

import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;

import sample.piotr.com.pathbrowser.dao.MyRoomDatabase;
import sample.piotr.com.pathbrowser.dao.PathDao;
import sample.piotr.com.pathbrowser.dao.PathRepository;
import sample.piotr.com.pathbrowser.model.ModelPath;
import sample.piotr.com.pathbrowser.model.ModelPoint;
import sample.piotr.com.pathbrowser.service.LocationPresenterContract;
import sample.piotr.com.pathbrowser.service.LocationServicePresenter;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by piotr on 19/07/17.
 */

public class LocationServicePresenterTest {

    private PathRepository pathRepository;
    private LocationServicePresenter listPresenter;
    @Mock
    LocationPresenterContract.View view;
    @Mock
    MyRoomDatabase myRoomDatabase;
    @Mock
    PathDao pathDao;
    @Mock
    Handler handler;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        //when handler.post then immadiatelly run Runnable.run
        when(handler.post(any(Runnable.class))).thenAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                ((Runnable) invocation.getArgument(0)).run();
                return null;
            }
        });

        // we return hardcoded id when saving model to database
        when(pathDao.insertPath(Mockito.any(ModelPath.class))).thenReturn((long) 1);
        // we return mock pathDao instead of real one
        when(myRoomDatabase.pathDao()).thenReturn(pathDao);

        //we create real PathRepository class to track all invocations
        pathRepository = new PathRepository(handler, myRoomDatabase, new Executor() {

            @Override
            public void execute(@NonNull Runnable command) {

                command.run();
            }
        });
        //we create real LocationServicePresenter instance to test it
        listPresenter = new LocationServicePresenter(pathRepository, view);
    }

    /***
     * We call onLocationFetched method 3 times in a loop.
     * After first invocation new object should be saved into database.
     * Each next invocation should just update object points list
     */
    @Test
    public void testSavingAndUpdatingPath() {

        ModelPath path = new ModelPath(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), new ArrayList<ModelPoint>());
        for (int i = 0; i < 3; i++) {
            Location location = new Location("");
            location.setLatitude(0);
            location.setLongitude(0);
            listPresenter.onLocationFetched(path, location);
        }
        verify(myRoomDatabase, times(3)).pathDao();
        verify(pathDao, times(1)).insertPath(path);
        verify(pathDao, times(2)).updatePath(path);
        verify(view, times(3)).onPathReady(path);
        assertTrue(path.getPoints().size() == 3);
    }
}
