package de.ptkapps.gorillas.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import de.ptkapps.gorillas.ActionResolver;
import de.ptkapps.gorillas.main.Gorillas;

public class AndroidLauncher extends AndroidApplication implements GameHelperListener, ActionResolver {

    private static final String AD_UNIT_ID = "ca-app-pub-6849139528319389/3242683354";

    protected AdView adView;
    protected View gameView;

    private RelativeLayout layout;

    private GameHelper gameHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        AdView admobView = createAdView();
        layout.addView(admobView);
        View gameView = createGameView(cfg);
        layout.addView(gameView);
        setContentView(layout);
        startAdvertising(admobView);

        // TODO test fix keyboard push view up bug
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // make libGDX loose the EGL context to test switching to the home menu
        // and then resuming the app on devices, which do not preserve the EGL
        // context doing that (all devices < android 3.0)
        // try {
        // gameView.getClass()
        // .getMethod("setPreserveEGLContextOnPause", boolean.class)
        // .invoke(gameView, false);
        // } catch (IllegalAccessException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IllegalArgumentException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (InvocationTargetException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (NoSuchMethodException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        if (gameHelper == null) {
            gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
            gameHelper.enableDebugLog(true);
        }
        gameHelper.setup(this);
    }

    private AdView createAdView() {

        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setId(12345); // this is an arbitrary id, allows for relative
                             // positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
    }

    private View createGameView(AndroidApplicationConfiguration cfg) {

        gameView = initializeForView(new Gorillas(this), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ABOVE, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
    }

    private void startAdvertising(AdView adView) {

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("001a33036fd0af").addTestDevice("HT1CKTRA0050").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        gameHelper.onActivityResult(request, response, data);
    }

    @Override
    public boolean getSignedInGPGS() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void loginGPGS() {
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (final Exception ex) {
        }
    }

    @Override
    public void getAchievementsGPGS() {

        if (gameHelper.isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
        } else if (!gameHelper.isConnecting()) {
            loginGPGS();
        }
    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {
        Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void showOrLoadInterstital() {
    }
}
