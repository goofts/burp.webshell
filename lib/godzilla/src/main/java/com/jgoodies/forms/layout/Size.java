package com.jgoodies.forms.layout;

import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;
import java.util.List;

public interface Size {
    boolean compressible();

    String encode();

    int maximumSize(Container container, List list, FormLayout.Measure measure, FormLayout.Measure measure2, FormLayout.Measure measure3);
}
