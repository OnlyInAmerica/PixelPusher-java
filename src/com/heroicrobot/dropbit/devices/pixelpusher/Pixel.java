package com.heroicrobot.dropbit.devices.pixelpusher;

public class Pixel {

  public byte red;
  public byte green;
  public byte blue;
  public byte orange;
  public byte white;

  public Pixel() {
    red = 0;
    green = 0;
    blue = 0;
    orange = 0;
    white = 0;
  }
  
  
  // Processing "color" objects only support the axes of red, green and blue.
  public void setColor(int color) {
    this.blue = (byte)(color & 0xff);
    this.green = (byte) ((color >> 8) & 0xff);
    this.red = (byte) ((color >> 16) & 0xff);
    this.orange = (byte) 0;
    this.white = (byte) 0;
  }
  
  public Pixel(int color) {
    this.setColor(color);
  }
  
  public void setColor(Pixel pixel) {
    this.red = pixel.red;
    this.blue = pixel.blue;
    this.green = pixel.green;
    this.orange = pixel.orange;
    this.white = pixel.white;
  }

  public Pixel(Pixel pixel) {
    this.setColor(pixel);
  }

  public Pixel(byte red, byte green, byte blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.orange = 0;
    this.white = 0;
  }

  public Pixel(byte red, byte green, byte blue, byte orange, byte white) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.orange = orange;
    this.white = white;
  }
  
}
