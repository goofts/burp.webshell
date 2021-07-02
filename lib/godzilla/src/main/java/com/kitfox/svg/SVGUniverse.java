package com.kitfox.svg;

import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.util.Base64InputStream;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class SVGUniverse implements Serializable {
    public static final String INPUTSTREAM_SCHEME = "svgSalamander";
    public static final long serialVersionUID = 0;
    static ThreadLocal<SAXParser> threadSAXParser = new ThreadLocal<>();
    private transient PropertyChangeSupport changes = new PropertyChangeSupport(this);
    protected double curTime = 0.0d;
    private boolean imageDataInlineOnly = false;
    final HashMap<URI, SVGDiagram> loadedDocs = new HashMap<>();
    final HashMap<String, Font> loadedFonts = new HashMap<>();
    final HashMap<URL, SoftReference<BufferedImage>> loadedImages = new HashMap<>();
    private boolean verbose = false;

    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        this.changes.removePropertyChangeListener(l);
    }

    public void clear() {
        this.loadedDocs.clear();
        this.loadedFonts.clear();
        this.loadedImages.clear();
    }

    public double getCurTime() {
        return this.curTime;
    }

    public void setCurTime(double curTime2) {
        double oldTime = this.curTime;
        this.curTime = curTime2;
        this.changes.firePropertyChange("curTime", new Double(oldTime), new Double(curTime2));
    }

    public void updateTime() throws SVGException {
        for (SVGDiagram dia : this.loadedDocs.values()) {
            dia.updateTime(this.curTime);
        }
    }

    /* access modifiers changed from: package-private */
    public void registerFont(Font font) {
        this.loadedFonts.put(font.getFontFace().getFontFamily(), font);
    }

    public Font getDefaultFont() {
        Iterator<Font> it = this.loadedFonts.values().iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public Font getFont(String fontName) {
        return this.loadedFonts.get(fontName);
    }

    /* access modifiers changed from: package-private */
    public URL registerImage(URI imageURI) {
        if (imageURI.getScheme().equals("data")) {
            String path = imageURI.getRawSchemeSpecificPart();
            int idx = path.indexOf(59);
            path.substring(0, idx);
            String content = path.substring(idx + 1);
            if (content.startsWith("base64")) {
                try {
                    BufferedImage img = ImageIO.read(new Base64InputStream(new ByteArrayInputStream(content.substring(6).getBytes())));
                    int urlIdx = 0;
                    while (true) {
                        URL url = new URL("inlineImage", "localhost", "img" + urlIdx);
                        if (!this.loadedImages.containsKey(url)) {
                            this.loadedImages.put(url, new SoftReference<>(img));
                            return url;
                        }
                        urlIdx++;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not decode inline image", (Throwable) ex);
                }
            }
            return null;
        }
        try {
            URL url2 = imageURI.toURL();
            registerImage(url2);
            return url2;
        } catch (MalformedURLException ex2) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Bad url", (Throwable) ex2);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void registerImage(URL imageURL) {
        SoftReference<BufferedImage> ref;
        if (!this.loadedImages.containsKey(imageURL)) {
            try {
                String fileName = imageURL.getFile();
                if (".svg".equals(fileName.substring(fileName.length() - 4).toLowerCase())) {
                    SVGIcon icon = new SVGIcon();
                    icon.setSvgURI(imageURL.toURI());
                    BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
                    Graphics2D g = img.createGraphics();
                    icon.paintIcon((Component) null, (Graphics) g, 0, 0);
                    g.dispose();
                    ref = new SoftReference<>(img);
                } else {
                    ref = new SoftReference<>(ImageIO.read(imageURL));
                }
                this.loadedImages.put(imageURL, ref);
            } catch (Exception e) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not load image: " + imageURL, (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public BufferedImage getImage(URL imageURL) {
        SoftReference<BufferedImage> ref = this.loadedImages.get(imageURL);
        if (ref == null) {
            return null;
        }
        BufferedImage img = ref.get();
        if (img != null) {
            return img;
        }
        try {
            img = ImageIO.read(imageURL);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not load image", (Throwable) e);
        }
        this.loadedImages.put(imageURL, new SoftReference<>(img));
        return img;
    }

    public SVGElement getElement(URI path) {
        return getElement(path, true);
    }

    public SVGElement getElement(URL path) {
        try {
            return getElement(new URI(path.toString()), true);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse url " + path, (Throwable) e);
            return null;
        }
    }

    public SVGElement getElement(URI path, boolean loadIfAbsent) {
        try {
            URI xmlBase = new URI(path.getScheme(), path.getSchemeSpecificPart(), null);
            SVGDiagram dia = this.loadedDocs.get(xmlBase);
            if (dia == null && loadIfAbsent) {
                loadSVG(xmlBase.toURL(), false);
                dia = this.loadedDocs.get(xmlBase);
                if (dia == null) {
                    return null;
                }
            }
            String fragment = path.getFragment();
            return fragment == null ? dia.getRoot() : dia.getElement(fragment);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse path " + path, (Throwable) e);
            return null;
        }
    }

    public SVGDiagram getDiagram(URI xmlBase) {
        return getDiagram(xmlBase, true);
    }

    public SVGDiagram getDiagram(URI xmlBase, boolean loadIfAbsent) {
        URL url;
        if (xmlBase == null) {
            return null;
        }
        SVGDiagram dia = this.loadedDocs.get(xmlBase);
        if (dia != null || !loadIfAbsent) {
            return dia;
        }
        try {
            if (!"jar".equals(xmlBase.getScheme()) || xmlBase.getPath() == null || xmlBase.getPath().contains("!/")) {
                url = xmlBase.toURL();
            } else {
                url = SVGUniverse.class.getResource(xmlBase.getPath());
            }
            loadSVG(url, false);
            return this.loadedDocs.get(xmlBase);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse", (Throwable) e);
            return null;
        }
    }

    private InputStream createDocumentInputStream(InputStream is) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(is);
        bin.mark(2);
        int b0 = bin.read();
        int b1 = bin.read();
        bin.reset();
        if (((b1 << 8) | b0) == 35615) {
            return new GZIPInputStream(bin);
        }
        return bin;
    }

    public URI loadSVG(URL docRoot) {
        return loadSVG(docRoot, false);
    }

    public URI loadSVG(URL docRoot, boolean forceLoad) {
        try {
            URI uri = new URI(docRoot.toString());
            if (this.loadedDocs.containsKey(uri) && !forceLoad) {
                return uri;
            }
            InputStream is = docRoot.openStream();
            URI result = loadSVG(uri, new InputSource(createDocumentInputStream(is)));
            is.close();
            return result;
        } catch (URISyntaxException ex) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse", (Throwable) ex);
            return null;
        } catch (IOException e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse", (Throwable) e);
            return null;
        }
    }

    public URI loadSVG(InputStream is, String name) throws IOException {
        return loadSVG(is, name, false);
    }

    public URI loadSVG(InputStream is, String name, boolean forceLoad) throws IOException {
        URI uri = getStreamBuiltURI(name);
        if (uri == null) {
            return null;
        }
        return (!this.loadedDocs.containsKey(uri) || forceLoad) ? loadSVG(uri, new InputSource(createDocumentInputStream(is))) : uri;
    }

    public URI loadSVG(Reader reader, String name) {
        return loadSVG(reader, name, false);
    }

    public URI loadSVG(Reader reader, String name, boolean forceLoad) {
        URI uri = getStreamBuiltURI(name);
        if (uri == null) {
            return null;
        }
        return (!this.loadedDocs.containsKey(uri) || forceLoad) ? loadSVG(uri, new InputSource(reader)) : uri;
    }

    public URI getStreamBuiltURI(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        if (name.charAt(0) != '/') {
            name = '/' + name;
        }
        try {
            return new URI(INPUTSTREAM_SCHEME, name, null);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse", (Throwable) e);
            return null;
        }
    }

    private XMLReader getXMLReader() throws SAXException, ParserConfigurationException {
        SAXParser saxParser = threadSAXParser.get();
        if (saxParser == null) {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            saxParser = saxParserFactory.newSAXParser();
            threadSAXParser.set(saxParser);
        }
        return saxParser.getXMLReader();
    }

    /* access modifiers changed from: protected */
    public URI loadSVG(URI xmlBase, InputSource is) {
        SVGLoader handler = new SVGLoader(xmlBase, this, this.verbose);
        this.loadedDocs.put(xmlBase, handler.getLoadedDiagram());
        try {
            XMLReader reader = getXMLReader();
            reader.setEntityResolver(new EntityResolver() {
                /* class com.kitfox.svg.SVGUniverse.AnonymousClass1 */

                @Override // org.xml.sax.EntityResolver
                public InputSource resolveEntity(String publicId, String systemId) {
                    return new InputSource(new ByteArrayInputStream(new byte[0]));
                }
            });
            reader.setContentHandler(handler);
            reader.parse(is);
            handler.getLoadedDiagram().updateTime(this.curTime);
            return xmlBase;
        } catch (SAXParseException sex) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Error processing " + xmlBase, (Throwable) sex);
            this.loadedDocs.remove(xmlBase);
            return null;
        } catch (Throwable e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not load SVG " + xmlBase, e);
            return null;
        }
    }

    public ArrayList<URI> getLoadedDocumentURIs() {
        return new ArrayList<>(this.loadedDocs.keySet());
    }

    public void removeDocument(URI uri) {
        this.loadedDocs.remove(uri);
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean verbose2) {
        this.verbose = verbose2;
    }

    public SVGUniverse duplicate() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bs);
        os.writeObject(this);
        os.close();
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bs.toByteArray()));
        SVGUniverse universe = (SVGUniverse) is.readObject();
        is.close();
        return universe;
    }

    public boolean isImageDataInlineOnly() {
        return this.imageDataInlineOnly;
    }

    public void setImageDataInlineOnly(boolean imageDataInlineOnly2) {
        this.imageDataInlineOnly = imageDataInlineOnly2;
    }
}
