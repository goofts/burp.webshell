package com.kitfox.svg.xml;

public class StyleSheetRule {
    final String className;
    final String styleName;
    final String tag;

    public StyleSheetRule(String styleName2, String tag2, String className2) {
        this.styleName = styleName2;
        this.tag = tag2;
        this.className = className2;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        if (this.styleName != null) {
            i = this.styleName.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i + 91) * 13;
        if (this.tag != null) {
            i2 = this.tag.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 13;
        if (this.className != null) {
            i3 = this.className.hashCode();
        }
        return i5 + i3;
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StyleSheetRule other = (StyleSheetRule) obj;
        if (this.styleName == null) {
            if (other.styleName != null) {
                return false;
            }
        } else if (!this.styleName.equals(other.styleName)) {
            return false;
        }
        if (this.tag == null) {
            if (other.tag != null) {
                return false;
            }
        } else if (!this.tag.equals(other.tag)) {
            return false;
        }
        if (this.className == null) {
            if (other.className == null) {
                return true;
            }
            return false;
        } else if (!this.className.equals(other.className)) {
            return false;
        }
        return true;
    }
}
