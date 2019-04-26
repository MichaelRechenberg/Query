package com.example.conwayying.query;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.SeekBar;

import com.example.conwayying.query.data.LectureSlidesPagerAdapter;

/**
 * Container of "lecture slides" from a directory of images named
 *  1.jpg, 2.jpg, 3.jpg etc in the res/ folder
 */
public class LectureSlideContainer {

    public static final String MOCK_LECTURE_ASSET_DIR = "mock-slides/";

    private ViewPager mViewPager;
    private SeekBar mSeekBar;
    private Context mContext;
    private FragmentManager mFragmentManager;



    private int mCurrentSlideNumber;


    /**
     * A wrapper object for the "lecture slides" (mocked as a series of locally stored
     *  jpg files named 0.jpg, 1.jpg, 2.jpg, ... etc.
     *
     *  Defaults to slide 0
     *
     * @param context Context for the application
     * @param fm FragmentManager of hosting Activity
     * @param viewPager ViewPager to render the slides onto and have swipe gestures for
     * @param seekBar The Seekbar to use for changing slides rapidly
     */
    public LectureSlideContainer(Context context, FragmentManager fm, ViewPager viewPager, SeekBar seekBar){
        this.mContext = context;
        this.mViewPager = viewPager;
        this.mSeekBar = seekBar;
        this.mFragmentManager = fm;

        this.mCurrentSlideNumber = 0;

        this.setupViewPager(mViewPager, this, fm);
        this.setupSeekBar(mSeekBar, this);
    }


    /**
     * @return The current slide number
     */
    public int getCurrentSlideNumber(){
        return this.mCurrentSlideNumber;
    }

    /**
     * Sets the current slide number (but doesn't redraw the image)
     * @param slideNumber The slide number to change to
     */
    public void setSlideNumber(int slideNumber){
        this.mCurrentSlideNumber = slideNumber;
    }

    /**
     * Redraws the slide based on the current slide number, updates seek bar
     */
    public void redrawSlide(){
        Log.d("LectureSlides", "Redrawing lecture slides to slide " + this.mCurrentSlideNumber);

        mViewPager.setCurrentItem(this.mCurrentSlideNumber, false);
        mSeekBar.setProgress(this.mCurrentSlideNumber);
    }

    private void setupSeekBar(SeekBar seekBar, final LectureSlideContainer lectureSlideContainer){
        // This 14 magic number is because our mock slide deck had 15 slides
        seekBar.setMax(14);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        lectureSlideContainer.setSlideNumber(0);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 1:
                        lectureSlideContainer.setSlideNumber(1);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 2:
                        lectureSlideContainer.setSlideNumber(2);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 3:
                        lectureSlideContainer.setSlideNumber(3);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 4:
                        lectureSlideContainer.setSlideNumber(4);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 5:
                        lectureSlideContainer.setSlideNumber(5);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 6:
                        lectureSlideContainer.setSlideNumber(6);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 7:
                        lectureSlideContainer.setSlideNumber(7);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 8:
                        lectureSlideContainer.setSlideNumber(8);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 9:
                        lectureSlideContainer.setSlideNumber(9);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 10:
                        lectureSlideContainer.setSlideNumber(10);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 11:
                        lectureSlideContainer.setSlideNumber(11);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 12:
                        lectureSlideContainer.setSlideNumber(12);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 13:
                        lectureSlideContainer.setSlideNumber(13);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 14:
                        lectureSlideContainer.setSlideNumber(14);
                        lectureSlideContainer.redrawSlide();
                        break;
                    case 15:
                        lectureSlideContainer.setSlideNumber(15);
                        lectureSlideContainer.redrawSlide();
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * Setup the ViewPager for the LectureSlideContainer so that both the mSlideNumber is updated
     *  whenever ths user scrolls through the slides and the mSeekBar is also updated to reflect
     *  the currently selected lecture slide
     * @param viewPager The ViewPager to setup
     * @param lectureSlideContainer this LectureSlideContainer
     * @param fm FragmentManager to use for adapter
     */
    private void setupViewPager(final ViewPager viewPager, final LectureSlideContainer lectureSlideContainer,
                                final FragmentManager fm){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Log.d("Foo", "OnPageScrolled " + position);
            }

            @Override
            public void onPageSelected(int position) {
                // Log.d("Foo", "OnPageSelected " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("Foo", "onPageScrollStateChanged");
                if (state == ViewPager.SCROLL_STATE_IDLE){
                    int slideNumber = viewPager.getCurrentItem();
                    Log.d("Foo", "slideNumber in onPageScrollStateChanged: " + slideNumber);
                    lectureSlideContainer.mCurrentSlideNumber = slideNumber;
                    lectureSlideContainer.mSeekBar.setProgress(lectureSlideContainer.mCurrentSlideNumber);

                }
            }
        });

        viewPager.setAdapter(new LectureSlidesPagerAdapter(fm));
    }
}


