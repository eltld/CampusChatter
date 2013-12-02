package entities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class ScreenshotAPI implements Screenshot{

	@Override
	public int takeScreenshot(Activity context) {
		// Some constants
		final String SCREENSHOTS_LOCATIONS = "screenshots/";

		// Get device dimmensions
		Display display = context.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		// Get root view
		View view = context.getWindow().getDecorView().findViewById(android.R.id.content);

		// Create the bitmap to use to draw the screenshot
		final Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
		final Canvas canvas = new Canvas(bitmap);

		// Get current theme to know which background to use
		final Activity activity = context;
		final Theme theme = activity.getTheme();
		final TypedArray ta = theme
		    .obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
		final int res = ta.getResourceId(0, 0);
		final Drawable background = activity.getResources().getDrawable(res);

		// Draw background
		background.draw(canvas);

		// Draw views
		view.draw(canvas);

		// Save the screenshot to the file system
		FileOutputStream fos = null;
		String loc = "img" + System.currentTimeMillis() + ".jpg";
		try {
			fos = context.openFileOutput(loc, Context.MODE_WORLD_READABLE);
		    if (fos != null) {
		        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)) {
		            Log.d(SCREENSHOTS_LOCATIONS, "Compress/Write failed");
		        }
		        fos.flush();
		        fos.close();
		    }

		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return 0;
	}

}
