package com.kitfox.svg.animation.parser;

import java.io.IOException;

public interface CharStream {
    void backup(int i);

    char beginToken() throws IOException;

    void done();

    int getBeginColumn();

    int getBeginLine();

    int getEndColumn();

    int getEndLine();

    String getImage();

    char[] getSuffix(int i);

    int getTabSize();

    boolean isTrackLineColumn();

    char readChar() throws IOException;

    void setTabSize(int i);

    void setTrackLineColumn(boolean z);
}
