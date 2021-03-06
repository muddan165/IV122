package IV122;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author Daniel Mudrik (433655)
 */
public class CV7 {
    private static int width = 1000;
    private static int height = 1000;
    
    public static void main(String args[]) {
        ImgV.folderName = "CV7_Images//";
        ImgB.folderName = "CV7_Images//";
        
        //Newton fractals
        ImgB.init(width, height, "NewtonFractal");
            newton(-3,-3,6);
        ImgB.save();
        //Mandelbrot
        ImgB.init(width, height, "Mandelbrot");
            mandelbrot(-2.5,-2,4,4,0,0);
        ImgB.save();
        //Julia
        ImgB.init(width, height, "JuliaSet");
            julia(-2,-2,4,4,-0.13,0.75);
        ImgB.save();
        /*ImgB.folderName = "CV7_Images//MandelbrotGIF//";
        
        double x = -0.6912;
        double y = 0.38735;
        double last = 0.0002;
        //BufferedImage[] imgs = new BufferedImage[120];
        for (int i = 0; i < 120; i++) {
            ImgB.init(width, height, "MandelBrot_" + i);
            mandelbrot(x,y,1 - i/(double)130,1-i/(double)130,0,0);
            ImgB.save();
        }*/
        
    }
    
    public static void julia(double sX, double sY, double sizeX, double sizeY, double cX, double cY) {
        double oldX = 0;
        double addX = sizeX/(double)width;
        double addY = sizeY/(double)height;
        int maxIters = 50;
        for (double x = sX; x < sX+sizeX; x+=addX) {
            for (double y = sY; y < sY+sizeY; y+=addY) {
                double zX = x;
                double zY = y;
                int i = 0;
                double sum = 0;
                double distance = 0;
                for (i = 0; i < maxIters; i++) {
                    oldX = zX*zX-zY*zY+cX;
                    zY = 2*zX*zY+cY;
                    zX = oldX;
                    
                    distance = Math.sqrt(zX*zX+zY*zY);
                    if (distance > 2) {
                        break;
                    }
                    sum += distance;
                }
                sum = Math.min(2,sum/(double)maxIters);
                
                if (i == maxIters) {
                    ImgB.putPixel((int)((x-sX)/addX), (int)((y-sY)/addY),0,(int)(127*sum),255);
                } else {
                    ImgB.putPixel((int)((x-sX)/addX), (int)((y-sY)/addY),0,(int)(255 * i/maxIters),0);
                }
            }
        }
    }
    
    public static void mandelbrot(double sX, double sY, double sizeX, double sizeY, double zX, double zY) {
        double x,y,oldX = 0;
        double addX = sizeX/(double)width;
        double addY = sizeY/(double)height;
        int maxIters = 30;
        for (double cX = sX; cX < sX+sizeX; cX+=addX) {
            for (double cY = sY; cY < sY+sizeY; cY+=addY) {
                x = zX;
                y = zY;
                int i = 0;
                double sum = 0;
                double distance = 0;
                for (i = 0; i < maxIters; i++) {
                    oldX = x*x-y*y+cX;
                    y = 2*x*y+cY;
                    x = oldX;
                    
                    distance = Math.sqrt(x*x+y*y);
                    if (distance > 2) {
                        break;
                    }
                    sum += distance;
                }
                sum = Math.min(2,sum/(double)maxIters);
                
                if (i == maxIters) {
                    ImgB.putPixel((int)((cX-sX)/addX), (int)((cY-sY)/addY),0,(int)(127*sum),255);
                } else {
                    ImgB.putPixel((int)((cX-sX)/addX), (int)((cY-sY)/addY),0,(int)(255 * i/maxIters),0);
                }
                
            }
        }
        
        
    }
    
    public static void newton(double sX, double sY, double size) {
        double dist1 = 0,distPlus = 0,distMinus = 0, minDist = 10;
        
        double addX = size/(double)width;
        double addY = size/(double)height;
        for (double x = sX; x < sX+size; x+=addX) {
            for (double y = sY; y < sY+size; y+=addY) {
                ComplexNum c = new ComplexNum(x, y);
                int r = 0, g = 0, b = 0;
                for (int i = 0; i < 20; i++) {
                    c = c.subtract(c.pow(3).subtract(new ComplexNum(1,0)).divide(c.pow(2).multiply(3)));
                    //compute distance to each of the 3 possible end points
                    //then get the first index at which the approximation is good enough = how quickly it converges
                    dist1 = Math.min(1.99,c.distance(new ComplexNum(1,0)));
                    if (dist1 < 0.01 && r == 0) {
                        r = i;
                    }
                    distPlus = Math.min(1.99,c.distance(new ComplexNum(-0.5,-Math.sqrt(3)/2)));
                    if (distPlus < 0.01 && g == 0) {
                        g = i;
                    }
                    distMinus = Math.min(1.99,c.distance(new ComplexNum(-0.5,Math.sqrt(3)/2)));
                    if (distMinus < 0.01 && b == 0) {
                        b = i;
                    }
                    //minDist = (dist1 < distPlus) ? ((dist1 < distMinus) ? dist1 : distMinus) : ((distPlus < distMinus) ? distPlus : distMinus);
                    
                }
                //1/20 * speed of convergence * 255 * 1/2 * (2-distance from convergence point)
                ImgB.putPixel((int)((x-sX)/addX), (int)((y-sY)/addY), (int)(0.05*(20-r)*0.5*255*(2-dist1)), (int)(0.05*(20-g)*0.5*255*(2-distPlus)), (int)(0.05*(20-b)*0.5*255*(2-distMinus)));
            }
        }
    }
}
