package com.example.conwayying.query;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.IOException;
import java.io.InputStream;

/**
 * Container of "lecture slides" from a directory of images named
 *  1.jpg, 2.jpg, 3.jpg etc in the res/ folder
 */
public class LectureSlideContainer {

    private static String MOCK_LECTURE_ASSET_DIR = "mock-slides/";

    private ImageView mImageView;
    private SeekBar mSeekBar;
    private Context mContext;
    private int currentSlideNumber;

    /**
     * A wrapper object for the "lecture slides" (mocked as a series of locally stored
     *  jpg files named 1.jpg, 2.jpg... etc.
     *
     *  Defaults to slide 1
     *
     * @param context Context for the application
     * @param imageView ImageView to render the slides onto
     * @param seekBar The Seekbar to use for changing slides rapidly
     */
    public LectureSlideContainer(Context context, ImageView imageView, SeekBar seekBar){
        this.mContext = context;
        this.mImageView = imageView;
        this.mSeekBar = seekBar;

        this.setupSeekBar(seekBar, this);

        this.currentSlideNumber = 1;
    }

    /**
     * @return The current slide number
     */
    public int getCurrentSlideNumber(){
        return this.currentSlideNumber;
    }

    /**
     * Sets the current slide number (but doesn't redraw the image)
     * @param slideNumber The slide number to change to
     */
    public void setSlideNumber(int slideNumber){
        this.currentSlideNumber = slideNumber;
    }

    /**
     * Redraws the slide based on the current slide number, updates seek bar
     */
    public void redrawSlide(){
        Log.d("LectureSlides", "Redrawing lecture slides to slide " + this.currentSlideNumber);
        InputStream inputStream = null;

        try {
            String slideFilePath = MOCK_LECTURE_ASSET_DIR + "slide" + this.currentSlideNumber + ".jpg";
            inputStream = this.mContext.getAssets().open(slideFilePath);
            Drawable slideDrawable = Drawable.createFromStream(inputStream, null);
            this.mImageView.setImageDrawable(slideDrawable);
            inputStream.close();

            mSeekBar.setProgress(this.currentSlideNumber);

        } catch (IOException ex){
            Log.e("LectureSlides","Could not find slide file " + ex.toString());
        }
    }

    private void setupSeekBar(SeekBar seekBar, final LectureSlideContainer lectureSlideContainer){
        // This 15 magic number is because our mock slide deck had 16 slides
        seekBar.setMax(15);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    // setMin() is only for API 26, so as a hack case 0 maps to the first slide (slide 1)
                    case 0:
                        lectureSlideContainer.setSlideNumber(1);
                        lectureSlideContainer.redrawSlide();
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
}
