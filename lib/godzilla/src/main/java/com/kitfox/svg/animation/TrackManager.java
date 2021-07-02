package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElementException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class TrackManager implements Serializable {
    public static final long serialVersionUID = 0;
    HashMap<TrackKey, TrackBase> tracks = new HashMap<>();

    /* access modifiers changed from: package-private */
    public static class TrackKey {
        String name;
        int type;

        TrackKey(AnimationElement base) {
            this(base.getAttribName(), base.getAttribType());
        }

        TrackKey(String name2, int type2) {
            this.name = name2;
            this.type = type2;
        }

        public int hashCode() {
            return ((this.name == null ? 0 : this.name.hashCode()) * 97) + this.type;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TrackKey)) {
                return false;
            }
            TrackKey key = (TrackKey) obj;
            if (key.type != this.type || !key.name.equals(this.name)) {
                return false;
            }
            return true;
        }
    }

    public void addTrackElement(AnimationElement element) throws SVGElementException {
        TrackKey key = new TrackKey(element);
        TrackBase track = this.tracks.get(key);
        if (track == null) {
            if (element instanceof Animate) {
                switch (((Animate) element).getDataType()) {
                    case 0:
                        track = new TrackDouble(element);
                        break;
                    case 1:
                        track = new TrackColor(element);
                        break;
                    case 2:
                        track = new TrackPath(element);
                        break;
                    default:
                        throw new RuntimeException("");
                }
            } else if (element instanceof AnimateColor) {
                track = new TrackColor(element);
            } else if ((element instanceof AnimateTransform) || (element instanceof AnimateMotion)) {
                track = new TrackTransform(element);
            }
            this.tracks.put(key, track);
        }
        track.addElement(element);
    }

    public TrackBase getTrack(String name, int type) {
        if (type == 2) {
            TrackBase t = getTrack(name, 0);
            if (t != null) {
                return t;
            }
            TrackBase t2 = getTrack(name, 1);
            if (t2 == null) {
                return null;
            }
            return t2;
        }
        TrackBase t3 = this.tracks.get(new TrackKey(name, type));
        if (t3 != null) {
            return t3;
        }
        return this.tracks.get(new TrackKey(name, 2));
    }

    public int getNumTracks() {
        return this.tracks.size();
    }

    public Iterator<TrackBase> iterator() {
        return this.tracks.values().iterator();
    }
}
