package com.video;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class RecordVideoThread extends Thread {

    public String streamURL;// 流地址 网上有自行百度
    public String filePath;// 文件路径
    public Integer id;// 案件id

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public void run() {
        System.out.println(streamURL);
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamURL);
        FFmpegFrameRecorder recorder = null;
        try {
            grabber.start();
            Frame frame = grabber.grabFrame();
            if (frame != null) {
                File outFile = new File(filePath);
                if (!outFile.isFile()) {
                    try {
                        outFile.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
                recorder = new FFmpegFrameRecorder(filePath, 128, 128, 1);
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// 直播流格式
                recorder.setFormat("flv");// 录制的视频格式
                recorder.setFrameRate(25);// 帧数
                //百度翻译的比特率，默认400000，但是我400000贼模糊，调成800000比较合适
                recorder.setVideoBitrate(800000);
                recorder.start();
                while ((frame != null)) {
                    recorder.record(frame);// 录制
                    frame = grabber.grabFrame();// 获取下一帧
                }
                recorder.record(frame);
                // 停止录制
                recorder.stop();
                grabber.stop();
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        } finally {
            if (null != grabber) {
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
            if (recorder != null) {
                try {
                    recorder.stop();
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        RecordVideoThread thread = new RecordVideoThread();
        thread.setFilePath("C:\\Users\\Administrator\\Videos\\maoer\\" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".flv");
        thread.setStreamURL("https://d1-missevan04.bilivideo.com/live-bvc/022167/maoer_7763752_140216322.flv?cdn=missevan04&oi=2028979231&pt=web&expires=1652940118&qn=10000&len=0&trid=99b692a852504e70cb0da3066afc20fe&sigparams=cdn,oi,pt,expires,qn,len,trid&sign=91e3e6edf7ddbe3b2e10c1e7ecd81f28");
        thread.start();
    }
}