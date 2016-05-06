package com.zfeng.imagechange;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by zfeng on 2016/4/28.
 * 图片美化类
 */
public class ImageHelper {
    /**
     * 调整图片三元素：色相/色调、饱合度、亮度
     * @param bm 图片,warning,不可直接修改
     * @param hue 色相、色调
     * @param saturation 饱合度
     * @param lum 亮度
     * @return
     */
    public static Bitmap handleImageEffect(Bitmap bm,float hue,float saturation,float lum)
    {
        //不能直接编辑bm
        Bitmap bmp=Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.ARGB_8888);
        //操作都是在Canvas上处理
        Canvas canvas=new Canvas(bmp);
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //调整色相、色调
        ColorMatrix hueMatrix=new ColorMatrix();
        hueMatrix.setRotate(0,hue);//Red color
        hueMatrix.setRotate(1,hue);//Green color
        hueMatrix.setRotate(2,hue);//blue color

        //设制饱合度
        ColorMatrix saturationMatrix=new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        //设制亮度
        ColorMatrix lumMatrix=new ColorMatrix();
        lumMatrix.setScale(lum,lum,lum,1);

        ColorMatrix imageMatrix=new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        //设制画笔
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        //绘制画布
        canvas.drawBitmap(bm,0,0,paint);
        return bmp;
    }

    /**
     * 通过修改像素点，生成"底片效果"
     * @param bm
     * @return
     */
    public static Bitmap changePixelsStyle1(Bitmap bm)
    {
        int r,g,b,a;
        int width=bm.getWidth();
        int height=bm.getHeight();
        Bitmap bmp=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        int[] oldPx=new int[width*height];
        int[] newPx=new int[width*height];
        bm.getPixels(oldPx,0,width,0,0,width,height);
        int color;
        for(int i=0;i<width*height;++i)
        {
            color=oldPx[i];
            r= Color.red(color);
            g=Color.green(color);
            b=Color.blue(color);
            a=Color.alpha(color);

            r=255-r;
            g=255-g;
            b=255-b;
            r=check(r);
            g=check(g);
            b=check(b);
            newPx[i]=Color.argb(a,r,g,b);
        }
        bmp.setPixels(newPx,0,width,0,0,width,height);
        return bmp;
    }

    /**
     *老照片效果
     * @param bm
     * @return
     */
    public static Bitmap handleImagePixelsOldPhoto(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0;
        int r, g, b, a, r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            a = Color.alpha(color);
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            if (r1 > 255) {
                r1 = 255;
            }
            if (g1 > 255) {
                g1 = 255;
            }
            if (b1 > 255) {
                b1 = 255;
            }

            newPx[i] = Color.argb(a, r1, g1, b1);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     *浮雕效果
     * @param bm
     * @return
     */
    public static Bitmap handleImagePixelsRelief(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0, colorBefore = 0;
        int a, r, g, b;
        int r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 1; i < width * height; i++) {
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);

            color = oldPx[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = (r - r1 + 127);
            g = (g - g1 + 127);
            b = (b - b1 + 127);
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }
    public static int check(int i)
    {
        if(i>255)
        {
            return 255;
        }else if(i<0)
        {
            return 0;
        }
        return i;
    }

    public static Bitmap handleImageNegative(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color;
        int r, g, b, a;

        Bitmap bmp = Bitmap.createBitmap(width, height
                , Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }
}
