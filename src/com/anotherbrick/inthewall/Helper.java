package com.anotherbrick.inthewall;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import processing.core.PApplet;

public class Helper {

  public static int hex2Rgb(String colorStr, PApplet p) {
    int r = Integer.valueOf(colorStr.substring(1, 3), 16);
    int g = Integer.valueOf(colorStr.substring(3, 5), 16);
    int b = Integer.valueOf(colorStr.substring(5, 7), 16);
    return p.color(r, g, b);
  }

  public static int[] toInt(String[] values) {
    int[] out = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      out[i] = Integer.parseInt(values[i]);
    }
    return out;
  }

  public static float costrain(float value, float minValue, float maxValue) {
    return Math.min(Math.max(value, minValue), maxValue);
  }

  public static ArrayList<String> wordWrapOld(String s, int maxWidth, PApplet p) {
    ArrayList<String> a = new ArrayList<String>();
    float w = 0; // Accumulate width of chars
    int i = 0; // Count through chars
    int rememberSpace = 0; // Remember where the last space was
    // As long as we are not at the end of the String
    while (i < s.length()) {
      // Current char
      char c = s.charAt(i);
      w += p.textWidth(c); // accumulate width
      if (c == ' ') rememberSpace = i; // Are we a blank space?
      if (w > maxWidth) { // Have we reached the end of a line?
        String sub = s.substring(0, rememberSpace); // Make a substring
        // Chop off space at beginning
        if (sub.length() > 0 && sub.charAt(0) == ' ') {
          sub = sub.substring(1, sub.length());
        }
        // Add substring to the list
        a.add(sub);
        // Reset everything
        s = s.substring(rememberSpace, s.length());
        i = 0;
        w = 0;
      } else {
        i++; // Keep going!
      }
    }
    // Take care of the last remaining line
    if (s.length() > 0 && s.charAt(0) == ' ') {
      s = s.substring(1, s.length());
    }
    a.add(s);
    return a;
  }

  public static ArrayList<String> wordWrap(String s, int maxWidth, PApplet p) {
    ArrayList<String> out = new ArrayList<String>();
    String[] words = s.split(" ");
    float width = 0;
    int line = 0;
    out.add("");
    for (String w : words) {
      width += p.textWidth(w + " ") / Config.getInstance().multiply;
      if (width > maxWidth) {
        line++;
        out.add("");
        width = p.textWidth(w + " ") / Config.getInstance().multiply;
      }
      out.set(line, out.get(line) + w + " ");
    }
    return out;
  }

  public static String formatMoneyValue(Integer value) {
    if (value > 1000 && value < 1000000) {
      value /= 1000;
      return value.toString() + "K";
    } else if (value >= 1000000) {
      value /= 1000000;
      return value.toString() + "M";
    }

    return value.toString();
  }

  public static String floatToString(float number, int decimalValues, boolean separateThousands) {
    return String.format("%" + (separateThousands ? "," : "") + "." + decimalValues + "f", number);
  }

  public static String limitStringLength(String s, int length, boolean dots) {
    if (s.length() > length) {
      if (dots)
        return s.substring(0, length - 3) + "�";
      else
        return s.substring(0, length - 1);
    } else
      return s;
  }

  public static String readFile(String path) throws IOException {
    FileInputStream stream = new FileInputStream(new File(path));
    try {
      FileChannel fc = stream.getChannel();
      MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      /* Instead of using default, pass in a decoder. */
      return Charset.defaultCharset().decode(bb).toString();
    } finally {
      stream.close();
    }

  }

  public static Float getOverallXMin(ArrayList<PlotData> plots) {
    float xmin = Float.MAX_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getXMin() < xmin) {
        xmin = p.getXMin();
      }
    }
    return xmin;
  }

  public static Float getOverallXMax(ArrayList<PlotData> plots) {
    float xmax = Float.MIN_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getXMax() > xmax) {
        xmax = p.getXMax();
      }
    }
    return xmax;

  }

  public static Float getOverallYMax(ArrayList<PlotData> plots) {
    float ymax = Float.MIN_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getYMax() > ymax) {
        ymax = p.getYMax();
      }
    }

    return ymax;
  }

  public static Float getOverallYMin(ArrayList<PlotData> plots) {
    float ymin = Float.MAX_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getYMin() < ymin) {
        ymin = p.getYMin();
      }
    }

    return ymin;
  }

  public static double getTicksRange(int nOfTicks, ArrayList<PlotData> plots) {
    double range = getOverallYMax(plots);
    int tickCount = nOfTicks;
    double unroundedTickSize = range / (tickCount - 1);

    double x = Math.ceil(Math.log10(unroundedTickSize) - 1);
    double pow10x = Math.pow(10, x);
    double roundedTickRange = Math.ceil(unroundedTickSize / pow10x) * pow10x;

    return roundedTickRange;
  }

}
