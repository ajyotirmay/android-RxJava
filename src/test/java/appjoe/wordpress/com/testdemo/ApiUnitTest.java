package appjoe.wordpress.com.testdemo;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;

@Config(constants = BuildConfig.class, sdk = 23,
manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class ApiUnitTest {
    private MainActivity mainActivity;

    @Mock
    private Retrofit mockRetrofitApiImpl;

    @Mock
    ApiService apiService;

    @Captor
    private ArgumentCaptor<Callback<List<String>>> callbackArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        mainActivity = controller.get();

        // Then we need to swap the retrofit api impl. with a mock one
        // I usually store my Retrofit api impl as a static singleton in class RestClient, hence:
//        RestClient.setApi(mockRetrofitApiImpl);

        controller.create();
    }
}
