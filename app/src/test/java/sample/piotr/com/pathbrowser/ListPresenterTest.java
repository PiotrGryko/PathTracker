package sample.piotr.com.pathbrowser;

import android.os.Handler;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import sample.piotr.com.pathbrowser.dao.MyRoomDatabase;
import sample.piotr.com.pathbrowser.dao.PathDao;
import sample.piotr.com.pathbrowser.dao.PathRepository;
import sample.piotr.com.pathbrowser.list.ListPresenter;
import sample.piotr.com.pathbrowser.list.ListPresenterContract;
import sample.piotr.com.pathbrowser.model.ModelPath;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by piotr on 19/07/17.
 */
@RunWith(JUnit4.class)
public class ListPresenterTest {

    private PathRepository pathRepository;
    private ListPresenter listPresenter;
    @Mock
    ListPresenterContract.View view;
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

        when(pathDao.loadAllPaths()).thenReturn(new ArrayList<ModelPath>());
        when(myRoomDatabase.pathDao()).thenReturn(pathDao);

        pathRepository = new PathRepository(handler, myRoomDatabase, new Executor() {

            @Override
            public void execute(@NonNull Runnable command) {

                command.run();
            }
        });
        listPresenter = new ListPresenter(view, pathRepository);
    }

    @Test
    public void testLoadingPaths() {

        listPresenter.loadPaths();
        verify(myRoomDatabase).pathDao();
        verify(pathDao).loadAllPaths();
        verify(view).onPathsLoaded(Mockito.<ModelPath>anyList());
    }
}
