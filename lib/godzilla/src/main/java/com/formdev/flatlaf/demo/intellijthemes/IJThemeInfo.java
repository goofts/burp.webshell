package com.formdev.flatlaf.demo.intellijthemes;

import java.io.File;
import java.io.Serializable;

public class IJThemeInfo implements Serializable {
    protected boolean dark;
    protected String lafClassName;
    protected String license;
    protected String licenseFile;
    protected String name;
    protected String resourceName;
    protected String sourceCodePath;
    protected String sourceCodeUrl;
    protected File themeFile;

    public IJThemeInfo(String name2, String resourceName2, boolean dark2, String license2, String licenseFile2, String sourceCodeUrl2, String sourceCodePath2, File themeFile2, String lafClassName2) {
        this.name = name2;
        this.resourceName = resourceName2;
        this.dark = dark2;
        this.license = license2;
        this.licenseFile = licenseFile2;
        this.sourceCodeUrl = sourceCodeUrl2;
        this.sourceCodePath = sourceCodePath2;
        this.themeFile = themeFile2;
        this.lafClassName = lafClassName2;
    }

    public IJThemeInfo(String resourceName2, String lafClassName2) {
        this.resourceName = resourceName2;
        this.lafClassName = lafClassName2;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public void setResourceName(String resourceName2) {
        this.resourceName = resourceName2;
    }

    public String getLafClassName() {
        return this.lafClassName;
    }

    public void setLafClassName(String lafClassName2) {
        this.lafClassName = lafClassName2;
    }

    public String toString() {
        return "IJThemeInfo{name='" + this.name + '\'' + ", resourceName='" + this.resourceName + '\'' + ", dark=" + this.dark + ", license='" + this.license + '\'' + ", licenseFile='" + this.licenseFile + '\'' + ", sourceCodeUrl='" + this.sourceCodeUrl + '\'' + ", sourceCodePath='" + this.sourceCodePath + '\'' + ", themeFile=" + this.themeFile + ", lafClassName='" + this.lafClassName + '\'' + '}';
    }
}
