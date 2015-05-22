package net.majorkernelpanic.spydroid.ui;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import net.majorkernelpanic.streaming.video.VideoStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static net.majorkernelpanic.spydroid.ui.SpydroidActivity.TAG;
import static net.majorkernelpanic.streaming.video.VideoStream.fileCount;
/**
 * Created by alex on 27/04/15.
 */
public class ProcessVideo extends Thread/*AsyncTask<Void, Integer, Void> */{

    public static boolean isstop=false;
    public static String path;
    @Override
   // protected Void doInBackground(Void... params) {

         public void run()
    {

        Process ffmpegProcess = null;

        try {

                //ffmpeg -r 10 -b 1800 -i %03d.jpg test1800.mp4
                // 00000
                // /data/data/com.mobvcasting.ffmpegcommandlinetest/ffmpeg -r p.getPreviewFrameRate() -b 1000 -i frame_%05d.jpg video.mov

                //String[] args2 = {"/data/data/com.mobvcasting.ffmpegcommandlinetest/ffmpeg", "-y", "-i", "/data/data/com.mobvcasting.ffmpegcommandlinetest/", "-vcodec", "copy", "-acodec", "copy", "-f", "flv", "rtmp://192.168.43.176/live/thestream"};
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                String[] ffmpegCommand = {"/data/data/net.majorkernelpanic.spydroid/ffmpeg", "-r", "" +  VideoStream.fps, "-vcodec", "mjpeg", "-i", path+"frame_%05d.jpg", Environment.getExternalStorageDirectory().getPath() + "/videos/video_" + ts + ".mov"};

                ffmpegProcess = new ProcessBuilder(ffmpegCommand).redirectErrorStream(true).start();


                OutputStream ffmpegOutStream = ffmpegProcess.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpegProcess.getInputStream()));

              try {
                    ffmpegProcess.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String line;

                Log.v(TAG, "***Starting FFMPEG***");
                while ((line = reader.readLine()) != null) {
                    Log.v(TAG, "***" + line + "***");
                }
                Log.v(TAG, "***Ending FFMPEG***");





        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
        }
       deleteFiles(path);

       // return null;
    }
    public void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }

    protected void onPostExecute(Void... result) {

    }
}


