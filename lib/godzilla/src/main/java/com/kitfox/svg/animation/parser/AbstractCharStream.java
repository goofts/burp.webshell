package com.kitfox.svg.animation.parser;

import java.io.IOException;
import javassist.bytecode.Opcode;

public abstract class AbstractCharStream implements CharStream {
    public static final int DEFAULT_BUF_SIZE = 4096;
    protected int available;
    protected int[] bufcolumn;
    protected char[] buffer;
    protected int[] bufline;
    protected int bufpos = -1;
    protected int bufsize;
    protected int column = 0;
    protected int inBuf = 0;
    protected int line = 1;
    protected int maxNextCharInd = 0;
    protected char[] nextCharBuf;
    protected int nextCharInd = -1;
    protected boolean prevCharIsCR = false;
    protected boolean prevCharIsLF = false;
    private int tabSize = 1;
    protected int tokenBegin;
    private boolean trackLineColumn = true;

    /* access modifiers changed from: protected */
    public abstract void streamClose() throws IOException;

    /* access modifiers changed from: protected */
    public abstract int streamRead(char[] cArr, int i, int i2) throws IOException;

    static final int hexval(char c) throws IOException {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case Opcode.FSTORE /*{ENCODED_INT: 56}*/:
                return 8;
            case Opcode.DSTORE /*{ENCODED_INT: 57}*/:
                return 9;
            case 'A':
            case Opcode.LADD /*{ENCODED_INT: 97}*/:
                return 10;
            case 'B':
            case Opcode.FADD /*{ENCODED_INT: 98}*/:
                return 11;
            case 'C':
            case Opcode.DADD /*{ENCODED_INT: 99}*/:
                return 12;
            case 'D':
            case 'd':
                return 13;
            case 'E':
            case 'e':
                return 14;
            case Opcode.FSTORE_3 /*{ENCODED_INT: 70}*/:
            case 'f':
                return 15;
            default:
                throw new IOException("Invalid hex char '" + c + "' provided!");
        }
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public void setTabSize(int i) {
        this.tabSize = i;
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public int getTabSize() {
        return this.tabSize;
    }

    /* access modifiers changed from: protected */
    public void expandBuff(boolean wrapAround) {
        char[] newbuffer = new char[(this.bufsize + 2048)];
        int[] newbufline = new int[(this.bufsize + 2048)];
        int[] newbufcolumn = new int[(this.bufsize + 2048)];
        if (wrapAround) {
            try {
                System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
                this.buffer = newbuffer;
                System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
                this.bufline = newbufline;
                System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
                this.bufcolumn = newbufcolumn;
                this.bufpos += this.bufsize - this.tokenBegin;
                this.maxNextCharInd = this.bufpos;
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        } else {
            System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
            this.buffer = newbuffer;
            System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
            this.bufline = newbufline;
            System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
            this.bufcolumn = newbufcolumn;
            this.bufpos -= this.tokenBegin;
            this.maxNextCharInd = this.bufpos;
        }
        this.bufsize += 2048;
        this.available = this.bufsize;
        this.tokenBegin = 0;
    }

    /* access modifiers changed from: protected */
    public void fillBuff() throws IOException {
        if (this.maxNextCharInd == this.available) {
            if (this.available == this.bufsize) {
                if (this.tokenBegin > 2048) {
                    this.maxNextCharInd = 0;
                    this.bufpos = 0;
                    this.available = this.tokenBegin;
                } else if (this.tokenBegin < 0) {
                    this.maxNextCharInd = 0;
                    this.bufpos = 0;
                } else {
                    expandBuff(false);
                }
            } else if (this.available > this.tokenBegin) {
                this.available = this.bufsize;
            } else if (this.tokenBegin - this.available < 2048) {
                expandBuff(true);
            } else {
                this.available = this.tokenBegin;
            }
        }
        try {
            int i = streamRead(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd);
            if (i == -1) {
                streamClose();
                throw new IOException();
            } else {
                this.maxNextCharInd += i;
            }
        } catch (IOException e) {
            this.bufpos--;
            backup(0);
            if (this.tokenBegin == -1) {
                this.tokenBegin = this.bufpos;
            }
            throw e;
        }
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public char beginToken() throws IOException {
        this.tokenBegin = -1;
        char c = readChar();
        this.tokenBegin = this.bufpos;
        return c;
    }

    /* access modifiers changed from: protected */
    public void updateLineColumn(char c) {
        this.column++;
        if (this.prevCharIsLF) {
            this.prevCharIsLF = false;
            this.column = 1;
            this.line++;
        } else if (this.prevCharIsCR) {
            this.prevCharIsCR = false;
            if (c == '\n') {
                this.prevCharIsLF = true;
            } else {
                this.column = 1;
                this.line++;
            }
        }
        switch (c) {
            case '\t':
                this.column--;
                this.column += this.tabSize - (this.column % this.tabSize);
                break;
            case '\n':
                this.prevCharIsLF = true;
                break;
            case '\r':
                this.prevCharIsCR = true;
                break;
        }
        this.bufline[this.bufpos] = this.line;
        this.bufcolumn[this.bufpos] = this.column;
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public char readChar() throws IOException {
        if (this.inBuf > 0) {
            this.inBuf--;
            int i = this.bufpos + 1;
            this.bufpos = i;
            if (i == this.bufsize) {
                this.bufpos = 0;
            }
            return this.buffer[this.bufpos];
        }
        this.bufpos++;
        if (this.bufpos >= this.maxNextCharInd) {
            fillBuff();
        }
        char c = this.buffer[this.bufpos];
        if (!this.trackLineColumn) {
            return c;
        }
        updateLineColumn(c);
        return c;
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public int getBeginColumn() {
        return this.bufcolumn[this.tokenBegin];
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public int getBeginLine() {
        return this.bufline[this.tokenBegin];
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public int getEndColumn() {
        return this.bufcolumn[this.bufpos];
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public int getEndLine() {
        return this.bufline[this.bufpos];
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public void backup(int amount) {
        this.inBuf += amount;
        this.bufpos -= amount;
        if (this.bufpos < 0) {
            this.bufpos += this.bufsize;
        }
    }

    public AbstractCharStream(int startline, int startcolumn, int buffersize) {
        this.line = startline;
        this.column = startcolumn - 1;
        this.bufsize = buffersize;
        this.available = buffersize;
        this.buffer = new char[buffersize];
        this.bufline = new int[buffersize];
        this.bufcolumn = new int[buffersize];
        this.nextCharBuf = new char[4096];
    }

    public void reInit(int startline, int startcolumn, int buffersize) {
        this.line = startline;
        this.column = startcolumn - 1;
        if (this.buffer == null || buffersize != this.buffer.length) {
            this.bufsize = buffersize;
            this.available = buffersize;
            this.buffer = new char[buffersize];
            this.bufline = new int[buffersize];
            this.bufcolumn = new int[buffersize];
            this.nextCharBuf = new char[4096];
        }
        this.prevCharIsCR = false;
        this.prevCharIsLF = false;
        this.maxNextCharInd = 0;
        this.inBuf = 0;
        this.tokenBegin = 0;
        this.bufpos = -1;
        this.nextCharInd = -1;
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public String getImage() {
        if (this.bufpos >= this.tokenBegin) {
            return new String(this.buffer, this.tokenBegin, (this.bufpos - this.tokenBegin) + 1);
        }
        return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public char[] getSuffix(int len) {
        char[] ret = new char[len];
        if (this.bufpos + 1 >= len) {
            System.arraycopy(this.buffer, (this.bufpos - len) + 1, ret, 0, len);
        } else {
            System.arraycopy(this.buffer, this.bufsize - ((len - this.bufpos) - 1), ret, 0, (len - this.bufpos) - 1);
            System.arraycopy(this.buffer, 0, ret, (len - this.bufpos) - 1, this.bufpos + 1);
        }
        return ret;
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public void done() {
        this.nextCharBuf = null;
        this.buffer = null;
        this.bufline = null;
        this.bufcolumn = null;
    }

    public void adjustBeginLineColumn(int nNewLine, int newCol) {
        int len;
        int start = this.tokenBegin;
        if (this.bufpos >= this.tokenBegin) {
            len = (this.bufpos - this.tokenBegin) + this.inBuf + 1;
        } else {
            len = (this.bufsize - this.tokenBegin) + this.bufpos + 1 + this.inBuf;
        }
        int i = 0;
        int j = 0;
        int columnDiff = 0;
        while (i < len) {
            int[] iArr = this.bufline;
            j = start % this.bufsize;
            int i2 = iArr[j];
            int[] iArr2 = this.bufline;
            start++;
            int k = start % this.bufsize;
            if (i2 != iArr2[k]) {
                break;
            }
            this.bufline[j] = nNewLine;
            int nextColDiff = (this.bufcolumn[k] + columnDiff) - this.bufcolumn[j];
            this.bufcolumn[j] = newCol + columnDiff;
            columnDiff = nextColDiff;
            i++;
        }
        if (i < len) {
            int newLine = nNewLine + 1;
            this.bufline[j] = nNewLine;
            this.bufcolumn[j] = newCol + columnDiff;
            int i3 = i;
            while (true) {
                int i4 = i3 + 1;
                if (i3 >= len) {
                    break;
                }
                int[] iArr3 = this.bufline;
                j = start % this.bufsize;
                start++;
                if (iArr3[j] != this.bufline[start % this.bufsize]) {
                    this.bufline[j] = newLine;
                    i3 = i4;
                    newLine++;
                } else {
                    this.bufline[j] = newLine;
                    i3 = i4;
                }
            }
        }
        this.line = this.bufline[j];
        this.column = this.bufcolumn[j];
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public void setTrackLineColumn(boolean tlc) {
        this.trackLineColumn = tlc;
    }

    @Override // com.kitfox.svg.animation.parser.CharStream
    public boolean isTrackLineColumn() {
        return this.trackLineColumn;
    }
}
