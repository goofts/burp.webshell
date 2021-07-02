package com.kitfox.svg.app.ant;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.xml.ColorTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class SVGToImageAntTask extends Task {
    boolean antiAlias = true;
    Color backgroundColor = null;
    boolean clipToViewBox = false;
    File destDir;
    private ArrayList<FileSet> filesets = new ArrayList<>();
    private String format = "png";
    int height = -1;
    String interpolation = "bicubic";
    boolean sizeToFit = true;
    boolean verbose = false;
    int width = -1;

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format2) {
        this.format = format2;
    }

    public void setBackgroundColor(String bgColor) {
        this.backgroundColor = ColorTable.parseColor(bgColor);
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public void setAntiAlias(boolean antiAlias2) {
        this.antiAlias = antiAlias2;
    }

    public void setInterpolation(String interpolation2) {
        this.interpolation = interpolation2;
    }

    public void setSizeToFit(boolean sizeToFit2) {
        this.sizeToFit = sizeToFit2;
    }

    public void setClipToViewBox(boolean clipToViewBox2) {
        this.clipToViewBox = clipToViewBox2;
    }

    public void setVerbose(boolean verbose2) {
        this.verbose = verbose2;
    }

    public void setDestDir(File destDir2) {
        this.destDir = destDir2;
    }

    public void addFileset(FileSet set) {
        this.filesets.add(set);
    }

    public void execute() {
        if (this.verbose) {
            log("Building SVG images");
        }
        Iterator<FileSet> it = this.filesets.iterator();
        while (it.hasNext()) {
            FileScanner scanner = it.next().getDirectoryScanner(getProject());
            String[] files = scanner.getIncludedFiles();
            try {
                File basedir = scanner.getBasedir();
                if (this.verbose) {
                    log("Scaning " + basedir);
                }
                for (String str : files) {
                    translate(basedir, str);
                }
            } catch (Exception e) {
                throw new BuildException(e);
            }
        }
    }

    private void translate(File baseDir, String shortName) throws BuildException {
        File source = new File(baseDir, shortName);
        if (this.verbose) {
            log("Reading file: " + source);
        }
        Matcher matchName = Pattern.compile("(.*)\\.svg", 2).matcher(shortName);
        if (matchName.matches()) {
            shortName = matchName.group(1);
        }
        String shortName2 = shortName + "." + this.format;
        SVGIcon icon = new SVGIcon();
        icon.setSvgURI(source.toURI());
        icon.setAntiAlias(this.antiAlias);
        if (this.interpolation.equals("nearest neighbor")) {
            icon.setInterpolation(0);
        } else if (this.interpolation.equals("bilinear")) {
            icon.setInterpolation(1);
        } else if (this.interpolation.equals("bicubic")) {
            icon.setInterpolation(2);
        }
        int iconWidth = this.width > 0 ? this.width : icon.getIconWidth();
        int iconHeight = this.height > 0 ? this.height : icon.getIconHeight();
        icon.setClipToViewbox(this.clipToViewBox);
        icon.setPreferredSize(new Dimension(iconWidth, iconHeight));
        icon.setScaleToFit(this.sizeToFit);
        BufferedImage image = new BufferedImage(iconWidth, iconHeight, 2);
        Graphics2D g = image.createGraphics();
        if (this.backgroundColor != null) {
            g.setColor(this.backgroundColor);
            g.fillRect(0, 0, iconWidth, iconHeight);
        }
        g.setClip(0, 0, iconWidth, iconHeight);
        icon.paintIcon((Component) null, (Graphics) g, 0, 0);
        g.dispose();
        File outFile = this.destDir == null ? new File(baseDir, shortName2) : new File(this.destDir, shortName2);
        if (this.verbose) {
            log("Writing file: " + outFile);
        }
        try {
            ImageIO.write(image, this.format, outFile);
            SVGCache.getSVGUniverse().clear();
        } catch (IOException e) {
            log("Error writing image: " + e.getMessage());
            throw new BuildException(e);
        }
    }
}
